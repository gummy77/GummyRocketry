package org.gumrockets.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.List;

public class RocketStage {
    private ArrayList<RocketPart> parts;
    private float burnTimeRemaining;

    private float cachedWidth = 0;

    private float cachedMass = 0;

    public RocketStage() {
        this.parts = new ArrayList<>();
    }

    public RocketStage(List<RocketPart> rocketParts, float burnTimeRemaining) {
        this.parts = new ArrayList<>(rocketParts);
        this.burnTimeRemaining = burnTimeRemaining;
    }

    public void calculateBurnTime() {
        for (RocketPart rocketPart : parts) {
            if (rocketPart.getFuelComponent() != null) {
                this.burnTimeRemaining += ((rocketPart.getFuelComponent().getCapactity() * rocketPart.getFuelComponent().getFillLevel()) * rocketPart.getFuelComponent().getBurnSpeed());
            }
        }
    }

    public void setBurnTimeRemaining(float burnTimeRemaining) { this.burnTimeRemaining = burnTimeRemaining; }
    public float getBurnTimeRemaining() { return this.burnTimeRemaining; }

    public void addPart(RocketPart part) {
        this.parts.add(part);
    }

    public ArrayList<RocketPart> getParts(){
        return this.parts;
    }

    public int getHeight() {
        int height = 0;
        for (RocketPart rocketPart : parts) {
            height += 1;
        }
        return height;
    }

    public float getWidth() {
        if (this.cachedWidth != 0) return this.cachedWidth;
        float maxWidth = 0;
            for (RocketPart part : this.getParts()) {
                if(part.getWidth() > maxWidth) maxWidth = part.getWidth();
        }
        this.cachedWidth = maxWidth / 16f;
        return this.cachedWidth;
    }
    public float getMass() {
        if (this.cachedMass != 0) return this.cachedMass;
        float mass = 0;
        for (RocketPart part : this.getParts()) {
            mass += part.getMass();
        }
        this.cachedMass = mass;
        return this.cachedMass;
    }

    public static Codec<RocketStage> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codecs.nonEmptyList(RocketPart.CODEC.listOf()).fieldOf("parts").forGetter(RocketStage::getParts),
                    Codec.FLOAT.fieldOf("burnTimeRemaining").forGetter(RocketStage::getBurnTimeRemaining)
            ).apply(builder, RocketStage::new));
}
