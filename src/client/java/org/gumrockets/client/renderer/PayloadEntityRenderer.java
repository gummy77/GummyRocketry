package org.gumrockets.client.renderer;

import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.gumrockets.client.model.PayloadEntityModel;
import org.gumrockets.client.registry.ModelRegistry;
import org.gumrockets.component.PayloadTypes;
import org.gumrockets.entity.PayloadEntity;
import org.gumrockets.gumrocketsMain;

public class PayloadEntityRenderer extends EntityRenderer<PayloadEntity> {

    public Model empty_payload_model;
    public Model stardust_catcher_payload_model;

    PayloadTypes payloadType = PayloadTypes.NONE;

    public PayloadEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
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

        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
            matrices.push();
            activeModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
            matrices.pop();
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
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
