package org.gumrockets.component.rocketpartcomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class FuelComponent {
    private final float capacity;
    private final float fillLevel;
    private final float burnPower;
    private final float burnSpeed;
    private final FuelType fuelType;

    public FuelComponent(float capacity, float fillLevel, float burnPower, float burnSpeed, FuelType fuelType) {
        this.capacity = capacity;
        this.fillLevel = fillLevel;
        this.burnPower = burnPower;
        this.burnSpeed = burnSpeed;
        this.fuelType = fuelType;
    }

    public FuelComponent(float capacity, float fillLevel, float burnPower, float burnSpeed, String fuelType) {
        this.capacity = capacity;
        this.fillLevel = fillLevel;
        this.burnPower = burnPower;
        this.burnSpeed = burnSpeed;
        this.fuelType = FuelType.valueOf(fuelType);
    }

    public float getCapactity() {
        return this.capacity;
    }
    public float getFillLevel() {
        return this.fillLevel;
    }
    public float getBurnPower() {
        return this.burnPower;
    }
    public float getBurnSpeed() {
        return this.burnSpeed;
    }
    public String getFuelTypeString() {
        return this.fuelType.toString();
    }
    public FuelType getFuelType() {
        return this.fuelType;
    }

    public enum FuelType {
        SOLID,
        KEROSENE,
        OXYGEN
    }

    public static final Codec<FuelComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("capacity").forGetter(FuelComponent::getCapactity),
                    Codec.FLOAT.fieldOf("fillLevel").forGetter(FuelComponent::getFillLevel),
                    Codec.FLOAT.fieldOf("burnPower").forGetter(FuelComponent::getBurnPower),
                    Codec.FLOAT.fieldOf("burnSpeed").forGetter(FuelComponent::getBurnSpeed),
                    Codec.STRING.fieldOf("fuelType").forGetter(FuelComponent::getFuelTypeString)
            ).apply(builder, FuelComponent::new));
}
