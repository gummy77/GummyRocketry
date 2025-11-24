package org.gumrockets.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import org.gumrockets.component.PayloadTypes;
import org.gumrockets.entity.PayloadEntity;
import org.gumrockets.entity.RocketEntity;
import org.gumrockets.entity.StageEntity;
import org.gumrockets.payload.UpdatePayloadPayload;
import org.gumrockets.payload.UpdateRocketPayload;
import org.gumrockets.payload.UpdateStagePayload;

public class ClientNetworkHandler {
    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateRocketPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                Entity entity = context.client().world.getEntityById(payload.rocketID());
                if(entity instanceof RocketEntity rocketEntity){
                    rocketEntity.setRocket(payload.rocketData());
                    rocketEntity.setBoundingBox(rocketEntity.calculateBoundingBox());
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(UpdateStagePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                Entity entity = context.client().world.getEntityById(payload.stageID());
                if(entity instanceof StageEntity stageEntity){
                    stageEntity.setStage(payload.stageData());
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(UpdatePayloadPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                Entity entity = context.client().world.getEntityById(payload.payloadID());
                if(entity instanceof PayloadEntity payloadEntity){
                    payloadEntity.setPayloadType(PayloadTypes.valueOf(payload.payloadType()));
                }
            });
        });
    }
}
