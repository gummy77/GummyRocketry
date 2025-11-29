package org.gumrockets.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.gumrockets.entity.PayloadEntity;
import org.gumrockets.registry.EntityRegistry;

public class RocketPayload {
    public static void FinishLaunch(Rocket rocket, World world) {
        switch (rocket.getPayloadType()) {
            case NONE -> { }
            case EMPTY, STARDUST_CATCHER -> {
                SpawnReturnCapsule(rocket.getPayloadType(), world, rocket.getLaunchPosition().add(-rocket.getDeltaV()/2, 0, 0));
            }
        }
        System.out.println("Launch Success");
    }

    public static void SpawnReturnCapsule(PayloadTypes type, World world, Vec3d spawnPos) {
        PayloadEntity payloadEntity = new PayloadEntity(EntityRegistry.PAYLOAD_ENTITY, world, type);
        payloadEntity.setPosition(spawnPos.add(new Vec3d(0, world.getHeight(), 0)));

        NbtCompound nbt = new NbtCompound();

        DataResult<NbtElement> dataResult = Codec.STRING.encodeStart(NbtOps.INSTANCE, type.toString());
        if (dataResult.isSuccess()) {
            nbt.put("payload_type", dataResult.getOrThrow());
        }

        payloadEntity.readCustomDataFromNbt(nbt);

        world.spawnEntity(payloadEntity);
    }
}
