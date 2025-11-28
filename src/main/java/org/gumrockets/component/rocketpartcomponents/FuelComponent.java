package org.gumrockets.component.rocketpartcomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class FuelComponent {
    private final float capacity;
    private final float fuelWeight;
    private final FuelType fuelType;

    public FuelComponent(float capacity, float fuelWeight, FuelType fuelType) {
        this.capacity = capacity;
        this.fuelWeight = fuelWeight;
        this.fuelType = fuelType;
    }

    public FuelComponent(float capacity, float fuelWeight,  String fuelType) {
        this.capacity = capacity;
        this.fuelWeight = fuelWeight;
        this.fuelType = FuelType.valueOf(fuelType);
    }

    public float getCapactity() {
        return this.capacity;
    }
    public float getFuelWeight() {
        return this.fuelWeight;
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
                    Codec.FLOAT.fieldOf("fuelWeight").forGetter(FuelComponent::getFuelWeight),
                    Codec.STRING.fieldOf("fuelType").forGetter(FuelComponent::getFuelTypeString)
            ).apply(builder, FuelComponent::new));
}
