package mods.betterwithpatches.features;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.data.Penalty;
import mods.betterwithpatches.data.PenaltyRegistry;
import mods.betterwithpatches.event.PunitiveEvents;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.FOVUpdateEvent;

import java.util.Hashtable;
import java.util.UUID;

public interface HCMovement {

    Hashtable<Material, Float> MATERIALS = new Hashtable<>();
    Hashtable<Block, Float> BLOCK_OVERRIDES = new Hashtable<>();
    Hashtable<UUID, Float> FALLBACK = new Hashtable<>();

    static void registerHCMovement() {
        PenaltyRegistry.PENALTY_PREDICATES.put("HCMovement", new MovementPenalty());

        float FAST = Config.hcMovementFast;
        MATERIALS.put(Material.rock, FAST);
        MATERIALS.put(Material.wood, FAST);
        MATERIALS.put(Material.iron, FAST);
        MATERIALS.put(Material.cloth, FAST);
        MATERIALS.put(Material.carpet, FAST);
        MATERIALS.put(Material.circuits, FAST);

        MATERIALS.put(Material.grass, 1.0f);
        MATERIALS.put(Material.glass, 1.0f);
        MATERIALS.put(Material.ground, 1.0f);
        MATERIALS.put(Material.clay, 1.0f);

        MATERIALS.put(Material.sand, 0.75f);
        MATERIALS.put(Material.snow, 0.75f);
        MATERIALS.put(Material.leaves, 0.70f);
        MATERIALS.put(Material.plants, 0.70f);
        MATERIALS.put(Material.vine, 0.70f);

        BLOCK_OVERRIDES.put(Blocks.soul_sand, 0.70f);
        BLOCK_OVERRIDES.put(Blocks.gravel, FAST);
    }

    class MovementPenalty implements Penalty {

        @Override
        public void apply(EntityPlayer player, PenaltyRegistry.PenaltyData data) {
            if (player.isRiding()) return;
            float speed = 0;
            if (player.onGround) {
                Block block = player.worldObj.getBlock((int) player.posX, BWPUtils.offsetYDown(player, 0.2f), (int) player.posZ);
                if (BLOCK_OVERRIDES.containsKey(block)) {
                    speed = BLOCK_OVERRIDES.get(block);
                } else {
                    speed = MATERIALS.getOrDefault(block.getMaterial(), Config.hcMovementDefault);
                }
                Block in = player.worldObj.getBlock((int) player.posX, BWPUtils.offsetYDown(player, 0), (int) player.posZ);
                if (in.getMaterial() != Material.air && !in.getMaterial().isSolid()) {
                    if (BLOCK_OVERRIDES.containsKey(in)) {
                        speed = BLOCK_OVERRIDES.get(in);
                    } else if (MATERIALS.containsKey(in.getMaterial())) {
                        speed *= MATERIALS.get(in.getMaterial());
                    }
                }
                FALLBACK.put(PenaltyRegistry.getPlayerUUID(player), speed);
            }
            if (speed == 0) {
                data.speedMod *= FALLBACK.getOrDefault(PenaltyRegistry.getPlayerUUID(player), Config.hcMovementDefault);
            } else {
                data.speedMod *= speed;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    class HCMovementFOV {
        @SubscribeEvent
        public void counteractFOV(FOVUpdateEvent evt) {
            EntityPlayer player = evt.entity;
            float f;
            if (player.capabilities.isFlying) {
                f = 1.1f;
            } else {
                f = 1f;
            }

            IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

            double value = iattributeinstance.getAttributeValue();
            AttributeModifier mod = iattributeinstance.getModifier(PunitiveEvents.PENALTY_SPEED_UUID);
            if (mod != null)
                value /= (1 + mod.getAmount());
            f = (float) (f * ((value / player.capabilities.getWalkSpeed() + 1f) * 0.5f));

            if (player.capabilities.getWalkSpeed() == 0 || Float.isNaN(f) || Float.isInfinite(f)) {
                f = 1f;
            }
            if (player.isUsingItem() && player.getItemInUse().getItem() == Items.bow) {
                int i = player.getItemInUseDuration();
                float f1 = (float) i * 0.05f;

                if (f1 > 1f) {
                    f1 = 1f;
                } else {
                    f1 = f1 * f1;
                }

                f *= 1f - f1 * 0.15f;
            }

            evt.newfov = f;
        }
    }
}
