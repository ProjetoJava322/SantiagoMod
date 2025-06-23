package org.amemeida.santiago.items;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import org.amemeida.santiago.components.ScriptComponent;
import org.amemeida.santiago.components.TextContent;
import org.amemeida.santiago.file.Script;
import org.amemeida.santiago.net.OpenScreenS2CPayload;
import org.amemeida.santiago.registry.items.ModComponents;
import org.amemeida.santiago.util.RandomId;

public class FloppyDisk extends Item {
    public FloppyDisk(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void postProcessComponents(ItemStack stack) {
        if (stack.contains(ModComponents.SCRIPT)) {
            stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        } else {
            stack.remove(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
        }
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
                } else if (!off.contains(ModComponents.SCRIPT) && main.contains(ModComponents.SCRIPT)) {
                    off.applyComponentsFrom(main.getComponents());
                    return ActionResult.SUCCESS;
                }
            }
        }
        
        var stack = user.getStackInHand(hand);

        if (!stack.contains(ModComponents.SCRIPT)) {
            var newScript = new Script(RandomId.genRandom(world.getRandom()));
            stack.set(ModComponents.SCRIPT, new ScriptComponent(newScript));
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
