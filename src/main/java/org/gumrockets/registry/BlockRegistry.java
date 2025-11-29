package org.gumrockets.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.gumrockets.gumrocketsMain;
import org.gumrockets.block.RocketPartBlock;
import org.gumrockets.entity.RocketPartBlockEntity;

public class BlockRegistry {

    public static final RocketPartBlock WOODEN_NOSE_CONE = (RocketPartBlock) register("rocket/wooden/nose",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)));
    public static final RocketPartBlock WOODEN_FUEL_SEGMENT = (RocketPartBlock) register("rocket/wooden/fuel_segment",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)));
    public static final RocketPartBlock WOODEN_MOTOR = (RocketPartBlock) register("rocket/wooden/exhaust",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)));

    public static final RocketPartBlock COPPER_NOSE_CONE = (RocketPartBlock) register("rocket/copper/nose",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.COPPER)));
    public static final RocketPartBlock COPPER_FUEL_SEGMENT = (RocketPartBlock) register("rocket/copper/fuel_segment",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.COPPER)));
    public static final RocketPartBlock COPPER_MOTOR = (RocketPartBlock) register("rocket/copper/exhaust",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.COPPER)));

    public static final RocketPartBlock IRON_NOSE_CONE = (RocketPartBlock) register("rocket/iron/nose",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.METAL)));
    public static final RocketPartBlock IRON_FUEL_SEGMENT = (RocketPartBlock) register("rocket/iron/fuel_segment",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.METAL)));
    public static final RocketPartBlock IRON_MOTOR = (RocketPartBlock) register("rocket/iron/exhaust",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.METAL)));

    public static BlockEntityType<RocketPartBlockEntity> ROCKET_PART_BLOCK_ENTITY = registerEntity("rocket_part_block_entity",
            BlockEntityType.Builder.create(RocketPartBlockEntity::new,
                    WOODEN_NOSE_CONE, WOODEN_FUEL_SEGMENT, WOODEN_MOTOR,
                    COPPER_NOSE_CONE, COPPER_FUEL_SEGMENT, COPPER_MOTOR,
                    IRON_NOSE_CONE, IRON_FUEL_SEGMENT, IRON_MOTOR
            ).build());

    public static void initializeRegistry() {

    }

    private static Block register(String path, Block block){
        Identifier blockID = Identifier.of(gumrocketsMain.MOD_ID, path);
        return Registry.register(Registries.BLOCK, blockID, block);
    }

    public static <T extends BlockEntityType<?>> T registerEntity (String path, T type) {
        Identifier blockEntityID = Identifier.of(gumrocketsMain.MOD_ID, path);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, blockEntityID, type);
    }
}
