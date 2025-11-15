package org.gumrockets.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class LaunchKit extends Item {

    public LaunchKit(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.startLaunch(user, world, hand);
    }

    private TypedActionResult<ItemStack> startLaunch(PlayerEntity player, World world, Hand hand) {
        return TypedActionResult.pass(player.getStackInHand(hand));
    }
}
