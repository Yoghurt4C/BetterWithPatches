package mods.betterwithpatches.data;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Hashtable;
import java.util.UUID;

public interface PenaltyRegistry {
    Hashtable<UUID, PenaltyData> PENALTIES = new Hashtable<>();
    Hashtable<String, Penalty> PENALTY_PREDICATES = new Hashtable<>();
    PenaltyData defaultValue = new PenaltyData();

    static void computePlayerPenalties(EntityPlayer player) {
        PenaltyData data = new PenaltyData();
        for (Penalty penalty : PENALTY_PREDICATES.values()) {
            penalty.apply(player, data);
        }
        PENALTIES.put(player.getUniqueID(), data);
    }


    static PenaltyData getPenaltiesForPlayer(EntityPlayer player) {
        return PENALTIES.getOrDefault(getPlayerUUID(player), defaultValue);
    }

    static UUID getPlayerUUID(EntityPlayer player) {
        return player.getGameProfile() != null ? player.getGameProfile().getId() : player.getUniqueID();
    }

    class PenaltyData {
        public byte canJump = 0;
        public byte canSwim = 0;
        public byte canHeal = 0;
        public byte canSprint = 0;
        public byte canAttack = 0;
        public byte isInPain = 0;
        public float speedMod = 1.0f;
    }
}