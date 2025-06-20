package org.amemeida.santiago.items;

import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;
import org.amemeida.santiago.components.TextContent;
import org.amemeida.santiago.file.Script;
import org.amemeida.santiago.net.OpenScreenS2CPayload;

import java.nio.file.Path;

public class FloppyDisk extends Item {
    public FloppyDisk(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return ActionResult.PASS;
        }

        Script.setServer(world.getServer());

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
