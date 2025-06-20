package org.amemeida.santiago.net;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

/**
 * @see net.minecraft.network.packet.c2s.play.SlotChangedStateC2SPacket
 */

public record PCModeC2SPayload(int screenHandlerId) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, PCModeC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.SYNC_ID, PCModeC2SPayload::screenHandlerId, PCModeC2SPayload::new
    );

    public static final Identifier PC_MODE_TOGGLE = Identifier.of(Santiago.MOD_ID, "pc_mode_toggle");
    public static final CustomPayload.Id<PCModeC2SPayload> ID = new CustomPayload.Id<>(PC_MODE_TOGGLE);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
