package org.gumrockets.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import org.gumrockets.component.rocketpartcomponents.EngineComponent;

import java.util.ArrayList;
import java.util.List;

public class RocketStage {
    private final ArrayList<RocketPart> parts;
    private float burnTime;
    private float burnTimeRemaining;

    private float cachedMass = 0;
    private float cachedFuelMass = 0;
    private float cachedWidth = 0;
    private float cachedThrust = 0;

    public RocketStage() {
        this.parts = new ArrayList<>();
    }

    public RocketStage(List<RocketPart> rocketParts, float burnTime, float burnTimeRemaining) {
        this.parts = new ArrayList<>(rocketParts);
        this.burnTime = burnTime;
        this.burnTimeRemaining = burnTimeRemaining;
    }

    public void calculateBurnTime() {
        float totalBurnTime = 0f;
        for (RocketPart rocketPart : parts) {
            if (rocketPart.getFuelComponent() != null) {
                totalBurnTime += rocketPart.getFuelComponent().getCapactity();
            }
        }
        for (RocketPart rocketPart : parts) {
            if (rocketPart.getEngineComponent() != null) {
                totalBurnTime /= rocketPart.getEngineComponent().getFuelConsumption();
            }
        }
        this.burnTime = totalBurnTime;
        this.burnTimeRemaining = totalBurnTime;
    }

    public void setBurnTime(float burnTime) {
        this.burnTime = burnTime;
    }

    public float getBurnTime() {
        return burnTime;
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
            height += 1; // TODO fix this
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
    public float getCurrentMass() {
        float mass = getPartMass();
        float fuelMass = 0;
        for (RocketPart part : this.getParts()) {
            if(part.getFuelComponent() != null) {
                fuelMass += part.getFuelComponent().getFuelWeight();
            }
        }
        float fuelLeftRatio = burnTimeRemaining / burnTime;
        return mass + (fuelMass * fuelLeftRatio);
    }
    public float getPartMass() {
        if (this.cachedMass != 0) return this.cachedMass;
        float partMass = 0;
        for (RocketPart part : this.getParts()) {
            partMass += part.getPartMass();
        }
        this.cachedMass = partMass;
        return this.cachedMass;
    }
    public float getFuelMass() {
        if (this.cachedFuelMass != 0) return this.cachedFuelMass;
        float fuelMass = 0;
        for (RocketPart part : this.getParts()) {
            if(part.getFuelComponent() != null) {
                fuelMass += part.getFuelComponent().getFuelWeight();
            }
        }
        this.cachedFuelMass = fuelMass;
        return this.cachedFuelMass;
    }

    public float getThrust() {
        if (this.cachedThrust != 0) return this.cachedThrust;
        float thrust = 0;
        for (RocketPart part : this.getParts()) {
            EngineComponent engineComponent = part.getEngineComponent();
            if(engineComponent != null) {
                thrust += part.getEngineComponent().getPower();
            }
        }
        this.cachedThrust = thrust;
        return this.cachedThrust;
    }

    public static Codec<RocketStage> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codecs.nonEmptyList(RocketPart.CODEC.listOf()).fieldOf("parts").forGetter(RocketStage::getParts),
                    Codec.FLOAT.fieldOf("burnTime").forGetter(RocketStage::getBurnTime),
                    Codec.FLOAT.fieldOf("burnTimeRemaining").forGetter(RocketStage::getBurnTimeRemaining)
            ).apply(builder, RocketStage::new));
}
