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
import org.gumrockets.entity.StageEntity;

import java.util.ArrayList;

public class StageEntityRenderer extends EntityRenderer<StageEntity> {

    private final BlockRenderManager blockRenderManager;

    public StageEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public boolean shouldRender(StageEntity entity, Frustum frustum, double x, double y, double z) {
        return true; // TODO make this frustum culled with no distance limit?
    }

    @Override
    public void render(StageEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        if(entity.getStage() != null) {
            matrices.push();
            matrices.translate(-0.5f, 0f, -0.5f);


            ArrayList<RocketPart> parts = entity.getStage().getParts();
            for (RocketPart part : parts) {
                matrices.push();
                matrices.translate(part.getOffset().getX(), part.getOffset().getY(), part.getOffset().getZ());
                blockRenderManager.renderBlockAsEntity(part.getBlock(), matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
                matrices.pop();
            }

            matrices.pop();
        }
    }

    @Override
    public Identifier getTexture(StageEntity entity) {
        return Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/rocket.png");
    }
}
