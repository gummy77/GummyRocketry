package org.gumrockets.component.rocketpartcomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.particle.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.gumrockets.component.RocketPart;
import org.gumrockets.entity.RocketEntity;

public class EngineComponent {
    private final float power;
    private final float fuelConsumption;
    private final ParticleEffect exhaustParticle;

    public EngineComponent(float power, float fuelConsumption, ParticleEffect exhaustparticle) {
        this.power = power;
        this.fuelConsumption = fuelConsumption;
        this.exhaustParticle = exhaustparticle;
    }

    public float getPower() {
        return power;
    }

    public float getFuelConsumption() {
        return fuelConsumption;
    }

    public ParticleEffect getExhaustparticle() {
        return exhaustParticle;
    }

    public void tick(World world, RocketEntity rocket, RocketPart rocketPart, boolean isIgnition) {

        if (!isIgnition) {
            // add force to rocket
            rocket.addForce(this.power);
        }

        // render engine particles
        Vec3d particlePosition = rocket.getPos();

        for (int i = 0; i <= 1; i++) {
            Random random = Random.create();
            Vec3d particleVelocity = new Vec3d(random.nextDouble() - 0.5, 0, random.nextDouble() - 0.5);
            particleVelocity = particleVelocity.multiply(isIgnition ? 0.15 : 0.025);
            world.addImportantParticle(exhaustParticle, true,
                    particlePosition.x, particlePosition.y, particlePosition.z,
                    particleVelocity.x, particleVelocity.y, particleVelocity.z
            );
        }
    }

    public static final Codec<EngineComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("power").forGetter(EngineComponent::getPower),
                    Codec.FLOAT.fieldOf("fuelConsumption").forGetter(EngineComponent::getFuelConsumption),
                    ParticleTypes.TYPE_CODEC.fieldOf("exhaustParticle").forGetter(EngineComponent::getExhaustparticle)
            ).apply(builder, EngineComponent::new));
}
