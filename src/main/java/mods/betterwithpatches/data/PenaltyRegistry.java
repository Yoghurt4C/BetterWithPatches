package mods.betterwithpatches.data;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Hashtable;
import java.util.UUID;

public class PenaltyRegistry {
    public static final Hashtable<UUID, PenaltyData> PENALTIES = new Hashtable<>();
    public static final Hashtable<String, Penalty> PENALTY_PREDICATES = new Hashtable<>();
    public static final PenaltyData defaultValue = new PenaltyData();

    public static void computePlayerPenalties(EntityPlayer player) {
        PenaltyData data = new PenaltyData();
        for (Penalty penalty : PENALTY_PREDICATES.values()) {
            penalty.apply(player, data);
        }
        PENALTIES.put(player.getUniqueID(), data);
    }


    public static PenaltyData getPenaltiesForPlayer(EntityPlayer player) {
        return PENALTIES.getOrDefault(getPlayerUUID(player), defaultValue);
    }

    public static UUID getPlayerUUID(EntityPlayer player) {
        return player.getGameProfile() != null ? player.getGameProfile().getId() : player.getUniqueID();
    }

    public static class PenaltyData {
        public boolean canJump = true;
        public boolean canSwim = true;
        public boolean canHeal = true;
        public boolean canSprint = true;
        public boolean canAttack = true;
        public boolean isInPain = false;
        public float speedMod = 1.0f;
    }
}