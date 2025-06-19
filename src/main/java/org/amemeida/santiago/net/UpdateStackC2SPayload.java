package org.amemeida.santiago.net;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

public record UpdateStackC2SPayload(int slot, ItemStack stack) implements CustomPayload {
    public static final Identifier UPDATE_TEXT_ID = Identifier.of(Santiago.MOD_ID, "update_text");
    public static final CustomPayload.Id<UpdateStackC2SPayload> ID = new CustomPayload.Id<>(UPDATE_TEXT_ID);
    public static final PacketCodec<RegistryByteBuf, UpdateStackC2SPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, UpdateStackC2SPayload::slot,
                    ItemStack.PACKET_CODEC, UpdateStackC2SPayload::stack, UpdateStackC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
