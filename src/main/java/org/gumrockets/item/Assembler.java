package org.gumrockets.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import org.gumrockets.component.RocketPart;
import org.gumrockets.entity.RocketPartBlockEntity;

public class Assembler extends Item {
    private final RocketPart.PartMaterial maxMaterial;

    public Assembler(Settings settings, RocketPart.PartMaterial maxMaterial) {
        super(settings);
        this.maxMaterial = maxMaterial;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(context.getWorld().isClient) {
            return super.useOnBlock(context);
        }

        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());

        if(blockEntity instanceof RocketPartBlockEntity rocketPartBlockEntity) {
            rocketPartBlockEntity.startAssembly(maxMaterial, context.getBlockPos());
            context.getWorld().playSound(context.getPlayer(), blockEntity.getPos(), SoundEvents.BLOCK_HEAVY_CORE_PLACE, SoundCategory.BLOCKS);

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }
}
