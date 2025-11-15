package org.gumrockets.client.renderer;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.gumrockets.gumrocketsMain;
import org.gumrockets.component.RocketPart;
import org.gumrockets.component.RocketStage;
import org.gumrockets.entity.RocketEntity;

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
        }
    }

    @Override
    public Identifier getTexture(RocketEntity entity) {
        return Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/rocket.png");
    }
}
