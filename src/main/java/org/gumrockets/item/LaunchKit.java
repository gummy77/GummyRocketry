package org.gumrockets.item;

import com.google.common.base.Predicates;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.gumrockets.entity.RocketEntity;
import org.gumrockets.registry.EntityRegistry;
import org.gumrockets.registry.SoundRegistry;

import java.util.List;
import java.util.function.Predicate;

public class LaunchKit extends Item {

    public enum LaunchKitLevel {
        BASIC,
        ADVANCED
    }

    private LaunchKitLevel level;

    public LaunchKit(Settings settings, LaunchKitLevel _level) {
        super(settings.maxCount(1));
        level = _level;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            return this.startLaunch(user, world, hand);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return ActionResult.PASS;
    }

    private TypedActionResult<ItemStack> startLaunch(PlayerEntity player, World world, Hand hand) {

        List<RocketEntity> entityList = world.getEntitiesByClass(RocketEntity.class, Box.of(player.getPos(), 25, 25, 25), Predicates.notNull());

        for (RocketEntity rocketEntity : entityList) {
            if (rocketEntity.getFuseHolder() == player && !rocketEntity.wasJustAttached()) {

                rocketEntity.Launch();

                switch (level){
                    case BASIC:
                        world.playSound(rocketEntity, rocketEntity.getBlockPos(), SoundRegistry.ENTITY_FUSE_BURN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        break;
                    case ADVANCED:

                        break;
                }

                return TypedActionResult.success(player.getStackInHand(hand), true);
            }
        }

        return TypedActionResult.fail(player.getStackInHand(hand));
    }
}
