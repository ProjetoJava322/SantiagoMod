package org.amemeida.santiago.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.amemeida.santiago.components.EnderIOComponent;
import org.amemeida.santiago.registry.items.ModComponents;
import org.amemeida.santiago.util.RandomId;


public class EnderCard extends PunchCard {
    public EnderCard(Settings settings) {
        super(settings);
    }

    @Override
    public void postProcessComponents(ItemStack stack) {
        if (stack.contains(ModComponents.ENDER)) {
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
                } else if (!off.contains(ModComponents.ENDER) && main.contains(ModComponents.ENDER)) {
                    off.applyComponentsFrom(main.getComponents());
                    return ActionResult.SUCCESS;
                }
            }
        }

        var stack = user.getStackInHand(hand);

        if (!stack.contains(ModComponents.ENDER)) {
            var text = RandomId.genRandom(world.getRandom());
            stack.set(ModComponents.ENDER, new EnderIOComponent(text));
        }

        return super.use(world, user, hand);
    }
}
