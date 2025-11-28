package org.gumrockets.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Rocket {
    private final ArrayList<RocketStage> stages;
    private RocketState state;

    private PayloadTypes payloadType;
    private Vec3d launchPosition;

    private int fuseHolderID = 0;
    private float cachedWidth = 0;

    public Rocket(ArrayList<RocketStage> stages, Vec3d launchPosition) {
        this.stages = stages;
        this.state = new RocketState(RocketState.LaunchState.IDLE);
        this.cachedWidth = 0;
        this.launchPosition = launchPosition;
        this.payloadType = PayloadTypes.NONE;
    }
    public Rocket(List<RocketStage> stages, RocketState state, int fuseHolderID, Vec3d launchPosition, String payloadTypeString) {
        this.stages = new ArrayList<>(stages);
        this.state = state;
        this.fuseHolderID = fuseHolderID;
        this.launchPosition = launchPosition;
        this.payloadType = PayloadTypes.valueOf(payloadTypeString);
    }

    public void createNewState() {
        if (this.state == null) {
            this.state = new RocketState(RocketState.LaunchState.IDLE);
        }
    }
    public RocketState getState() {
        return this.state;
    }

    public Vec3d getLaunchPosition() {
        return launchPosition;
    }
    public void setLaunchPosition(Vec3d launchPosition) {
        this.launchPosition = launchPosition;
    }

    public float getWidth() {
        if (this.cachedWidth != 0) return this.cachedWidth;
        float maxWidth = 0;
        for (RocketStage stage : stages) {
            for (RocketPart part : stage.getParts()) {
                if(part.getWidth() > maxWidth) maxWidth = part.getWidth();
            }
        }
        this.cachedWidth = maxWidth / 16f;
        return this.cachedWidth;
    }
    public float getHeight() {
        //if (this.cachedHeight != 0) return this.cachedHeight;

        int height = 0;
        int stageCounter = Math.min(this.state.getCurrentStage(), this.stages.size()-1);
        for (int i = stageCounter; i < this.stages.size(); i++) {
            RocketStage stage = this.stages.get(i);
            height += stage.getHeight();
        }
        //this.cachedHeight = height;
        return height;
    }

    public float getMaxPayloadWeight() {
        float maxWeight = 0;
        for (RocketStage stage : stages) {
            for (RocketPart part : stage.getParts()) {
                if(part.getPayloadCarrierComponent() != null) {
                    maxWeight += part.getPayloadCarrierComponent().getMaxCarrySize();
                }
            }
        }
        return maxWeight;
    }

    public float getMass() {
        return getMassAtStage(this.state.getCurrentStage());
    }

    public float getMassAtStage(int stageIndex) {
        float mass = 0;
        int stageCounter = Math.min(stageIndex, this.stages.size()-1);
        for (int i = stageCounter; i < this.stages.size(); i++) {
            RocketStage stage = this.stages.get(i);
            mass += stage.getCurrentMass();
        }
        return mass;
    }
    public float getFuelMassAtStage(int stageIndex) {
        float mass = 0;
        int stageCounter = Math.min(stageIndex, this.stages.size());
        for (int i = stageCounter; i < this.stages.size(); i++) {
            RocketStage stage = this.stages.get(i);
            mass += stage.getFuelMass();
        }
        return mass;
    }
    public float getEmptyMassAtStage(int stageIndex) {
        float mass = 0;
        int stageCounter = Math.min(stageIndex, this.stages.size()-1);
        for (int i = stageCounter; i < this.stages.size(); i++) {
            RocketStage stage = this.stages.get(i);
            mass += stage.getPartMass();
        }
        return mass;
    }

    public float getTWR() {
        float mass = getMass();
        float thrust = this.getCurrentStage().getThrust();

        return thrust / mass;
    }

    public float getTWRAtStage(int stageIndex) {
        float mass = getMassAtStage(stageIndex);
        float thrust = this.getStages().get(stageIndex).getThrust();

        return thrust / mass;
    }

    public float getDeltaV() {
        float totalDeltaV = 0;
        for (int stageIndex = 0; stageIndex < this.stages.size(); stageIndex++) {
            totalDeltaV += getDeltaVAtStage(stageIndex);
        }
        return totalDeltaV;
    }

    public float getDeltaVAtStage(int stageIndex) {
        float thrust = this.getStages().get(stageIndex).getThrust();
        float fuelMass = this.getStages().get(stageIndex).getFuelMass();
        float rocketMass = getEmptyMassAtStage(stageIndex) + getFuelMassAtStage(stageIndex + 1);
        float burnTime = this.getStages().get(stageIndex).getBurnTime();

        float a = calculateFlightIntegral(thrust, burnTime, fuelMass, rocketMass, 0);
        float b = calculateFlightIntegral(thrust, burnTime, fuelMass, rocketMass, burnTime);

        return b - a;
    }
    float calculateFlightIntegral(float thrust, float burnTime, float fuelMass, float rocketMass, float t) {
        return (float) (thrust * burnTime * Math.log(Math.abs((fuelMass * t) + (burnTime * rocketMass)))) / fuelMass;
    }
    public static float calculateHeightFromDeltaV(float deltaV) {
        return (float) (0.2 * Math.pow(deltaV, 1.8)) + deltaV;
    }

    public void setPayloadType(PayloadTypes payloadType) {
        this.payloadType = payloadType;
    }
    public PayloadTypes getPayloadType() {
        return this.payloadType;
    }
    public String getPayloadTypeString() {
        return this.payloadType.toString();
    }

    public int getFuseHolderID() {
        return this.fuseHolderID;
    }

    public void setFuseHolderID(int fuseHolderID) {
        this.fuseHolderID = fuseHolderID;
    }

    public RocketStage getCurrentStage() {
        if(this.state.getCurrentStage() > this.stages.size()-1) return null;
        return this.stages.get(this.state.getCurrentStage());
    }

    public ArrayList<RocketStage> getStages() {
        return this.stages;
    }

    public static Codec<Rocket> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codecs.nonEmptyList(RocketStage.CODEC.listOf()).fieldOf("stages").forGetter(Rocket::getStages),
                    RocketState.CODEC.fieldOf("state").forGetter(Rocket::getState),
                    Codec.INT.fieldOf("fuseHolderID").forGetter(Rocket::getFuseHolderID),
                    Vec3d.CODEC.fieldOf("launchPosition").forGetter(Rocket::getLaunchPosition),
                    Codec.STRING.fieldOf("payloadType").forGetter(Rocket::getPayloadTypeString)
            ).apply(builder, Rocket::new));
}
