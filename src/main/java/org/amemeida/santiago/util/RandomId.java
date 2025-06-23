package org.amemeida.santiago.util;

import org.amemeida.santiago.Santiago;

import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class RandomId {
    public static String genRandom(Random random, int size) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'

        StringBuilder buffer = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }

    public static String genRandom(Random random) {
        return genRandom(random, 20);
    }

    public static String genRandom() {
        var random = Santiago.getServer().getWorld(World.OVERWORLD).getRandom();
        return genRandom(random);
    }
}
