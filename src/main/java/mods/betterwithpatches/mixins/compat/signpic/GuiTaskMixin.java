package mods.betterwithpatches.mixins.compat.signpic;

import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.gui.GuiTask;
import mods.betterwithpatches.compat.signpic.GuiTaskHolder;
import org.lwjgl.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Mixin(GuiTask.class)
public class GuiTaskMixin {
    @Shadow(remap = false)
    @Nonnull
    protected Timer showtime;

    @Shadow(remap = false)
    @Nonnull
    protected VMotion right;

    @Inject(method = "initWidget", at = @At("HEAD"), remap = false)
    public void setter(CallbackInfo ctx) {
        this.showtime.set(0.0f);
        this.right.stop().add(Easings.easeOutQuart.move(0f, -0.1f)).start();
        GuiTaskHolder.TASK = (GuiTask) (Object) this;
    }
}
