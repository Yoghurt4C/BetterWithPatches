package mods.betterwithpatches.compat.minetweaker.util;

import betterwithmods.craft.CraftingManagerBulk;
import minetweaker.IUndoableAction;
import net.minecraft.item.ItemStack;

public abstract class RemoveBulkAction implements IUndoableAction {
    public abstract CraftingManagerBulk getManager();

    public abstract String getHandlerName();

    public ItemStack[] outputs;
    public Object[] inputs;

    public RemoveBulkAction(ItemStack[] outputs, Object[] inputs) {
        this.outputs = outputs;
        this.inputs = inputs;
    }

    @Override
    public void apply() {
        this.getManager().removeRecipe(outputs, inputs);
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() {
        this.getManager().addOreRecipe(this.outputs, this.inputs);
    }

    @Override
    public String describe() {
        return MTHelper.removeRecipeDescription(this.getHandlerName(), this.inputs, this.outputs);
    }

    @Override
    public String describeUndo() {
        return MTHelper.addRecipeDescription(this.getHandlerName(), this.inputs, this.outputs);
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
