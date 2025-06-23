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
import org.amemeida.santiago.components.TextContent;
import org.amemeida.santiago.net.OpenScreenS2CPayload;

/**
 *
 * @see net.minecraft.item.WritableBookItem
 * @see net.minecraft.item.Items
 */

public class PunchCard extends Item {
    public PunchCard(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return ActionResult.PASS;
        }

        if (user.isSneaking()) {
            ItemStack main = user.getMainHandStack();
            ItemStack off = user.getOffHandStack();

            if (main.getItem() == off.getItem()) {
                if (hand == Hand.MAIN_HAND) {
                    return ActionResult.SUCCESS;
                } else {
                    off.applyComponentsFrom(main.getComponents());
                    return ActionResult.SUCCESS;
                }
            }
        }

        int slot = hand == Hand.MAIN_HAND ? user.getInventory().getSelectedSlot() : 40;
        var content = TextContent.get(user.getInventory().getStack(slot));

        if (content == null) {
            return ActionResult.SUCCESS;
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        var payload = new OpenScreenS2CPayload(slot, content.text());
        ServerPlayNetworking.send((ServerPlayerEntity) user, payload);

        return ActionResult.SUCCESS;
    }
}
