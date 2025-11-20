package org.gumrockets.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import org.gumrockets.client.item.PayloadCompassAnglePredicateProvider;
import org.gumrockets.client.registry.ParticleRegistry;
import org.gumrockets.component.PayloadCompassComponent;
import org.gumrockets.item.PayloadCompass;
import org.gumrockets.registry.ComponentRegistry;
import org.gumrockets.registry.ItemRegistry;

import java.util.Objects;

public class gumrocketsMainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        org.gumrockets.client.ClientNetworkHandler.initialize();
        org.gumrockets.client.registry.EntityRendererRegistry.initializeRegistry();
        ParticleRegistry.initializeRegistry();

        registerModelPredicateProviders();
    }

    public static void registerModelPredicateProviders() {
        ModelPredicateProviderRegistry.register(ItemRegistry.PAYLOAD_COMPASS, Identifier.of("angle"), new PayloadCompassAnglePredicateProvider((world, stack, entity) -> {
            return PayloadCompassComponent.getCompassPosition(stack, world);
        }));
    }
}
