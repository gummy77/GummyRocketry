package org.gumrockets.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import org.joml.Quaternionf;

public class RocketState {
    private LaunchState launchState;
    private float launchTimer;
    private int currentStage;
    private Quaternionf rotation;

    public RocketState(LaunchState launchState) {
        this.launchState = launchState;
        this.launchTimer = 2f;
        this.currentStage = 0;
        this.rotation = new Quaternionf();
    }

    public RocketState(String launchState, float launchTimer, int currentStage, Quaternionf rotation) {
        this.launchState = LaunchState.valueOf(launchState);
        this.launchTimer = launchTimer;
        this.currentStage = currentStage;
        this.rotation = rotation;
    }

    public void setLaunchTimer(float timer) { this.launchTimer = Math.max(timer, 0); }
    public float getLaunchTimer() { return this.launchTimer; }

    public void setLaunchState(LaunchState launchState) { this.launchState = launchState; }
    public String getLaunchStateString() { return launchState.toString(); }
    public LaunchState getLaunchState() { return launchState; }

    public void setRotation(Quaternionf rotation) { this.rotation = rotation; }
    public Quaternionf getRotation() { return rotation; }

    public void stage() {
        this.currentStage++;
    }
    public int getCurrentStage() {
        return this.currentStage;
    }

    public enum LaunchState {
        IDLE,
        IGNITION,
        LAUNCHING,
        COASTING
    }

    public static Codec<RocketState> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codecs.NON_EMPTY_STRING.fieldOf("launchState").forGetter(RocketState::getLaunchStateString),
                    Codec.FLOAT.fieldOf("launchTimer").forGetter(RocketState::getLaunchTimer),
                    Codec.INT.fieldOf("currentStage").forGetter(RocketState::getCurrentStage),
                    Codecs.QUATERNIONF.fieldOf("rotation").forGetter(RocketState::getRotation)
            ).apply(builder, RocketState::new));
}
