package mods.betterwithpatches.data;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Hashtable;
import java.util.UUID;

public class PenaltyRegistry {
    public static final Hashtable<UUID, PunitivePlayerData> PENALTIES = new Hashtable<>();
    public static final Hashtable<String, Penalty> PENALTY_PREDICATES = new Hashtable<>();
    public static final PunitivePlayerData defaultValue = new PunitivePlayerData();

    public static void computePlayerPenalties(EntityPlayer player) {
        PENALTIES.compute(player.getUniqueID(), (uuid, punitivePlayerData) -> {
            PunitivePlayerData data = new PunitivePlayerData();
            for (Penalty penalty : PENALTY_PREDICATES.values()) {
                penalty.apply(player, data);
            }
            return data;
        });
    }


    public static PunitivePlayerData getPenaltiesForPlayer(EntityPlayer player) {
        return PENALTIES.getOrDefault(getPlayerUUID(player), defaultValue);
    }

    public static UUID getPlayerUUID(EntityPlayer player) {
        return player.getGameProfile() != null ? player.getGameProfile().getId() : player.getUniqueID();
    }

    public static class PunitivePlayerData {
        public boolean canJump = true;
        public boolean canSwim = true;
        public boolean canHeal = true;
        public boolean canSprint = true;
        public boolean canAttack = true;
        public boolean isInPain = false;
        public float speedMod = 1.0f;
    }
}