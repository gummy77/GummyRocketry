package org.gumrockets.client;

import net.fabricmc.api.ClientModInitializer;

public class gumrocketsMainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        org.gumrockets.client.ClientNetworkHandler.initialize();
        org.gumrockets.client.registry.EntityRendererRegistry.initializeRegistry();
    }
}
