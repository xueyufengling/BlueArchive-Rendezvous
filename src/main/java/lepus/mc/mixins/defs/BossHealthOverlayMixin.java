package lepus.mc.mixins.defs;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lepus.mc.mixins.internal.BossHealthOverlayInternal;
import lepus.mc.mixins.internal.Internal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;

@Mixin(targets = { "net.minecraft.client.gui.components.BossHealthOverlay" })
public abstract class BossHealthOverlayMixin {
	@Shadow
	@Final
	Map<UUID, LerpingBossEvent> events;

	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	public void after_invoke(GuiGraphics guiGraphics, CallbackInfo ci) {
		BossHealthOverlayInternal.Render.Args.store((BossHealthOverlay) (Object) this, guiGraphics, ci);
		BossHealthOverlayInternal.Render.LocalVars.store(events);
		Internal.Callbacks.invoke(BossHealthOverlayInternal.Render.Callbacks.after_invoke);
	}

	@Inject(method = "render", at = @At(value = "RETURN", shift = At.Shift.BEFORE), cancellable = true)
	public void before_return(GuiGraphics guiGraphics, CallbackInfo ci) {
		BossHealthOverlayInternal.Render.Args.store((BossHealthOverlay) (Object) this, guiGraphics, ci);
		Internal.Callbacks.invoke(BossHealthOverlayInternal.Render.Callbacks.before_return);
	}

}
