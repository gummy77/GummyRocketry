package org.gumrockets.client.renderer;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.gumrockets.component.*;
import org.gumrockets.gumRocketsMain;
import org.gumrockets.entity.RocketEntity;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;

public class RocketEntityRenderer extends EntityRenderer<RocketEntity> {

    private final BlockRenderManager blockRenderManager;
    private final TextRenderer textRenderer;

    public RocketEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        blockRenderManager = context.getBlockRenderManager();
        textRenderer = context.getTextRenderer();

        this.shadowRadius = 0.5f;
        this.shadowOpacity = 0.5f;
    }

    @Override
    public boolean shouldRender(RocketEntity entity, Frustum frustum, double x, double y, double z) {
        return true; // TODO make this frustum culled with no distance limit?
    }

    @Override
    public void render(RocketEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        if(entity.getRocket() != null) {
            matrices.push();
            matrices.multiply(entity.getRocket().getState().getRotation());
            matrices.translate(-0.5f, 0f, -0.5f);

            ArrayList<RocketStage> stages = entity.getRocket().getStages();
            int stageCounter = Math.min(entity.getRocket().getState().getCurrentStage(), stages.size()-1);

            for (int i = stageCounter; i < stages.size(); i++) {
                RocketStage stage = stages.get(i);
                ArrayList<RocketPart> parts = stage.getParts();
                for (RocketPart part : parts) {
                    matrices.push();
                    matrices.translate(part.getOffset().getX(), part.getOffset().getY(), part.getOffset().getZ());
                    blockRenderManager.renderBlockAsEntity(part.getBlock(), matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
                    matrices.pop();
                }
                matrices.translate(0, stage.getHeight(), 0);
            }
            matrices.pop();

            if((entity.IsPlayerWatching() || entity.statsVisibility > 0) && entity.getRocket().getState().getLaunchState() == RocketState.LaunchState.IDLE) {
                renderStatsText(entity, matrices, vertexConsumers, light, entity.statsVisibility);
            }
            if(entity.IsPlayerWatching()) {
                entity.statsVisibility += 0.1f;
            } else {
                entity.statsVisibility -= 0.1f;
            }

            entity.statsVisibility = MathHelper.clamp(entity.statsVisibility, 0, 1);

            if (entity.getFuseHolder() != null) {
                this.renderFuse(entity, tickDelta, matrices, vertexConsumers, entity.getFuseHolder());
            }
        }
    }

    private void renderStatsText(RocketEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float visibility) {
        matrices.push();

        Rocket rocketData = entity.getRocket();

        int textColour = Colors.BLACK;

        // Stage Info
        matrices.push();
        for (int stageIndex = 0; stageIndex < rocketData.getStages().size(); stageIndex++) {
            RocketStage stage = rocketData.getStages().get(stageIndex);
            matrices.translate(0f, stage.getHeight(), 0f);
            matrices.push();

            matrices.multiply(dispatcher.camera.getRotation());
            matrices.translate(rocketData.getWidth() + 0.25f, 0f, 0f);
            matrices.scale(0.025f, -0.025f, 0.025f);
            matrices.scale(visibility, visibility, visibility);

            matrices.push();
            matrices.translate(20, 0, -1);
            matrices.scale(25f, 7f, 10f);
            textRenderer.draw(" ", 0, 0, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Colors.ALTERNATE_WHITE, light);
            matrices.pop();

            matrices.push();
            matrices.scale(3f, 2f, 1f);
            matrices.translate(-5, 21f, -0.5f);
            textRenderer.draw("/", 0, 0, Colors.ALTERNATE_WHITE, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
            matrices.pop();

            textRenderer.draw("Stage " + stageIndex, 0f, 0f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
            matrices.scale(0.8f, 0.8f, 0.8f);

            textRenderer.draw("Stage Mass: " + stage.getCurrentMass() + "kg", 0f, 15f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
            textRenderer.draw("Rocket Mass at Stage: " + rocketData.getMassAtStage(stageIndex) + "kg", 0f, 25f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
            textRenderer.draw("Stage Thrust: " + stage.getThrust() + "N", 0f, 35f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
            textRenderer.draw("Burn Time " + stage.getBurnTimeRemaining() + "s", 0f, 45f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
            float TWR = (float) Math.round(rocketData.getTWRAtStage(stageIndex) * 100) / 100;
            textRenderer.draw("Stage TWR: " + TWR, 0f, 55f, (TWR > 1 ? textColour : Colors.RED), false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
            float deltaV = (float) Math.round(rocketData.getDeltaVAtStage(stageIndex) * 100) / 100;
            textRenderer.draw("Stage Delta V: " + deltaV + "m/s", 0f, 65f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);

            matrices.pop();
        }
        matrices.pop();

        // Rocket Info
        matrices.translate(0f,  (rocketData.getHeight() / 2) + 1f, 0f);
        matrices.push();

        matrices.multiply(dispatcher.camera.getRotation());
        matrices.translate(-rocketData.getWidth() - 0.35f, 0f, 0f);

        matrices.scale(visibility, visibility, visibility);

        matrices.scale(0.03f, -0.03f, 0.03f);



        matrices.push();
        matrices.scale(3f, 2f, 1f);
        matrices.translate(0, 26f, -0.5f);
        textRenderer.draw("\\", 0, 0, Colors.ALTERNATE_WHITE, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
        matrices.pop();

        matrices.push();
        float textPosition = 0;

        String text = "Rocket Stats";
        textRenderer.draw(text, text.length() * -5, textPosition, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
        matrices.scale(0.8f, 0.8f, 0.8f);
        text = "Stages: " + rocketData.getStages().size();
        textRenderer.draw(text, text.length() * -5, textPosition += 15f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
        text = "Total Mass: " + rocketData.getMass() + "kg";
        textRenderer.draw(text, text.length() * -5, textPosition += 10f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);

        float deltaV = ((float) Math.round(rocketData.getDeltaV() * 100f) / 100f);
        text = "Total Delta V: " + deltaV + "m/s";
        textRenderer.draw(text, text.length() * -5, textPosition += 10f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);

        float height = Rocket.calculateHeightFromDeltaV(deltaV);
        if(height < 100000) {
            text = "Est. Apogee: " + ((float) Math.round(height / 100f) / 10) + "km";
            textRenderer.draw(text, text.length() * -5, textPosition += 10f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
        } else {
            text = "Reaches Space";
            textRenderer.draw(text, text.length() * -5.5f, textPosition += 10f, Colors.GREEN, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);

            boolean orbital = false;
            text = "Orbital: " + (orbital ? "" : " "); // TODO when physics math stuff sorted out.
            textRenderer.draw(text, text.length() * -5, textPosition += 10f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
            textRenderer.draw((orbital ? "yes" : "no"), -10, textPosition, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
        }
        boolean payload = rocketData.getPayloadType() != PayloadTypes.NONE;
        text = "Payload:  " + (payload ? "  " : " ");
        textRenderer.draw(text, text.length() * -5, textPosition += 10f, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
        textRenderer.draw((payload ? "yes" : "no"), (payload ? -15 : -10), textPosition, (payload ? Colors.GREEN : textColour), false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Color.TRANSLUCENT, light);
        matrices.pop();

        matrices.push();
        matrices.translate(-85, 0, -1);
        matrices.scale(18f, (textPosition / 10) + 1, 10f);
        textRenderer.draw(" ", 0, 0, textColour, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Colors.ALTERNATE_WHITE, light);
        matrices.pop();

        matrices.pop();

        matrices.pop();
    }

    private <E extends Entity> void renderFuse(RocketEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, E holdingEntity) {
        matrices.push();
        Vec3d vec3d = holdingEntity.getLeashPos(tickDelta);
        double d = (double)(entity.lerpYaw(tickDelta) * 0.017453292F) + 1.5707963267948966;
        Vec3d vec3d2 = entity.getLeashOffset(tickDelta);
        double e = Math.cos(d) * vec3d2.z + Math.sin(d) * vec3d2.x;
        double f = Math.sin(d) * vec3d2.z - Math.cos(d) * vec3d2.x;
        double g = MathHelper.lerp((double)tickDelta, entity.prevX, entity.getX()) + e;
        double h = MathHelper.lerp((double)tickDelta, entity.prevY, entity.getY()) + vec3d2.y;
        double i = MathHelper.lerp((double)tickDelta, entity.prevZ, entity.getZ()) + f;
        matrices.translate(e, vec3d2.y, f);
        float j = (float)(vec3d.x - g);
        float k = (float)(vec3d.y - h);
        float l = (float)(vec3d.z - i);
        float m = 0.025F;
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLeash());
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float n = MathHelper.inverseSqrt(j * j + l * l) * 0.025F / 2.0F;
        float o = l * n;
        float p = j * n;
        BlockPos blockPos = BlockPos.ofFloored(entity.getCameraPosVec(tickDelta));
        BlockPos blockPos2 = BlockPos.ofFloored(holdingEntity.getCameraPosVec(tickDelta));
        int q = this.getBlockLight(entity, blockPos);
        int s = entity.getWorld().getLightLevel(LightType.SKY, blockPos);
        int t = entity.getWorld().getLightLevel(LightType.SKY, blockPos2);

        int u;
        for(u = 0; u <= 24; ++u) {
            renderFusePiece(vertexConsumer, matrix4f, j, k, l, q, q, s, t, 0.025F, 0.025F, o, p, u, false);
        }

        for(u = 24; u >= 0; --u) {
            renderFusePiece(vertexConsumer, matrix4f, j, k, l, q, q, s, t, 0.025F, 0.0F, o, p, u, true);
        }

        matrices.pop();
    }

    private static void renderFusePiece(VertexConsumer vertexConsumer, Matrix4f matrix, float entityX, float entityY, float entityZ, int entityBlockLight, int holderBlockLight, int entitySkyLight, int holderSkyLight, float f, float g, float h, float i, int segmentIndex, boolean isLeashKnot) {
        float j = (float)segmentIndex / 24.0F;
        int k = (int)MathHelper.lerp(j, (float)entityBlockLight, (float)holderBlockLight);
        int l = (int)MathHelper.lerp(j, (float)entitySkyLight, (float)holderSkyLight);
        int m = LightmapTextureManager.pack(k, l);
        float o = 0.2F; // R
        float p = 0.2F; // G
        float q = 0.2F; // N
        float r = entityX * j;
        float s = entityY > 0.0F ? entityY * j * j : entityY - entityY * (1.0F - j) * (1.0F - j);
        float t = entityZ * j;
        vertexConsumer.vertex(matrix, r - h, s + g, t + i).color(o, p, q, 1.0F).light(m);
        vertexConsumer.vertex(matrix, r + h, s + f - g, t - i).color(o, p, q, 1.0F).light(m);
    }

    @Override
    public Identifier getTexture(RocketEntity entity) {
        return Identifier.of(gumRocketsMain.MOD_ID, "textures/entity/rocket.png");
    }
}
