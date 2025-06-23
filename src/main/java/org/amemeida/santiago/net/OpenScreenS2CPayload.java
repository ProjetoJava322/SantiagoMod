package org.amemeida.santiago.net;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

public record OpenScreenS2CPayload(int slot, String text) implements CustomPayload {
    public static final Identifier OPEN_SCREEN_ID = Identifier.of(Santiago.MOD_ID, "open_edit_screen");
    public static final CustomPayload.Id<OpenScreenS2CPayload> ID = new CustomPayload.Id<>(OPEN_SCREEN_ID);
    public static final PacketCodec<RegistryByteBuf, OpenScreenS2CPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, OpenScreenS2CPayload::slot,
                    PacketCodecs.STRING, OpenScreenS2CPayload::text, OpenScreenS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
