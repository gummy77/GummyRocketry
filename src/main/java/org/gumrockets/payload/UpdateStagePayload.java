package org.gumrockets.payload;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.gumrockets.component.RocketStage;
import org.gumrockets.registry.NetworkingConstants;

public record UpdateStagePayload(int stageID, RocketStage stageData) implements CustomPayload {
    public static final Id<UpdateStagePayload> ID = new Id<>(NetworkingConstants.UPDATE_STAGE_DATA_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, UpdateStagePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpdateStagePayload::stageID,
            PacketCodecs.codec(RocketStage.CODEC), UpdateStagePayload::stageData,
            UpdateStagePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
