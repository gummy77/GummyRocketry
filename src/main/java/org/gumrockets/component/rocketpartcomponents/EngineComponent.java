package org.gumrockets.component.rocketpartcomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.particle.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.gumrockets.component.RocketPart;
import org.gumrockets.entity.RocketEntity;

public class EngineComponent {
    private final float power;
    private final float fuelConsumption;
    private final ParticleEffect exhaustParticle;

    private SoundEvent ignitionSound;
    private SoundEvent burnSound;

    private boolean hasPlayedIgnitionSound = false;
    private int burnSoundTimer = 0;

    public EngineComponent(float power, float fuelConsumption, ParticleEffect exhaustparticle, SoundEvent ignitionSound, SoundEvent burnSound) {
        this.power = power;
        this.fuelConsumption = fuelConsumption;
        this.exhaustParticle = exhaustparticle;
        this.ignitionSound = ignitionSound;
        this.burnSound = burnSound;
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

    private SoundEvent getIgnitionSound() {
        return ignitionSound;
    }
    private SoundEvent getBurnSound() {
        return burnSound;
    }

    public void tick(World world, RocketEntity rocket, RocketPart rocketPart, boolean isIgnition) {

        if (!isIgnition) {
            // add force to rocket
            rocket.addForce(this.power);
        }

        // render engine particles
        Vec3d particlePosition = rocket.getPos();

        float particleIntensity = Math.min(1, this.power / 1000f);
        for (int i = 0; i <= particleIntensity; i++) {
            Random random = rocket.getRandom();
            Vec3d particleVelocity = new Vec3d(
                    (random.nextDouble() - 0.5) * particleIntensity * 1.5f,
                    isIgnition ? 0 : -random.nextDouble() * particleIntensity * 3,
                    (random.nextDouble() - 0.5) * particleIntensity * 1.5f
            );
            particleVelocity = particleVelocity.multiply(isIgnition ? 0.15 : 0.025);

            world.addImportantParticle(exhaustParticle, true,
                    particlePosition.x, particlePosition.y - (isIgnition ? 0 : random.nextDouble()/2), particlePosition.z,
                    particleVelocity.x, particleVelocity.y, particleVelocity.z
            );
        }

        if (isIgnition) {
            if (!hasPlayedIgnitionSound) {
                world.playSound(rocket, rocket.getBlockPos(), ignitionSound, SoundCategory.BLOCKS, 10, 1);
                hasPlayedIgnitionSound = true;
            }
        } else {
            if(burnSoundTimer <= 0) {
                world.playSound(rocket, rocket.getBlockPos(), burnSound, SoundCategory.BLOCKS, 10, 1);
                burnSoundTimer = 10; // 20 ticks a second, audio plays every 0.5 seconds.
            }
            burnSoundTimer -= 1;
        }
    }

    public static final Codec<EngineComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("power").forGetter(EngineComponent::getPower),
                    Codec.FLOAT.fieldOf("fuelConsumption").forGetter(EngineComponent::getFuelConsumption),
                    ParticleTypes.TYPE_CODEC.fieldOf("exhaustParticle").forGetter(EngineComponent::getExhaustparticle),
                    SoundEvent.CODEC.fieldOf("ignitionSound").forGetter(EngineComponent::getIgnitionSound),
                    SoundEvent.CODEC.fieldOf("burnSound").forGetter(EngineComponent::getBurnSound)
            ).apply(builder, EngineComponent::new));
}
