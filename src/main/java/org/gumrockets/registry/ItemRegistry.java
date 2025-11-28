package org.gumrockets.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.gumrockets.component.PayloadTypes;
import org.gumrockets.gumrocketsMain;
import org.gumrockets.component.RocketPart;
import org.gumrockets.item.Assembler;
import org.gumrockets.item.LaunchKit;
import org.gumrockets.item.PayloadItem;
import org.gumrockets.item.RocketInspector;

public class ItemRegistry {

    public static final RegistryKey<ItemGroup> CSP_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(gumrocketsMain.MOD_ID, "item_group"));
    public static final ItemGroup CSP_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.FIREWORK_ROCKET))
            .displayName(Text.translatable("itemGroup.gumrockets.gumrocketsmodgroup"))
            .build();

    // Tools
    public static final Assembler BASIC_ASSEMBLER = (Assembler) register("basic_assembler", new Assembler(new Item.Settings(), RocketPart.PartMaterial.IRON));
    public static final LaunchKit BASIC_LAUNCH_KIT = (LaunchKit) register("basic_launch_kit", new LaunchKit(new Item.Settings()));
    public static final Item PAYLOAD_COMPASS = register("payload_compass", new Item(new Item.Settings()));
    public static final Item ROCKET_INSPECTOR = register("rocket_inspector", new RocketInspector(new Item.Settings().maxCount(1)));

    // Resources
    public static final Item STARDUST = register("stardust", new Item(new Item.Settings().rarity(Rarity.RARE)));
    public static final Item FUSE = register("fuse", new Item(new Item.Settings()));
    public static final Item PARACHUTE = register("parachute", new Item(new Item.Settings()));
    public static final Item IRON_EXHAUST_NOZZLE = register("iron_exhaust_nozzle", new Item(new Item.Settings()));

    // Payloads
    public static final Item EMPTY_PAYLOAD = register("payloads/empty_payload", new PayloadItem(new Item.Settings(), PayloadTypes.EMPTY, 5));
    public static final Item STARDUST_CATCHER = register("payloads/stardust_catcher_payload", new PayloadItem(new Item.Settings(), PayloadTypes.STARDUST_CATCHER, 10));



    public static void initializeRegistry() {
        Registry.register(Registries.ITEM_GROUP, CSP_ITEM_GROUP_KEY, CSP_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(CSP_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(BASIC_LAUNCH_KIT);
            itemGroup.add(BASIC_ASSEMBLER);
            itemGroup.add(PAYLOAD_COMPASS);
            itemGroup.add(ROCKET_INSPECTOR);

            itemGroup.add(STARDUST);
            itemGroup.add(FUSE);
            itemGroup.add(PARACHUTE);
            itemGroup.add(IRON_EXHAUST_NOZZLE);

            itemGroup.add(EMPTY_PAYLOAD);
            itemGroup.add(STARDUST_CATCHER);

        });
        gumrocketsMain.LOGGER.debug("Registering items complete");
    }

    public static Item register (String path, Item item) {
        Identifier itemID = Identifier.of(gumrocketsMain.MOD_ID, path);
        return Registry.register(Registries.ITEM, itemID, item);
    }
}
