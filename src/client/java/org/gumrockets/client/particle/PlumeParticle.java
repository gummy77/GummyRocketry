package org.gumrockets.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class PlumeParticle extends AbstractSlowingParticle {


    protected PlumeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.maxAge = 300;
        this.scale = 2;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getSize(float tickDelta) {
        float age = ((this.age) / (float) this.getMaxAge());
        float newScale = (float) Math.max(Math.min( (1f - Math.pow((age * 0.8f)-0.8f, 2f)) , 1f), 0f);
        float newAlpha = (float) Math.max(Math.min( (1f - Math.pow((age + 0.25f)    , 2f)) , 1f), 0f);

        this.setAlpha(newAlpha);
        return newScale * this.scale;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            PlumeParticle plumeParticle = new PlumeParticle(clientWorld, d, e, f, g, h, i);
            plumeParticle.setSprite(this.spriteProvider);
            return plumeParticle;
        }
    }
}
