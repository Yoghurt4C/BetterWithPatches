package mods.betterwithpatches.mixins.filteredhopper;

import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.blocks.tile.TileEntityVisibleInventory;
import betterwithmods.util.InvUtils;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.craft.FilteredHopperInteractions;
import mods.betterwithpatches.craft.filteredhopper.HopperRecipe;
import mods.betterwithpatches.data.recipe.HopperFilter;
import mods.betterwithpatches.util.IFilteredHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityFilteredHopper.class)
public abstract class TileEntityFilteredHopperMixin extends TileEntityVisibleInventory implements IFilteredHopper {
    @Shadow(remap = false)
    @Final
    private int xpInvSpace;
    @Shadow(remap = false)
    @Final
    private int xpEjectSize;
    @Shadow(remap = false)
    @Final
    private int delayBetweenXPDrops;
    @Shadow(remap = false)
    private int containedXP;
    @Shadow(remap = false)
    private int xpDropDelay;
    @Shadow(remap = false)
    private int soulsRetained;
    @Shadow(remap = false)
    @Final
    private int maximumRetainedSouls;

    @Shadow(remap = false)
    protected abstract boolean spawnGhast();

    public ItemStack filterStack;
    public HopperFilter filter;
    public AxisAlignedBB collectionZone;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void initFilter(CallbackInfo ctx) {
        this.filterStack = null;
        this.filter = FilteredHopperInteractions.EMPTY;
        //this.collectionZone = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord + 0.625f, this.zCoord, this.xCoord + 1, this.yCoord + 1.15f, this.zCoord + 1);
    }

    @Redirect(method = "readFromNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", ordinal = 3))
    private boolean noShort(NBTTagCompound instance, String key) {
        this.filterStack = this.contents[18];
        this.filter = FilteredHopperInteractions.HOPPER_FILTERS.get(this.filterStack);
        this.collectionZone = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord + 0.625f, this.zCoord, this.xCoord + 1, this.yCoord + 1.15f, this.zCoord + 1);
        return false;
    }

    @Redirect(method = "writeToNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setShort(Ljava/lang/String;S)V", ordinal = 0))
    private void noShort(NBTTagCompound instance, String key, short value) {
    }

    @Redirect(method = "markDirty", at = @At(value = "INVOKE", target = "Lbetterwithmods/blocks/tile/TileEntityFilteredHopper;setFilter(S)V", remap = false))
    private void setActualFilter(TileEntityFilteredHopper instance, short filter) {
        if (this.filterStack != this.contents[18]) {
            this.filterStack = this.contents[18];
            this.filter = FilteredHopperInteractions.HOPPER_FILTERS.get(this.filterStack);
            this.soulsRetained = 0;
        }
    }

    /**
     * @author Yoghurt4C
     * @reason This class has 600 loc...
     */
    @Overwrite(remap = false)
    private boolean validateInventory() {
        boolean stateChanged = false;
        if (this.filterStack != this.contents[18]) {
            this.filterStack = this.contents[18];
            this.filter = FilteredHopperInteractions.HOPPER_FILTERS.get(this.filterStack);
            stateChanged = true;
        }

        short slotsOccupied = (short) InvUtils.getOccupiedStacks(this, 0, 17);
        if (slotsOccupied != this.occupiedSlots) {
            this.occupiedSlots = slotsOccupied;
            stateChanged = true;
        }

        return stateChanged;
    }

    /**
     * @author Yoghurt4C
     * @reason ...and I have little patience.
     */
    @Overwrite(remap = false)
    public boolean canFilterProcessItem(ItemStack stack) {
        return this.filter.test(stack);
    }

    @Inject(method = "updateEntity", at = @At(value = "INVOKE", target = "Lbetterwithmods/blocks/tile/TileEntityFilteredHopper;processSouls()V", remap = false), cancellable = true)
    public void dontDoThat(CallbackInfo ctx) {
        ctx.cancel();
    }

    @Override
    public void onEntityCollidedWithHopper(World world, int x, int y, int z, Entity entity) {
        if (entity != null && !entity.isDead && entity.boundingBox.intersectsWith(this.collectionZone)) {
            TileEntityFilteredHopper tile = (TileEntityFilteredHopper) (Object) this;
            if (this.filter.shouldHopperProcessItems(world, x, y, z, tile, entity)) {
                if (entity instanceof EntityItem) {
                    EntityItem itemEntity = (EntityItem) entity;
                    ItemStack stack = itemEntity.getEntityItem();
                    HopperRecipe recipe = FilteredHopperInteractions.findFilteredRecipe(this.filterStack, itemEntity.getEntityItem());
                    if (recipe != null && recipe.canCraft(world, x, y, z, tile)) {
                        int left = Math.max(0, stack.stackSize - Config.filteredHopperItemsPerTick);
                        int toProcess = stack.stackSize = stack.stackSize - left;
                        for (ItemStack output : recipe.getOutputs(toProcess)) {
                            if (!InvUtils.addItemStackToInvInSlotRange(tile, output, 0, 17)) {
                                EntityItem overflow = new EntityItem(world, itemEntity.posX, itemEntity.posY, itemEntity.posZ, output);
                                overflow.delayBeforeCanPickup = 10;
                                overflow.motionX = itemEntity.motionX;
                                overflow.motionY = itemEntity.motionY;
                                overflow.motionZ = itemEntity.motionZ;
                                world.spawnEntityInWorld(overflow);
                            }
                        }
                        itemEntity.setDead();
                        recipe.onCraft(world, x, y, z, tile);
                    } else if (InvUtils.addItemStackToInvInSlotRange(tile, ((EntityItem) entity).getEntityItem(), 0, 17)) {
                        world.playSoundEffect((double) x + 0.5, (double) y + 0.5, (double) z + 0.5, "random.pop", 0.8F, world.rand.nextFloat() * 0.1F + 0.45F);
                        entity.setDead();
                    }
                }
            }
        }
    }

    @Override
    public int getMaxExperienceCount() {
        return this.xpInvSpace;
    }

    @Override
    public int getMaxEjectedXP() {
        return this.xpEjectSize;
    }

    @Override
    public int getDelayBetweenXPOrbs() {
        return this.delayBetweenXPDrops;
    }

    @Override
    public int getExperienceCount() {
        return this.containedXP;
    }

    @Override
    public int getXPDropDelay() {
        return this.xpDropDelay;
    }

    @Override
    public int getSoulsRetained() {
        return this.soulsRetained;
    }

    @Override
    public int getMaxSoulsRetained() {
        return this.maximumRetainedSouls;
    }

    @Override
    public void setExperienceCount(int xp) {
        this.containedXP = xp;
    }

    @Override
    public void setSoulsRetained(int souls) {
        this.soulsRetained = souls;
    }

    @Override
    public HopperFilter getFilter() {
        return this.filter;
    }

    @Override
    public boolean trySpawnGhast() {
        return this.spawnGhast();
    }

    @Override
    public AxisAlignedBB getCollectionZone() {
        return this.collectionZone;
    }

    @Override
    public void setCollectionZone(AxisAlignedBB box) {
        this.collectionZone = box;
    }
}
