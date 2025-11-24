package org.gumrockets.entity;

import com.mojang.serialization.DataResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.gumrockets.component.Rocket;
import org.gumrockets.component.RocketPart;
import org.gumrockets.component.RocketStage;
import org.gumrockets.registry.BlockRegistry;
import org.gumrockets.registry.ComponentRegistry;
import org.gumrockets.registry.EntityRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class RocketPartBlockEntity extends BlockEntity {
    private RocketPart rocketPart;

    public RocketPartBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.ROCKET_PART_BLOCK_ENTITY , pos, state);
    }

    public RocketPart getRocketPart() {
        return this.rocketPart;
    }
    public void setRocketPart(RocketPart rocketPart) {
        this.rocketPart = rocketPart;
        this.markDirty();
    }

    public void startAssembly(RocketPart.PartMaterial maxMaterial, BlockPos selectedBlock) {
        assert world != null;
        // find base block (lowest block)
        BlockPos lowestBlock = getLowestBlock(selectedBlock);
        BlockPos currentBlock = lowestBlock;

        // find stages (up to next engine)
        // TODO add height limit checks
        ArrayList<RocketStage> stages = getMainSegments(currentBlock);
        if (stages.isEmpty()){
            return; // TODO fail Assembly
        }

        int offset = 0;
        for (RocketStage stage : stages) {
            offset += stage.getHeight();
        }
        assert currentBlock != null;
        currentBlock = currentBlock.up(offset);

        offset = stages.getLast().getParts().size();

        // add Payload (if there)
        if(Objects.requireNonNull(getPartAtPos(currentBlock)).getType() == RocketPart.PartType.PAYLOAD) {
            stages.getLast().addPart(Objects.requireNonNull(getPartAtPos(currentBlock)).setOffset(new Vec3d(0, offset, 0)));
            currentBlock = currentBlock.up();
            offset++;
        }

        // add nose cone
        if(Objects.requireNonNull(getPartAtPos(currentBlock)).getType() == RocketPart.PartType.NOSE) {
            stages.getLast().addPart(Objects.requireNonNull(getPartAtPos(currentBlock)).setOffset(new Vec3d(0, offset, 0)));
            currentBlock = currentBlock.up();
            offset++;
        } else {
            return; // TODO fail Assembly
        }

        // destroy blocks
        int stageoffset = 0;
        for (RocketStage stage : stages) {
            ArrayList<RocketPart> parts = stage.getParts();
            for (RocketPart part : parts) {
                world.breakBlock(lowestBlock.add(0, Math.round((float)part.getOffset().y + stageoffset), 0), false);
            }
            stageoffset += stage.getHeight();
        }

        // create NBT save data for rocket
        Rocket rocketData = new Rocket(stages, lowestBlock.toBottomCenterPos());
        NbtCompound nbt = new NbtCompound();

        DataResult<NbtElement> dataResult = Rocket.CODEC.encodeStart(NbtOps.INSTANCE, rocketData);
        if (dataResult.isSuccess()) {
            nbt.put("rocket_data", dataResult.getOrThrow());
        }

        // spawn rocket entity
        RocketEntity rocketEntity = new RocketEntity(EntityRegistry.ROCKET_ENTITY, world);
        rocketEntity.setPosition(lowestBlock.toBottomCenterPos());
        rocketEntity.readCustomDataFromNbt(nbt);

        world.spawnEntity(rocketEntity);
    }

    private RocketPart getPartAtPos(BlockPos pos) {
        assert world != null;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof RocketPartBlockEntity rocketPartBlockEntity) {
            return rocketPartBlockEntity.getRocketPart();
        }
        return null;
    }

    private BlockPos getLowestBlock(BlockPos startPos) {
        if(world instanceof ServerWorld) {
            BlockPos lowestBlock = startPos;
            BlockEntity blockEntity = world.getBlockEntity(startPos);

            while (blockEntity instanceof RocketPartBlockEntity) {
                lowestBlock = lowestBlock.down();
                blockEntity = world.getBlockEntity(lowestBlock);
            }
            return lowestBlock.up();
        }
        return null;
    }

    private ArrayList<RocketStage> getMainSegments(BlockPos currentPos) {
        ArrayList<RocketStage> stages = new ArrayList<>();

        RocketPart currentPart = getPartAtPos(currentPos);

        assert currentPart != null;

        while (currentPart.getType() == RocketPart.PartType.ENGINE){
            int offset = 0;
            RocketStage currentStage = new RocketStage();
            currentStage.addPart(currentPart.setOffset(new Vec3d(0, offset, 0)));
            currentPos = currentPos.up();
            currentPart = getPartAtPos(currentPos);
            offset++;

            while (Objects.requireNonNull(currentPart).getType() == RocketPart.PartType.FUEL || currentPart.getType() == RocketPart.PartType.COUPLER) {
                if(currentPart.getType() == RocketPart.PartType.COUPLER) {

                    // TODO add boosters

                    currentStage.addPart(currentPart.setOffset(new Vec3d(0, offset, 0)));
                    currentPos = currentPos.up();
                    currentPart = getPartAtPos(currentPos);
                    offset++;
                    continue;
                }

                currentStage.addPart(currentPart.setOffset(new Vec3d(0, offset, 0)));
                currentPos = currentPos.up();
                currentPart = getPartAtPos(currentPos);
                offset++;
            }
            currentStage.calculateBurnTime();
            stages.add(currentStage);
        }

        return stages;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.rocketPart != null) {
            DataResult<NbtElement> dataResult = RocketPart.CODEC.encodeStart(NbtOps.INSTANCE, this.rocketPart.clone());
            if (dataResult.isSuccess()) {
                nbt.put("rocket_part", dataResult.getOrThrow());
            }
        }

        super.writeNbt(nbt, registryLookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        DataResult<RocketPart> dataResult = RocketPart.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("rocket_part"));
        if(dataResult.isSuccess()) {
            this.rocketPart = dataResult.getOrThrow();
        }

    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.rocketPart = components.getOrDefault(ComponentRegistry.ROCKET_PART_COMPONENT_TYPE, null).clone();
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(ComponentRegistry.ROCKET_PART_COMPONENT_TYPE, this.rocketPart);
    }

    @Override
    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        nbt.remove("rocket_part");
    }
}
