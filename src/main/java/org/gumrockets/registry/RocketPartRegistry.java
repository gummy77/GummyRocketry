package org.gumrockets.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.gumrockets.component.RocketPart;
import org.gumrockets.component.rocketpartcomponents.EngineComponent;
import org.gumrockets.component.rocketpartcomponents.FuelComponent;
import org.gumrockets.component.rocketpartcomponents.PayloadCarrierComponent;
import org.gumrockets.item.RocketPartBlockItem;
import org.gumrockets.gumrocketsMain;

public class RocketPartRegistry {

    public static void initializeRegistry() {
        try {
            // -- WOOD --

            registerRocketPart("rocket/wooden/nose",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.WOODEN_NOSE_CONE.getDefaultState(),
                            RocketPart.PartMaterial.WOOD,
                            RocketPart.PartType.NOSE,
                            6, 25
                    ).addPayloadCarrierComponent(new PayloadCarrierComponent(15f)).build());
            registerRocketPart("rocket/wooden/fuel_segment",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.WOODEN_FUEL_SEGMENT.getDefaultState(),
                            RocketPart.PartMaterial.WOOD,
                            RocketPart.PartType.FUEL,
                            6, 25
                    ).addFuelComponent(new FuelComponent(25f, 175f, FuelComponent.FuelType.SOLID)).build());
            registerRocketPart("rocket/wooden/exhaust",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.WOODEN_MOTOR.getDefaultState(),
                            RocketPart.PartMaterial.WOOD,
                            RocketPart.PartType.ENGINE,
                            6, 50
                    ).addEngineComponent(new EngineComponent(2500f, 1, ParticleRegistry.EXHAUST)).build());

            // -- COPPER --

            registerRocketPart("rocket/copper/nose",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.COPPER_NOSE_CONE.getDefaultState(),
                            RocketPart.PartMaterial.COPPER,
                            RocketPart.PartType.NOSE,
                            8, 60
                    ).addPayloadCarrierComponent(new PayloadCarrierComponent(20f)).build());
            registerRocketPart("rocket/copper/fuel_segment",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.COPPER_FUEL_SEGMENT.getDefaultState(),
                            RocketPart.PartMaterial.COPPER,
                            RocketPart.PartType.FUEL,
                            8, 60
                    ).addFuelComponent(new FuelComponent(35f, 175f, FuelComponent.FuelType.SOLID)).build());
            registerRocketPart("rocket/copper/exhaust",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.COPPER_MOTOR.getDefaultState(),
                            RocketPart.PartMaterial.COPPER,
                            RocketPart.PartType.ENGINE,
                            8, 120
                    ).addEngineComponent(new EngineComponent(5000f, 1, ParticleRegistry.EXHAUST)).build());

            // -- IRON --

            registerRocketPart("rocket/iron/nose",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.IRON_NOSE_CONE.getDefaultState(),
                            RocketPart.PartMaterial.IRON,
                            RocketPart.PartType.NOSE,
                            14, 100
                    ).addPayloadCarrierComponent(new PayloadCarrierComponent(45f)).build());
            registerRocketPart("rocket/iron/fuel_segment",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.IRON_FUEL_SEGMENT.getDefaultState(),
                            RocketPart.PartMaterial.IRON,
                            RocketPart.PartType.FUEL,
                            14, 100
                    ).addFuelComponent(new FuelComponent(50f, 255f, FuelComponent.FuelType.SOLID)).build());
            registerRocketPart("rocket/iron/exhaust",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.IRON_MOTOR.getDefaultState(),
                            RocketPart.PartMaterial.IRON,
                            RocketPart.PartType.ENGINE,
                            14, 215
                    ).addEngineComponent(new EngineComponent(7500f, 1, ParticleRegistry.EXHAUST)).build());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerRocketPart(String path, RocketPart rocketPart) throws CloneNotSupportedException {
        Identifier identifier = Identifier.of(gumrocketsMain.MOD_ID, path);

        // create item settings with default values
        Item.Settings settings = new Item.Settings();
        settings.component(ComponentRegistry.ROCKET_PART_COMPONENT_TYPE, rocketPart.clone());

        // register item
        Item item = Registry.register(Registries.ITEM, identifier, new RocketPartBlockItem(rocketPart.getBlock().getBlock(), settings));

        // add to itemgroup (creative tab)
        ItemGroupEvents.modifyEntriesEvent(ItemRegistry.CSP_ITEM_GROUP_KEY).register(itemGroup -> itemGroup.add(item));
    }
}
