package org.gumrockets.client.renderer;

import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.gumrockets.client.model.PayloadEntityModel;
import org.gumrockets.client.registry.ModelRegistry;
import org.gumrockets.component.PayloadTypes;
import org.gumrockets.entity.PayloadEntity;
import org.gumrockets.gumrocketsMain;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PayloadEntityRenderer extends EntityRenderer<PayloadEntity> {
    private EntityModelLoader modelLoader;

    public Model empty_payload_model;
    public Model stardust_catcher_payload_model;

    PayloadTypes payloadType = PayloadTypes.NONE;

    public PayloadEntityRenderer(EntityRendererFactory.Context context) {
        super(context);

        modelLoader = context.getModelLoader();

        this.shadowRadius = 0.3f;
        empty_payload_model = new PayloadEntityModel(context.getPart(ModelRegistry.EMPTY_PAYLOAD_MODEL_LAYER));
        stardust_catcher_payload_model = new PayloadEntityModel(context.getPart(ModelRegistry.STARDUST_CATCHER_PAYLOAD_MODEL_LAYER));
    }

    @Override
    public void render(PayloadEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Model activeModel = empty_payload_model;
        payloadType = entity.getPayloadType();

        switch(entity.getPayloadType()) {
            case EMPTY -> {
                activeModel = empty_payload_model;
            }
            case STARDUST_CATCHER -> {
                activeModel = stardust_catcher_payload_model;
            }
        }

        Identifier identifier = this.getTexture(entity);
        RenderLayer renderLayer = activeModel.getLayer(identifier);

        matrices.push();
        matrices.multiply(new Quaternionf().fromAxisAngleDeg(new Vector3f(0, 1, 0), -yaw));

        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
            matrices.push();
            activeModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
            matrices.pop();
        }

        renderParachute(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }

    public void renderParachute(PayloadEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        float scale = 1; // TODO scaling parachutes for larger payloads

        if(entity.isOnGround()) {
            entity.setLandTime(entity.getLandTime() + tickDelta * 2f);
            if(entity.getLandTime() > 90) {
                matrices.scale(0.75f * scale, 0.75f * scale, 0.75f * scale);
                matrices.multiply(new Quaternionf().fromAxisAngleDeg(new Vector3f(1, 0, 0), 12.5f));
                matrices.translate(0, -0.75f, 1.5f);

                modelLoader.getModelPart(ModelRegistry.FALLEN_PARACHUTE_MODEL_LAYER).render(matrices,
                        vertexConsumers.getBuffer(RenderLayer.getEntitySolid(Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/fallen_parachute_texture.png"))),
                        light, OverlayTexture.DEFAULT_UV);
            } else if(entity.getLandTime() > 75) {
                matrices.translate(0, 0.55f, 0);
                matrices.multiply(new Quaternionf().fromAxisAngleDeg(new Vector3f(1, 0, 0), entity.getLandTime()));
                matrices.translate(0, 1.5f, 0);
                matrices.multiply(new Quaternionf(1, 0, 0, 0));
                matrices.scale(0.5f * scale, 0.5f * scale, 0.5f * scale);
                modelLoader.getModelPart(ModelRegistry.UNDEPLOYED_PARACHUTE_MODEL_LAYER).render(matrices,
                        vertexConsumers.getBuffer(RenderLayer.getEntitySolid(Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/parachute_texture.png"))),
                        light, OverlayTexture.DEFAULT_UV);
            } else {
                matrices.translate(0, 0.55f, 0);
                matrices.multiply(new Quaternionf().fromAxisAngleDeg(new Vector3f(1, 0, 0), entity.getLandTime()));
                matrices.translate(0, 1.2f, 0);
                matrices.multiply(new Quaternionf(1, 0, 0, 0));
                matrices.scale(0.5f * scale, 0.5f * scale, 0.5f * scale);
                modelLoader.getModelPart(ModelRegistry.DEPLOYED_PARACHUTE_MODEL_LAYER).render(matrices,
                        vertexConsumers.getBuffer(RenderLayer.getEntitySolid(Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/parachute_texture.png"))),
                        light, OverlayTexture.DEFAULT_UV);
            }
        } else {
            if(entity.hasParachuteDeployed()) {
                matrices.translate(0, 1.75f, 0);
                matrices.multiply(new Quaternionf(1, 0, 0, 0));
                matrices.scale(0.5f * scale, 0.5f * scale, 0.5f * scale);
                modelLoader.getModelPart(ModelRegistry.DEPLOYED_PARACHUTE_MODEL_LAYER).render(matrices,
                        vertexConsumers.getBuffer(RenderLayer.getEntitySolid(Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/parachute_texture.png"))),
                        light, OverlayTexture.DEFAULT_UV);
            } else {
                matrices.translate(0, 1.75f, 0);
                matrices.multiply(new Quaternionf(1, 0, 0, 0));
                matrices.scale(0.5f * scale, 0.5f * scale, 0.5f * scale);
                modelLoader.getModelPart(ModelRegistry.UNDEPLOYED_PARACHUTE_MODEL_LAYER).render(matrices,
                        vertexConsumers.getBuffer(RenderLayer.getEntitySolid(Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/parachute_texture.png"))),
                        light, OverlayTexture.DEFAULT_UV);
            }
        }
        matrices.pop();
    }

    @Override
    public Identifier getTexture(PayloadEntity entity) {
        switch (payloadType) {
            case EMPTY -> {
                return Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/payload_entity_texture.png");
            }
            case STARDUST_CATCHER -> {
                return Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/stardust_catcher_payload_entity_texture.png");
            }
        }
        return Identifier.of(gumrocketsMain.MOD_ID, "textures/entity/payload_entity_texture.png");
    }
}
