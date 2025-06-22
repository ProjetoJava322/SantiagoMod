package org.amemeida.santiago.net;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.computer.ComputerEntity;

/**
 * @see net.minecraft.network.packet.c2s.play.SlotChangedStateC2SPacket
 */

public record PCDataC2SPayload(int screenHandlerId, ComputerEntity.ComputerData data) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, PCDataC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.SYNC_ID, PCDataC2SPayload::screenHandlerId,
            ComputerEntity.ComputerData.PACKET_CODEC, PCDataC2SPayload::data,
            PCDataC2SPayload::new
    );

    public static final Identifier PC_DATA = Identifier.of(Santiago.MOD_ID, "pc_data");
    public static final CustomPayload.Id<PCDataC2SPayload> ID = new CustomPayload.Id<>(PC_DATA);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
