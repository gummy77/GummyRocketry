package org.gumrockets;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.gumrockets.registry.*;

public class gumRocketsMain implements ModInitializer {
    public static final String MOD_ID = "gumrockets";
    public static final Logger LOGGER = LogManager.getLogger("Creeper Space Program");



    @Override
    public void onInitialize() {
        LOGGER.debug("CSP Setup Starting");

        ComponentRegistry.initializeRegistry();
        ItemRegistry.initializeRegistry();
        BlockRegistry.initializeRegistry();
        EntityRegistry.initializeRegistry();
        NetworkingConstants.registerPayloads();
        ParticleRegistry.registerParticles();
        SoundRegistry.registerSounds();
        RocketPartRegistry.initializeRegistry();

        LOGGER.debug("CSP Setup Complete");
    }
}
