package org.gumrockets.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.gumrockets.component.PayloadTypes;
import org.gumrockets.component.Rocket;
import org.gumrockets.payload.UpdatePayloadPayload;
import org.gumrockets.payload.UpdateRocketPayload;
import org.gumrockets.registry.ItemRegistry;

public class PayloadEntity extends Entity {

    private PayloadTypes payloadType;

    public static final EntitySettings settings = new EntitySettings(
            "payload_entity",
            SpawnGroup.MISC,
            0.4f, 0.6f,
            true
    );

    public PayloadEntity(EntityType<?> type, World world) {
        super(type, world);
        payloadType = PayloadTypes.NONE;
    }

    public PayloadEntity(EntityType<?> type, World world, PayloadTypes _payloadType) {
        super(type, world);
        payloadType = _payloadType;
    }

    @Override
    public void tick() {
        if(getWorld() instanceof ServerWorld) {
            networkUpdateData();
        } else {

        }

        this.addVelocity(new Vec3d(0, -0.05f, 0));
        this.move(MovementType.SELF, this.getVelocity());

        super.tick();
    }

    public PayloadTypes getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(PayloadTypes payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public boolean handleAttack(Entity attacker) {
        return super.handleAttack(attacker);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(!source.isSourceCreativePlayer()) {
            switch (payloadType) {
                case EMPTY -> {
                    dropStack(new ItemStack(ItemRegistry.EMPTY_PAYLOAD, random.nextBetween(0, 1)));
                }
                case STARDUST_CATCHER -> {
                    dropStack(new ItemStack(ItemRegistry.STARDUST, random.nextBetween(2, 4)));
                    dropStack(new ItemStack(ItemRegistry.STARDUST_CATCHER, random.nextBetween(0, 1)));
                }
            }
        }
        this.kill();

        return super.damage(source, amount);
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
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    public void networkUpdateData() {
        if(this.payloadType != null) {
            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) getWorld(), this.getBlockPos())) {
                ServerPlayNetworking.send(player, new UpdatePayloadPayload(this.getId(), this.payloadType.toString()));
            }
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        DataResult<String> dataResult = Codec.STRING.parse(NbtOps.INSTANCE, nbt.getCompound("payload_type"));
        if(dataResult.isSuccess()) {
            this.payloadType = PayloadTypes.valueOf(dataResult.getOrThrow());
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.payloadType != null) {
            DataResult<NbtElement> dataResult = Codec.STRING.encodeStart(NbtOps.INSTANCE, this.payloadType.toString());
            if (dataResult.isSuccess()) {
                nbt.put("payload_type", dataResult.getOrThrow());
            }
        }
    }
}
