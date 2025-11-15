package org.gumrockets.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Vec3d;
import org.gumrockets.component.rocketpartcomponents.EngineComponent;
import org.gumrockets.component.rocketpartcomponents.FuelComponent;

import java.util.Optional;

public class RocketPart implements Cloneable{
    private final BlockState block;
    private final PartMaterial material;
    private final PartType type;
    private final int width; // 1/16 of a block

    private float mass;

    private EngineComponent engineComponent;
    private FuelComponent fuelComponent;

    private Vec3d offset;

    private RocketPart(BlockState block, String material, String partType, int width, float mass, EngineComponent engineComponent, FuelComponent fuelComponent, Vec3d offset) {
        this.block = block;
        this.material = PartMaterial.valueOf(material);
        this.type = PartType.valueOf(partType);
        this.width = width;
        this.mass = mass;

        this.engineComponent = engineComponent;
        this.fuelComponent = fuelComponent;
        this.offset = offset;
    }

    private RocketPart(BlockState block, String material, String partType, int width, float mass, Optional<EngineComponent> engineComponent, Optional<FuelComponent> fuelComponent, Vec3d offset) {
        this.block = block;
        this.material = PartMaterial.valueOf(material);
        this.type = PartType.valueOf(partType);
        this.width = width;
        this.mass = mass;

        engineComponent.ifPresent(component -> this.engineComponent = component);
        fuelComponent.ifPresent(component -> this.fuelComponent = component);

        this.offset = offset;
    }

    private RocketPart(RocketPartBuilder builder) {
        this.block = builder.block;
        this.material = builder.partMaterial;
        this.type = builder.partType;
        this.width = builder.width;
        this.mass = builder.mass;

        this.engineComponent = builder.engineComponent;
        this.fuelComponent = builder.fuelComponent;

        this.offset = builder.offset;
    }


    public BlockState getBlock() {
        return this.block;
    }
    public String getMaterialString() { return this.material.toString(); }
    public PartMaterial getMaterial() { return this.material; }
    public String getTypeString() { return this.type.toString(); }
    public PartType getType() { return this.type; }
    public int getWidth() { return this.width; }

    public float getMass() { return this.mass; }
    public void setMass(float mass) { this.mass = mass; }

    public EngineComponent getEngineComponent() { return this.engineComponent; }
    public Optional<EngineComponent> getEngineOptional() { return this.engineComponent != null ? Optional.of(this.engineComponent) : Optional.empty(); }
    public FuelComponent getFuelComponent() { return this.fuelComponent; }
    public Optional<FuelComponent> getFuelOptional() {return this.fuelComponent != null ? Optional.of(this.fuelComponent) : Optional.empty(); }

    public Vec3d getOffset() {
        return this.offset;
    }
    public RocketPart setOffset(Vec3d offset) {
        this.offset = offset;
        return this;
    }

    public static class RocketPartBuilder {
        private final BlockState block;

        private final PartMaterial partMaterial;
        private final PartType partType;
        private final int width;

        private final float mass;

        private EngineComponent engineComponent = null;
        private FuelComponent fuelComponent = null;

        private Vec3d offset = new Vec3d(0,0,0);

        public RocketPartBuilder(BlockState block, PartMaterial partMaterial, PartType partType, int width, int mass) {
            this.block = block;
            this.partMaterial = partMaterial;
            this.partType = partType;
            this.width = width;
            this.mass = mass;
        }

        public RocketPartBuilder addEngineComponent(EngineComponent engineComponent) {
            this.engineComponent = engineComponent;
            return this;
        }

        public RocketPartBuilder addFuelComponent(FuelComponent fuelComponent) {
            this.fuelComponent = fuelComponent;
            return this;
        }

        public RocketPart build() {
            return new RocketPart(this);
        }
    }

    public enum PartType {
        NOSE,
        FUEL,
        COUPLER,
        ENGINE,
        PAYLOAD;
    }

    public enum PartMaterial {
        BAMBOO(),
        WOOD(),
        COPPER(),
        IRON();
    }

    @Override
    public RocketPart clone() {
        return new RocketPart(this.getBlock(), this.getMaterialString(), this.getTypeString(), this.getWidth(), this.getMass(), this.getEngineComponent(), this.getFuelComponent(), this.getOffset());
    }

    public static final Codec<RocketPart> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    BlockState.CODEC.fieldOf("block").forGetter(RocketPart::getBlock),
                    Codec.STRING.fieldOf("material").forGetter(RocketPart::getMaterialString),
                    Codec.STRING.fieldOf("partType").forGetter(RocketPart::getTypeString),
                    Codec.INT.fieldOf("width").forGetter(RocketPart::getWidth),
                    Codec.FLOAT.fieldOf("mass").forGetter(RocketPart::getMass),
                    EngineComponent.CODEC.optionalFieldOf("engineComponent").forGetter(RocketPart::getEngineOptional),
                    FuelComponent.CODEC.optionalFieldOf("fuelComponent").forGetter(RocketPart::getFuelOptional),
                    Vec3d.CODEC.fieldOf("offset").forGetter(RocketPart::getOffset)

            ).apply(builder, RocketPart::new));
}
