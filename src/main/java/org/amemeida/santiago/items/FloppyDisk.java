package org.amemeida.santiago.items;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.amemeida.santiago.components.LocalText;
import org.amemeida.santiago.net.OpenScreenS2CPayload;
import org.amemeida.santiago.registry.items.ModComponents;

public class FloppyDisk extends Item {
    public FloppyDisk(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return ActionResult.PASS;
        }

        ItemStack itemStack = user.getStackInHand(hand);

        if (!itemStack.contains(ModComponents.LOCAL_TEXT)) {
            itemStack.set(ModComponents.LOCAL_TEXT, new LocalText(""));
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        var payload = new OpenScreenS2CPayload(itemStack);
        ServerPlayNetworking.send((ServerPlayerEntity) user, payload);

        return ActionResult.SUCCESS;
    }
}
