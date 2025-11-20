package org.gumrockets.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.gumrockets.registry.ComponentRegistry;

public class PayloadCompassComponent {

    private GlobalPos payloadPosition;
    private int rocketID;

    public PayloadCompassComponent(GlobalPos pos, int _rocketID) {
        payloadPosition = pos;
        rocketID = _rocketID;
    }

    public static GlobalPos getCompassPosition(ItemStack stack, World world) {
        PayloadCompassComponent payloadCompassComponent = stack.get(ComponentRegistry.PAYLOAD_COMPASS_COMPONENT_COMPONENT_TYPE);
        if(payloadCompassComponent != null) {
            if (payloadCompassComponent.payloadPosition != null) {
                if (payloadCompassComponent.getRocketID() != 0) {
                    Entity entity2 = world.getEntityById(payloadCompassComponent.getRocketID());
                    if (entity2 != null && entity2.isAlive() && !entity2.isRemoved()) {
                        return GlobalPos.create(world.getRegistryKey(), entity2.getBlockPos());
                    }
                }
                return payloadCompassComponent.getPayloadPosition();
            }
        }
        return null;
    }

    public void setPayloadPosition(GlobalPos pos) {
        payloadPosition = pos;
    }
    public GlobalPos getPayloadPosition() {
        return payloadPosition;
    }

    public void setRocketID(int _rocketID) {
        rocketID = _rocketID;
    }
    public int getRocketID () {
        return rocketID;
    }

    public static final Codec<PayloadCompassComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    GlobalPos.CODEC.fieldOf("pos").forGetter(PayloadCompassComponent::getPayloadPosition),
                    Codec.INT.fieldOf("rocketID").forGetter(PayloadCompassComponent::getRocketID)
            ).apply(builder, PayloadCompassComponent::new));
}
