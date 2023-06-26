package mods.betterwithpatches.compat.minetweaker.util;

import betterwithmods.craft.CraftingManagerBulk;
import minetweaker.IUndoableAction;
import net.minecraft.item.ItemStack;

public abstract class AddBulkAction implements IUndoableAction {
    public abstract CraftingManagerBulk getManager();

    public abstract String getHandlerName();

    public ItemStack[] outputs;
    public Object[] inputs;

    public AddBulkAction(ItemStack[] outputs, Object[] inputs) {
        this.outputs = outputs;
        this.inputs = inputs;
    }

    @Override
    public void apply() {
        this.getManager().addOreRecipe(this.outputs, this.inputs);
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() {
        this.getManager().removeRecipe(this.outputs, this.inputs);
    }

    @Override
    public String describe() {
        return MTHelper.addRecipeDescription(this.getHandlerName(), this.inputs, this.outputs);
    }

    @Override
    public String describeUndo() {
        return MTHelper.removeRecipeDescription(this.getHandlerName(), this.inputs, this.outputs);
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
