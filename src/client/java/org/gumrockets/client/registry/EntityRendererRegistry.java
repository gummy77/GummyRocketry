package org.gumrockets.client.registry;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.gumrockets.client.renderer.RocketEntityRenderer;
import org.gumrockets.client.renderer.StageEntityRenderer;
import org.gumrockets.registry.EntityRegistry;

public class EntityRendererRegistry {

    public static void initializeRegistry() {
        registerRenderer(EntityRegistry.ROCKET_ENTITY, RocketEntityRenderer::new);
        registerRenderer(EntityRegistry.STAGE_ENTITY, StageEntityRenderer::new);
    }

    private static <E extends Entity> void registerRenderer(EntityType<E> entityType, EntityRendererFactory<E> rendererFactory) {
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(entityType, rendererFactory);
    }
}
