package mods.betterwithpatches.mixins.compat.signpic;

import com.kamesuta.mc.bnnwidget.WCommon;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WTypedPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.http.Communicator;
import mods.betterwithpatches.compat.signpic.GuiTaskHolder;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Pseudo
@Mixin(targets = {"com/kamesuta/mc/signpic/gui/GuiTask$1$1"})
public abstract class GuiTask$1$1Mixin extends WTypedPanel<WCommon> {
    public GuiTask$1$1Mixin(@Nonnull R position) {
        super(position);
    }
    @Dynamic
    @Inject(method = "update(Lcom/kamesuta/mc/bnnwidget/WEvent;Lcom/kamesuta/mc/bnnwidget/position/Area;Lcom/kamesuta/mc/bnnwidget/position/Point;)V", at = @At("HEAD"), remap = false, cancellable = true)
    public void test(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point p, CallbackInfo ctx) {
        ctx.cancel();
        GuiTaskAccessor this$0 = (GuiTaskAccessor) GuiTaskHolder.TASK;
        if (Client.mc.currentScreen != null && !Communicator.instance.getTasks().isEmpty()) {

            if (!this$0.getOshow()) {
                this$0.getOright().stop().add(Easings.easeOutQuart.move(0.5F, 2.0F)).start();
            }

            this$0.setOshow(true);
        } else {
            if (this$0.getOshow()) {
                this$0.getOright().stop().add(Easings.easeOutQuart.move(0.5F, 0.0F)).start();
            }

            this$0.setOshow(false);
        }

        super.update(ev, pgp, p);
    }
}
