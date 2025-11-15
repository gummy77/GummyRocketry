package org.gumrockets.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.List;

public class Rocket {
    private ArrayList<RocketStage> stages;
    private RocketState state;

    private float cachedWidth = 0;
    private float cachedHeight = 0;
    private float cachedMass = 0;

    public Rocket(ArrayList<RocketStage> stages) {
        this.stages = stages;
        this.state = new RocketState(RocketState.LaunchState.IDLE);
        this.cachedWidth = 0;
    }
    public Rocket(List<RocketStage> stages, RocketState state) {
        this.stages = new ArrayList<>(stages);
        this.state = state;
    }

    public void createNewState() {
        if (this.state == null) {
            this.state = new RocketState(RocketState.LaunchState.IDLE);
        }
    }
    public RocketState getState() {
        return this.state;
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

    public float getMass() {
        if (this.cachedMass != 0) return this.cachedMass;

        int mass = 0;
        int stageCounter = Math.min(this.state.getCurrentStage(), this.stages.size()-1);
        for (int i = stageCounter; i < this.stages.size(); i++) {
            RocketStage stage = this.stages.get(i);
            mass += stage.getMass();
        }
        this.cachedMass = mass;
        return mass;
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
                    RocketState.CODEC.fieldOf("state").forGetter(Rocket::getState)
            ).apply(builder, Rocket::new));
}
