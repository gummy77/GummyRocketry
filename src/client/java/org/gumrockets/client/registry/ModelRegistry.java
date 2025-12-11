package org.gumrockets.client.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.gumrockets.client.model.*;
import org.gumrockets.client.renderer.PayloadEntityRenderer;
import org.gumrockets.gumRocketsMain;
import org.gumrockets.registry.EntityRegistry;

public class ModelRegistry {
    public static final EntityModelLayer EMPTY_PAYLOAD_MODEL_LAYER = registerModel("payload_entity_model", EntityRegistry.PAYLOAD_ENTITY, PayloadEntityRenderer::new);
    public static final EntityModelLayer STARDUST_CATCHER_PAYLOAD_MODEL_LAYER = registerModel("stardust_catcher_payload_entity_model", EntityRegistry.PAYLOAD_ENTITY, PayloadEntityRenderer::new);
    public static final EntityModelLayer DEPLOYED_PARACHUTE_MODEL_LAYER = registerModel("deployed_parachute_model", EntityRegistry.PAYLOAD_ENTITY, PayloadEntityRenderer::new);
    public static final EntityModelLayer UNDEPLOYED_PARACHUTE_MODEL_LAYER = registerModel("undeployed_parachute_model", EntityRegistry.PAYLOAD_ENTITY, PayloadEntityRenderer::new);
    public static final EntityModelLayer FALLEN_PARACHUTE_MODEL_LAYER = registerModel("fallen_parachute_model", EntityRegistry.PAYLOAD_ENTITY, PayloadEntityRenderer::new);


    public static void registerModels() {
        EntityModelLayerRegistry.registerModelLayer(EMPTY_PAYLOAD_MODEL_LAYER, PayloadEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(STARDUST_CATCHER_PAYLOAD_MODEL_LAYER, StardustCatcher_PayloadEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DEPLOYED_PARACHUTE_MODEL_LAYER, DeployedParachuteModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(UNDEPLOYED_PARACHUTE_MODEL_LAYER, UndeployedParachuteModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(FALLEN_PARACHUTE_MODEL_LAYER, FallenParachuteModel::getTexturedModelData);
    }

    private static <E extends Entity> EntityModelLayer registerModel(String path, EntityType<E> entityType, EntityRendererFactory<E> rendererFactory) {
        return new EntityModelLayer(Identifier.of(gumRocketsMain.MOD_ID, path), "main");
    }

}
