package org.gumrockets.payload;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.gumrockets.component.PayloadTypes;
import org.gumrockets.component.Rocket;
import org.gumrockets.registry.NetworkingConstants;

public record UpdatePayloadPayload(int payloadID, String payloadType) implements CustomPayload {

    public static final Id<UpdatePayloadPayload> ID = new Id<>(NetworkingConstants.UPDATE_PAYLOAD_DATA_PACKET_ID);

    public static final PacketCodec<RegistryByteBuf, UpdatePayloadPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpdatePayloadPayload::payloadID,
            PacketCodecs.codec(Codec.STRING), UpdatePayloadPayload::payloadType,
            UpdatePayloadPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
