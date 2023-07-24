package mods.betterwithpatches.data;

import net.minecraft.entity.player.EntityPlayer;

@FunctionalInterface
public interface Penalty {
    void apply(EntityPlayer player, PenaltyRegistry.PunitivePlayerData data);
}
