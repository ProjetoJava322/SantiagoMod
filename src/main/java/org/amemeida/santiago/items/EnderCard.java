package org.amemeida.santiago.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.amemeida.santiago.components.EnderIOComponent;
import org.amemeida.santiago.registry.items.ModComponents;
import java.util.Random;

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

            if (main.getItem() instanceof EnderCard && off.getItem() instanceof EnderCard) {
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
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String generatedString = buffer.toString();
            stack.set(ModComponents.ENDER, new EnderIOComponent(generatedString));
        }

        return super.use(world, user, hand);
    }
}
