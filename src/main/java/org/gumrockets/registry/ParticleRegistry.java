package org.gumrockets.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.gumrockets.gumrocketsMain;

public class ParticleRegistry {
    public static final SimpleParticleType EXHAUST;

    public static void registerParticles() {
    }

    public static SimpleParticleType register (String path, ParticleType<?> particle) {
        return (SimpleParticleType) Registry.register(Registries.PARTICLE_TYPE, Identifier.of(gumrocketsMain.MOD_ID, path), particle);
    }

    static {
        EXHAUST = register("exhaust", FabricParticleTypes.simple());
    }
}
