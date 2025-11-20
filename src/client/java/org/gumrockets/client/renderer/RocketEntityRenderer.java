package org.gumrockets.client.renderer;

import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.gumrockets.gumrocketsMain;
import org.gumrockets.component.RocketPart;
import org.gumrockets.component.RocketStage;
import org.gumrockets.entity.RocketEntity;
import org.joml.Matrix4f;

import java.util.ArrayList;

public class RocketEntityRenderer extends EntityRenderer<RocketEntity> {

    private final BlockRenderManager blockRenderManager;

    public RocketEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        blockRenderManager = context.getBlockRenderManager();
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

//            for(int i = 0; i <= stageCounter-1; i++) {
//                RocketStage stage = stages.get(i);
//                matrices.translate(0, -stage.getHeight(), 0);
//            }

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

            if (entity.getFuseHolder() != null) {
                this.renderFuse(entity, tickDelta, matrices, vertexConsumers, entity.getFuseHolder());
            }
        }
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
        float o = 0.3F; // R
        float p = 0.3F; // G
        float q = 0.3F; // N
        float r = entityX * j;
        float s = entityY > 0.0F ? entityY * j * j : entityY - entityY * (1.0F - j) * (1.0F - j);
        float t = entityZ * j;
        vertexConsumer.vertex(matrix, r - h, s + g, t + i).color(o, p, q, 1.0F).light(m);
        vertexConsumer.vertex(matrix, r + h, s + f - g, t - i).color(o, p, q, 1.0F).light(m);
    }

    @Override
    public Identifier getTexture(RocketEntity entity) {
        return Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/rocket.png");
    }
}
