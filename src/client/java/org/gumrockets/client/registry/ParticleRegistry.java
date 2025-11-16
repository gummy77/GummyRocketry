package org.gumrockets.client.registry;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import org.gumrockets.client.particle.PlumeParticle;

public class ParticleRegistry {


    public static void initializeRegistry() {
        registerParticle(org.gumrockets.registry.ParticleRegistry.EXHAUST, PlumeParticle.Factory::new);
    }

    private static <T extends ParticleEffect> void registerParticle(ParticleType<T> particle, ParticleFactoryRegistry.PendingParticleFactory<T> factory) {
        ParticleFactoryRegistry.getInstance().register(particle, factory);
    }
}