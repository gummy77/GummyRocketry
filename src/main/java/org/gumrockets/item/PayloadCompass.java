package org.gumrockets.item;

import com.mojang.logging.LogUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.gumrockets.component.PayloadCompassComponent;
import org.gumrockets.registry.ComponentRegistry;
import org.slf4j.Logger;

import java.util.Optional;

public class PayloadCompass extends Item {

    private static final Logger LOGGER = LogUtils.getLogger();

    public PayloadCompass(Settings settings) {
        super(settings);
    }

    private static Optional<RegistryKey<World>> getPayloadDimension(NbtCompound nbt) {
        return World.CODEC.parse(NbtOps.INSTANCE, nbt.get("PayloadDimension")).result();
    }

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            PayloadCompassComponent payloadCompassComponent = stack.get(ComponentRegistry.PAYLOAD_COMPASS_COMPONENT_COMPONENT_TYPE);
            if (payloadCompassComponent != null) {

                GlobalPos payloadPos = payloadCompassComponent.getPayloadPosition();
                if (payloadPos != null && payloadPos.dimension() == world.getRegistryKey()) {
                    BlockPos blockPos = payloadPos.pos();
                    if (!world.isInBuildLimit(blockPos)) {
                        stack.remove(ComponentRegistry.PAYLOAD_COMPASS_COMPONENT_COMPONENT_TYPE);
                    }
                }

                int rocketID = payloadCompassComponent.getRocketID();
                if(rocketID != 0) {
                    Entity entity2 = world.getEntityById(rocketID);
                    if(entity2 == null || !entity2.isAlive() || entity2.isRemoved()) {
                        payloadCompassComponent.setRocketID(0);
                    }
                }
            }
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return super.useOnEntity(stack, user, entity, hand);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
    }
}