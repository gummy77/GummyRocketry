package org.gumrockets.client.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.gumrockets.client.model.PayloadEntityModel;
import org.gumrockets.client.model.StardustCatcher_PayloadEntityModel;
import org.gumrockets.client.renderer.PayloadEntityRenderer;
import org.gumrockets.registry.EntityRegistry;
import org.gumrockets.gumrocketsMain;

public class ModelRegistry {
    public static final EntityModelLayer EMPTY_PAYLOAD_MODEL_LAYER = registerModel("payload_entity_model", EntityRegistry.PAYLOAD_ENTITY, PayloadEntityRenderer::new);
    public static final EntityModelLayer STARDUST_CATCHER_PAYLOAD_MODEL_LAYER = registerModel("stardust_catcher_payload_entity_model", EntityRegistry.PAYLOAD_ENTITY, PayloadEntityRenderer::new);

    public static void registerModels() {
        EntityModelLayerRegistry.registerModelLayer(EMPTY_PAYLOAD_MODEL_LAYER, PayloadEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(STARDUST_CATCHER_PAYLOAD_MODEL_LAYER, StardustCatcher_PayloadEntityModel::getTexturedModelData);
    }

    private static <E extends Entity> EntityModelLayer registerModel(String path, EntityType<E> entityType, EntityRendererFactory<E> rendererFactory) {
        return new EntityModelLayer(Identifier.of(gumrocketsMain.MOD_ID, path), "main");
    }

}
