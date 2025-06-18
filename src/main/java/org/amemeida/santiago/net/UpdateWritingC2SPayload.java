package org.amemeida.santiago.net;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

public record UpdateWritingC2SPayload implements CustomPayload {
    public static final Identifier UPDATE_TEXT_ID = Identifier.of(Santiago.MOD_ID, "update_text");
    public static final CustomPayload.Id<OpenScreenS2CPayload> ID = new CustomPayload.Id<>(UPDATE_TEXT_ID);
    public static final PacketCodec<RegistryByteBuf, UpdateWritingC2SPayload> CODEC =
            PacketCodec.tuple(ItemStack.PACKET_CODEC, uPD::stack, OpenScreenS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
