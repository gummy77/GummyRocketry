package org.gumrockets.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.gumrockets.component.RocketPart;
import org.gumrockets.entity.RocketPartBlockEntity;
import org.jetbrains.annotations.Nullable;

public class RocketPartBlock extends BlockWithEntity {
    public RocketPartBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof RocketPartBlockEntity rocketPartBlockEntity) {
            RocketPart rocketPart = rocketPartBlockEntity.getRocketPart();
            if(rocketPart != null) {
                float edgeWidth = (16f - rocketPart.getWidth()) / 2f;

                float startWidth = edgeWidth / 16f;
                float endWidth = (16f - edgeWidth) / 16f;

                return VoxelShapes.cuboid(startWidth, 0f, startWidth, endWidth, 1f, endWidth);
            }
        }
        return VoxelShapes.cuboid(0.25f, 0f, 0.25f, 0.75f, 1f, 0.75f);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RocketPartBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
