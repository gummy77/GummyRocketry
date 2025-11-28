package org.gumrockets.component.rocketpartcomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.gumrockets.component.RocketPart;
import org.gumrockets.entity.RocketEntity;

public class PayloadCarrierComponent {
    private final float maxCarrySize;

    public PayloadCarrierComponent(float maxCarrySize) {
        this.maxCarrySize = maxCarrySize;
    }

    public float getMaxCarrySize() {
        return maxCarrySize;
    }

    public static final Codec<PayloadCarrierComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("maxCarrySize").forGetter(PayloadCarrierComponent::getMaxCarrySize)
            ).apply(builder, PayloadCarrierComponent::new));
}
