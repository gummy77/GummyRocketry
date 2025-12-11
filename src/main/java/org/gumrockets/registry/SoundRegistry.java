package org.gumrockets.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.gumrockets.gumRocketsMain;

public class SoundRegistry {

    public static SoundEvent ENTITY_FUSE_BURN; // NOT DONE
    public static SoundEvent ENTITY_SMALL_ROCKET_LAUNCH; // NOT DONE
    public static SoundEvent ENTITY_SMALL_ROCKET_BURN; // NOT DONE
    public static SoundEvent ENTITY_FUSE_ATTACH; // NOT DONE
    public static SoundEvent ENTITY_FUSE_DETACH; // NOT DONE
    public static SoundEvent ENTITY_PARACHUTE_DEPLOY; // NOT DONE
    public static SoundEvent ENTITY_PARACHUTE_FLAP; // NOT DONE
    public static SoundEvent ENTITY_PARACHUTE_LAND; // NOT DONE

    public static SoundEvent ITEM_BASIC_ASSEMBLE;

    public static void registerSounds() {
    }


    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(gumRocketsMain.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    static {
        ENTITY_FUSE_BURN = registerSound("fuse_burn");
        ENTITY_SMALL_ROCKET_LAUNCH = registerSound("small_rocket_launch");
        ENTITY_SMALL_ROCKET_BURN = registerSound("small_rocket_burn");
        ENTITY_FUSE_ATTACH = registerSound("fuse_attach");
        ENTITY_FUSE_DETACH = registerSound("fuse_detach");
        ENTITY_PARACHUTE_DEPLOY = registerSound("parachute_deploy");
        ENTITY_PARACHUTE_FLAP = registerSound("parachute_flap");
        ENTITY_PARACHUTE_LAND = registerSound("parachute_land");

        ITEM_BASIC_ASSEMBLE = registerSound("basic_assemble");

    }
}
