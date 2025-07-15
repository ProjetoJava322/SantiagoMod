package org.amemeida.santiago.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ModGamerules {
    public static final GameRules.Key<GameRules.IntRule> COMPUTER_TIMEOUT = GameRuleRegistry.register("setComputerTimeout", GameRules.Category.UPDATES, GameRuleFactory.createIntRule(2000, -1));
    public static void initialize(){}

}
