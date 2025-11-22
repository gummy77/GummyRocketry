package org.gumrockets.entity;

import com.google.common.base.Predicates;
import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.gumrockets.component.*;
import org.gumrockets.payload.UpdateRocketPayload;
import org.gumrockets.registry.ComponentRegistry;
import org.gumrockets.registry.EntityRegistry;
import org.gumrockets.registry.ItemRegistry;
import org.gumrockets.registry.ParticleRegistry;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RocketEntity extends Entity {
    private Rocket rocket;
    private Vec3d prevVelocity = Vec3d.ZERO;

    private boolean justAttached = false;
    private boolean hasPlayerInspecting = false;

    public static final EntitySettings settings = new EntitySettings(
            "rocket_entity",
            SpawnGroup.MISC,
            0.6f, 2f,
            true
    );

    public RocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        hasPlayerInspecting = false;
        if(getWorld() instanceof ServerWorld) {
            networkUpdateData();
            updateFuse();
            this.setBoundingBox(this.calculateBoundingBox());
        } else {
            List<PlayerEntity> entityList = getWorld().getEntitiesByClass(PlayerEntity.class, Box.of(getPos(), 25, 25, 25), Predicates.notNull());

            for (PlayerEntity playerEntity : entityList) {
                if(playerEntity.isMainPlayer() && playerEntity.isHolding(ItemRegistry.ROCKET_INSPECTOR)) {
                    hasPlayerInspecting = true;
                }
            }
        }

        if (this.getRocket() == null) return; // probably loading from NBT data or needs to be networked

        if(this.getRocket().getState() == null) {
            this.getRocket().createNewState();
        }

        switch (this.getRocket().getState().getLaunchState()) {
            case IDLE -> {
            }
            case IGNITION -> {
                if(this.getRocket().getState().getLaunchTimer() <= 0) {
                    this.rocket.getState().setLaunchState(RocketState.LaunchState.LAUNCHING);
                    this.move(MovementType.SELF, new Vec3d(0, 0.1f, 0));
                    this.setVelocity(0, 0.1, 0);
                    setRotation(RotationAxis.POSITIVE_Y.rotation(0));
                } else {
                    this.rocket.getState().setLaunchTimer(this.getRocket().getState().getLaunchTimer() - 0.05f); // 20 ticks/second -> 0.05s per tick
                }
                this.getRocket().getState().setRotation(this.getRocket().getState().getRotation().rotateLocalX(0.00001f));
                this.tickStages(true);
            }
            case LAUNCHING -> {
                this.updateRotation();
                this.tickStages(false);
            }
            case COASTING -> {
                this.updateRotation();

                if(Math.abs(getVelocity().y) <= 0.1) {
                    System.out.println("peaking: " + getPos().y);
                }

                getWorld().addImportantParticle(ParticleRegistry.EXHAUST, true,
                        getPos().x, getPos().y, getPos().z,
                        0, 0, 0
                );
            }
        }

        this.addVelocity(new Vec3d(0, -0.05f, 0));
        this.move(MovementType.SELF, this.getVelocity());


        super.tick();

        Vec3d velocityChange = prevVelocity.subtract(this.getVelocity());
        if(velocityChange.lengthSquared() > 1) {
            getWorld().createExplosion(
                    this,
                    getX(), getY(), getZ(),
                    (float) (Math.sqrt(velocityChange.length()) * getRocket().getHeight()) * 0.25f,
                    (Math.sqrt(velocityChange.length()) > 2.5),
                    World.ExplosionSourceType.BLOCK
            );
            this.kill();
        }

        justAttached = false;
        prevVelocity = getVelocity();
    }

    public boolean IsPlayerWatching() {
        return hasPlayerInspecting;
    }

    private void updateRotation() {
        Quaternionf rotation = new Quaternionf(this.getRocket().getState().getRotation());
        Quaternionf velocityRotation = new Quaternionf().rotationTo(new Vector3f(0f, 1f, 0), this.getVelocity().toVector3f());

        if(Float.isNaN(velocityRotation.x)) velocityRotation = new Quaternionf();

        Quaternionf newRotation = new Quaternionf(rotation);

        newRotation.slerp(velocityRotation, 0.1f);

        setRotation(newRotation);
    }

    private void setRotation(Quaternionf rotation) {
        this.getRocket().getState().setRotation(rotation);
    }

    private void tickStages(boolean isIgnition) {
        if (this.getRocket().getCurrentStage() == null) {
            this.getRocket().getState().setLaunchState(RocketState.LaunchState.COASTING);
            return;
        }

        if(this.getRocket().getCurrentStage().getBurnTimeRemaining() <= 0) {
            this.stage();
        } else {
            this.tickEngines(isIgnition);
        }
    }

    private void stage() {
        if(this.getRocket().getState().getCurrentStage() < this.getRocket().getStages().size() - 1) {
            if (getWorld() instanceof ServerWorld) {
                // create NBT save data for rocket
                RocketStage stageData = this.getRocket().getCurrentStage();
                NbtCompound nbt = new NbtCompound();

                DataResult<NbtElement> dataResult = RocketStage.CODEC.encodeStart(NbtOps.INSTANCE, stageData);
                if (dataResult.isSuccess()) {
                    nbt.put("stage_data", dataResult.getOrThrow());
                }

                // spawn stage entity
                StageEntity stageEntity = new StageEntity(EntityRegistry.STAGE_ENTITY, getWorld());
                stageEntity.setPosition(this.getPos().add(new Vec3d(0, 0.1f, 0))); // TODO when doing rotations fix this

//                stageEntity.setVelocity(this.getVelocity().multiply(0.9f));
                stageEntity.readCustomDataFromNbt(nbt);

                getWorld().spawnEntity(stageEntity);
            }
            this.addForce(1);
            this.move(MovementType.SELF, new Vec3d(0, this.getRocket().getCurrentStage().getHeight() + 0.1f, 0)); // TODO when doing rotations fix this
        }
        this.getRocket().getState().stage();
    }

    public void Launch() {
        if (getWorld() instanceof ServerWorld) {
            if (this.getRocket().getState().getLaunchState() == RocketState.LaunchState.IDLE && this.getRocket().getState().getLaunchTimer() > 0) {
                if(!justAttached) {
                    this.getRocket().getState().setLaunchState(RocketState.LaunchState.IGNITION);
                    detachFuse(false);
                }
            }
        }
    }

    private void tickEngines(boolean isIgnition) {
        ArrayList<RocketPart> parts = this.getRocket().getCurrentStage().getParts();

        for (RocketPart part : parts) {
            if(part.getEngineComponent() != null) {
                part.getEngineComponent().tick(getWorld(), this, part, isIgnition);
                if(!isIgnition) {
                    this.getRocket().getCurrentStage().setBurnTimeRemaining(this.getRocket().getCurrentStage().getBurnTimeRemaining() - part.getEngineComponent().getFuelConsumption() * 0.05f); // 20 ticks/second -> 0.05s per tick
                }
            }
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if(hand != Hand.MAIN_HAND) return super.interact(player, hand);;
        if(this.getRocket() == null || this.getRocket().getState() == null) return ActionResult.FAIL;

        if((getWorld() instanceof ServerWorld)) {
            ItemStack stackInHand = player.getStackInHand(hand);
            if (stackInHand != ItemStack.EMPTY) {
                if (stackInHand.getItem() == ItemRegistry.PAYLOAD_COMPASS) {
                    stackInHand.set(ComponentRegistry.PAYLOAD_COMPASS_COMPONENT_COMPONENT_TYPE,
                            new PayloadCompassComponent(
                                    GlobalPos.create(player.getWorld().getRegistryKey(),
                                            player.getBlockPos()
                                    ),
                                    this.getId()
                            )
                    );
                } else if (stackInHand.getItem() == ItemRegistry.BASIC_LAUNCH_KIT) {
                    if (this.getRocket().getState().getLaunchState() == RocketState.LaunchState.IDLE
                            && this.getRocket().getState().getLaunchTimer() > 0
                            && getFuseHolder() == null
                    ) {
                        if (player.getInventory().contains(ItemRegistry.FUSE.getDefaultStack()) || player.isCreative()) {
                            if (!player.isCreative())
                                player.getInventory().removeStack(player.getInventory().getSlotWithStack(ItemRegistry.FUSE.getDefaultStack()), 1);
                            this.attachFuse(player);
                            return ActionResult.success(true);
                        }
                    } else {
                        return ActionResult.FAIL;
                    }
                }
            } else {
                if (getFuseHolder() == player) {
                    detachFuse(!player.getAbilities().creativeMode);
                }
            }
        }
        return super.interact(player, hand);
    }

    private void printConsoleStats() {
        if(getWorld() instanceof ServerWorld) {
            for (int i = 0; i <= this.getRocket().getStages().size() - 1; i++) {
                RocketStage stage = this.getRocket().getStages().get(i);

                System.out.println("Stage: " + i);

                for (int j = 0; j <= stage.getParts().size() - 1; j++) {
                    RocketPart part = stage.getParts().get(j);

                    System.out.println(part.getType().toString());
                }

                System.out.println(" ");

                System.out.println("Fuel: " + stage.getBurnTimeRemaining() + "s");
                System.out.println("Thrust: " + stage.getParts().getFirst().getEngineComponent().getPower() + "N");
                System.out.println("Mass: " + stage.getMass() + "kg");

                System.out.println(" ");
            }
            System.out.println("TWR: " + getRocket().getTWR());
        }
    }


    @Override
    public boolean handleAttack(Entity attacker) {
        if(attacker instanceof PlayerEntity) {
            destroy();
            return true;
        }
        return super.handleAttack(attacker);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(source.getType().effects() == DamageEffects.BURNING) { // TODO or explosion
            getWorld().createExplosion(
                    this,
                    getX(), getY(), getZ(),
                    (float) getRocket().getMass() / 250,
                    true,
                    World.ExplosionSourceType.BLOCK
            );
            this.kill();
        } else {
            destroy();
        }
        return super.damage(source, amount);
    }

    private void destroy() {
        this.kill();
        float currentHeight = 0;
        for (RocketStage stage : getRocket().getStages()) {
            for (RocketPart part : stage.getParts()) {
                dropStack(part.getBlock().getBlock().asItem().getDefaultStack(), (float) part.getOffset().getY() + currentHeight);
            }
            currentHeight += stage.getHeight();
        }
    }

    public void addForce(float force) {
        Vector3f accelerationVector = this.getRocket().getState().getRotation().transformUnit(new Vector3f(0, 1, 0));
        accelerationVector.mul(force / (rocket.getMass()));
        accelerationVector.div(20);

        setRotation(getRocket().getState().getRotation().rotateLocalX(0.00025f * (float) Math.pow(getRocket().getTWR(), 3)));

        addVelocity(accelerationVector.x, accelerationVector.y, accelerationVector.z);
    }

    private void updateFuse() {
        if (getFuseHolder() != null) {
            if (!this.isAlive() || !this.getFuseHolder().isAlive()) {
                this.detachFuse(true);
            }
        }
    }

    public void attachFuse(Entity entity) {
        if (entity != null) {
            setFuseHolder(entity);
            justAttached = true;
        }
    }

    public void detachFuse(boolean dropItem) {
        if (getFuseHolder() != null) {
            setFuseHolder(null);
            if (dropItem) {
                this.dropItem(ItemRegistry.FUSE);
            }
        }
    }

    private void setFuseHolder(@Nullable Entity entity) {
        if(entity != null) {
            this.rocket.setFuseHolderID(entity.getId());
        } else {
            this.rocket.setFuseHolderID(-1);
        }

    }

    public Entity getFuseHolder() {
        if(rocket != null) {
            if (rocket.getFuseHolderID() != -1) {
                Entity holderEntity = this.getWorld().getEntityById(rocket.getFuseHolderID());
                if (holderEntity != null) {
                    return holderEntity;
                }
            }
        }
        return null;
    }

    public void setRocket(Rocket rocket) {
        this.rocket = rocket;
    }

    public Rocket getRocket() {
        return rocket;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    protected Vec3d getLeashOffset() {
        return Vec3d.ZERO;
    }

    @Override
    public Box calculateBoundingBox() {
        if(this.rocket != null) {
            return Box.of(this.getPos().add(0, this.rocket.getHeight()/2, 0), this.rocket.getWidth(), this.rocket.getHeight(), this.rocket.getWidth());
        } // TODO potentially add rotation? or something? so this looks more accurate when flying
        return super.calculateBoundingBox();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    public void networkUpdateData() {
        if(this.rocket != null) {
            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) getWorld(), this.getBlockPos())) {
                ServerPlayNetworking.send(player, new UpdateRocketPayload(this.getId(), this.rocket));
            }
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        DataResult<Rocket> dataResult = Rocket.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("rocket_data"));
        if(dataResult.isSuccess()) {
            this.rocket = dataResult.getOrThrow();
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.rocket != null) {
            DataResult<NbtElement> dataResult = Rocket.CODEC.encodeStart(NbtOps.INSTANCE, this.rocket);
            if (dataResult.isSuccess()) {
                nbt.put("rocket_data", dataResult.getOrThrow());
            }
        }
    }
}
