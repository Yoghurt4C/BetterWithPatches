package mods.betterwithpatches.event;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import mods.betterwithpatches.data.PenaltyRegistry;
import mods.betterwithpatches.data.PenaltyRegistry.PenaltyData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.Hashtable;
import java.util.UUID;

public class PunitiveEvents {
    public final static UUID PENALTY_SPEED_UUID = UUID.fromString("c5595a67-9410-4fb2-826a-bcaf432c6a6f");
    private final static Hashtable<UUID, Integer> cooldown = new Hashtable<>();

    public PunitiveEvents() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    /*
    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent evt) {
        if (evt.entityLiving instanceof EntityPlayer) {
            PunitivePlayerData data = PenaltyData.getPenaltiesForPlayer((EntityPlayer) evt.entityLiving);
            if (!data.canJump) {
                evt.entityLiving.motionX = 0;
                evt.entityLiving.motionY = 0;
                evt.entityLiving.motionZ = 0;
            }
        }
    }
     */

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent evt) {
        if (evt.phase == TickEvent.Phase.START) {
            UUID id = PenaltyRegistry.getPlayerUUID(evt.player);
            int cd = cooldown.getOrDefault(id, 20);
            if (cd == 20) {
                cooldown.put(id, 0);
                PenaltyRegistry.computePlayerPenalties(evt.player);
            } else {
                cooldown.put(id, cd + 1);
            }
        } else if (evt.player.worldObj.isRemote || evt.player.capabilities.isCreativeMode) {
            return;
        }
        PenaltyData data = PenaltyRegistry.getPenaltiesForPlayer(evt.player);
        if (!data.canSprint) {
            evt.player.setSprinting(false);
        }
        if (evt.player.isInWater() && !data.canSwim && evt.player.worldObj.getBlock((int) evt.player.posX, (int) (evt.player.posY - 2), (int) evt.player.posZ).getMaterial().isReplaceable()) {
            evt.player.motionY -= 0.04;
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent evt) {
        if (evt.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) evt.entityLiving;
            //World world = player.worldObj;

            IAttributeInstance attr = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            if (player.capabilities.isCreativeMode || player.isRiding()) {
                //Remove the modifier when gamemode changes.
                AttributeModifier mod = attr.getModifier(PENALTY_SPEED_UUID);
                if (mod != null) attr.removeModifier(mod);
            }
            //Speed
            PenaltyData data = PenaltyRegistry.getPenaltiesForPlayer(player);
            modifyPlayerSpeed(attr, data.speedMod, "SpeedPenalty", PENALTY_SPEED_UUID);

            /*
            if (!world.isRemote && BWRegistry.PENALTY_HANDLERS.inPain(player)) {
                if (PlayerHelper.isMoving(player) && inPain(player)) {
                    world.playSound(null, player.getPosition(), BWSounds.OOF, SoundCategory.BLOCKS, 0.75f, 1f);
                }
            }
             */

        }
    }

    public static void modifyPlayerSpeed(IAttributeInstance attr, float speedMod, String descriptor, UUID attribute) {
        if (speedMod != 0) {
            AttributeModifier mod = new AttributeModifier(attribute, descriptor, speedMod - 1, 2);
            if (attr.getModifier(attribute) != null) {
                attr.removeModifier(mod);
            }
            attr.applyModifier(mod);
        }
    }
}
