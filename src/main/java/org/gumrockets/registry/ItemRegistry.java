package org.gumrockets.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.gumrockets.gumrocketsMain;
import org.gumrockets.component.RocketPart;
import org.gumrockets.item.Assembler;
import org.gumrockets.item.LaunchKit;

public class ItemRegistry {

    public static final RegistryKey<ItemGroup> CSP_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(gumrocketsMain.MOD_ID, "item_group"));
    public static final ItemGroup CSP_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.FIREWORK_ROCKET))
            .displayName(Text.translatable("itemGroup.csp.cspmodgroup"))
            .build();

    public static final Assembler BASIC_ASSEMBLER = (Assembler) register("basic_assembler", new Assembler(new Item.Settings(), RocketPart.PartMaterial.IRON));

    public static final LaunchKit BASIC_LAUNCH_KIT = (LaunchKit) register("basic_launch_kit", new LaunchKit(new Item.Settings()));

    public static void initializeRegistry() {
        Registry.register(Registries.ITEM_GROUP, CSP_ITEM_GROUP_KEY, CSP_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(CSP_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(BASIC_LAUNCH_KIT);
            itemGroup.add(BASIC_ASSEMBLER);
        });
        gumrocketsMain.LOGGER.debug("Registering items complete");
    }

    public static Item register (String path, Item item) {
        Identifier itemID = Identifier.of(gumrocketsMain.MOD_ID, path);
        return Registry.register(Registries.ITEM, itemID, item);
    }
}
