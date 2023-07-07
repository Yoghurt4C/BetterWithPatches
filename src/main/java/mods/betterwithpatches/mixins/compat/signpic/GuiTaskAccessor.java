package mods.betterwithpatches.mixins.compat.signpic;

import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.gui.GuiTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiTask.class)
public interface GuiTaskAccessor {
    @Accessor(remap = false)
    boolean getOshow();

    @Accessor(remap = false)
    void setOshow(boolean bl);

    @Accessor(remap = false)
    VMotion getOright();
}
