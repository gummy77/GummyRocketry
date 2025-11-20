package org.gumrockets.item;

import com.google.common.base.Predicates;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.gumrockets.entity.RocketEntity;
import org.gumrockets.registry.EntityRegistry;

import java.util.List;
import java.util.function.Predicate;

public class LaunchKit extends Item {

    public LaunchKit(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            return this.startLaunch(user, world, hand);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    private TypedActionResult<ItemStack> startLaunch(PlayerEntity player, World world, Hand hand) {

        List<RocketEntity> entityList = world.getEntitiesByClass(RocketEntity.class, Box.of(player.getPos(), 25, 25, 25), Predicates.notNull());

        for (RocketEntity rocketEntity : entityList) {
            if (rocketEntity.getFuseHolder() == player) {

                rocketEntity.Launch();
            }
        }

        return TypedActionResult.fail(player.getStackInHand(hand));
    }
}
