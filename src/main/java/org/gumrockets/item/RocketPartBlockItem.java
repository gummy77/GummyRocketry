package org.gumrockets.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.gumrockets.component.RocketPart;
import org.gumrockets.entity.RocketPartBlockEntity;
import org.gumrockets.registry.ComponentRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class RocketPartBlockItem extends BlockItem {
    public RocketPartBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        //updateBlockComponents(user.getStackInHand(hand));
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        RocketPart rocketPart = stack.get(ComponentRegistry.ROCKET_PART_COMPONENT_TYPE);

        if (rocketPart != null) {
            tooltip.add(Text.of("§7Mass: §2" + rocketPart.getMass() + "kg"));

            if(rocketPart.getPayloadCarrierComponent() != null) {
                tooltip.add(Text.of("§7Max Payload: §2" + rocketPart.getPayloadCarrierComponent().getMaxCarrySize() + "kg"));
            }
            if (rocketPart.getFuelComponent() != null) {
                tooltip.add(Text.of("§7Fuel: §2" + rocketPart.getFuelComponent().getCapactity() + "fU"));
                tooltip.add(Text.of("§7Fuel Type: §2" + rocketPart.getFuelComponent().getFuelType().name()));
            }
            if (rocketPart.getEngineComponent() != null) {
                tooltip.add(Text.of("§7Thrust: §2" + (rocketPart.getEngineComponent().getPower()) + "N"));
                tooltip.add(Text.of("§7Fuel Consumption: §2" + (rocketPart.getEngineComponent().getFuelConsumption()) + "fU/s"));
            }
        }
    }


    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof RocketPartBlockEntity) {
            ((RocketPartBlockEntity) blockEntity).setRocketPart(Objects.requireNonNull(stack.get(ComponentRegistry.ROCKET_PART_COMPONENT_TYPE)).clone());
        }

        return super.postPlacement(pos, world, player, stack, state);
    }
}
