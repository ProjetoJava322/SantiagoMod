package org.amemeida.santiago.net;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.amemeida.santiago.Santiago;

public record TriggerPCC2SPayload(BlockPos pos) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, TriggerPCC2SPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, TriggerPCC2SPayload::pos,
            TriggerPCC2SPayload::new
    );

    public static final Identifier TRIGGER_PC = Identifier.of(Santiago.MOD_ID, "trigger_pc");
    public static final CustomPayload.Id<TriggerPCC2SPayload> ID = new CustomPayload.Id<>(TRIGGER_PC);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
