package org.amemeida.santiago.util;

import org.amemeida.santiago.Santiago;

import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * Utilitário para geração de IDs aleatórios compostos por letras minúsculas.
 */
public class RandomId {

    /**
     * Gera uma string aleatória de tamanho especificado usando letras de 'a' a 'z'.
     *
     * @param random gerador de números aleatórios
     * @param size tamanho da string a ser gerada
     * @return string aleatória gerada
     */
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

    /**
     * Gera uma string aleatória padrão de tamanho 20 usando letras de 'a' a 'z'.
     *
     * @param random gerador de números aleatórios
     * @return string aleatória gerada
     */
    public static String genRandom(Random random) {
        return genRandom(random, 20);
    }

    /**
     * Gera uma string aleatória padrão de tamanho 20 usando o gerador de aleatoriedade do mundo Overworld.
     *
     * @return string aleatória gerada
     */
    public static String genRandom() {
        var random = Santiago.getServer().getWorld(World.OVERWORLD).getRandom();
        return genRandom(random);
    }
}
