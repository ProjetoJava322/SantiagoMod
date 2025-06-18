package org.amemeida.santiago.net;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;

public record OpenScreenS2CPayload(ItemStack stack) implements CustomPayload {
    public static final Identifier OPEN_SCREEN_ID = Identifier.of(Santiago.MOD_ID, "punch_card_screen");
    public static final CustomPayload.Id<OpenScreenS2CPayload> ID = new CustomPayload.Id<>(OPEN_SCREEN_ID);
    public static final PacketCodec<RegistryByteBuf, OpenScreenS2CPayload> CODEC =
            PacketCodec.tuple(ItemStack.PACKET_CODEC, OpenScreenS2CPayload::stack, OpenScreenS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
