package lepus.mc.mixins.internal;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;

public class BossHealthOverlayInternal {

	public static class Render {

		public static class Args {
			public static BossHealthOverlay this_;
			public static GuiGraphics guiGraphics;
			public static CallbackInfo ci;

			public static final void store(BossHealthOverlay this_, GuiGraphics guiGraphics, CallbackInfo ci) {
				// 方法参数
				Args.this_ = this_;
				Args.guiGraphics = guiGraphics;
				Args.ci = ci;
			}
		}

		public static class LocalVars {
			public static Map<UUID, LerpingBossEvent> events;

			public static final void store(Map<UUID, LerpingBossEvent> events) {
				LocalVars.events = events;
			}
		}

		@FunctionalInterface
		public static interface Callback extends Internal.Callback {
			public void render(BossHealthOverlay this_, GuiGraphics guiGraphics, CallbackInfo ci);

			public default void execute() {
				render(Args.this_, Args.guiGraphics, Args.ci);
			}
		}

		public static class Callbacks {
			public static ArrayList<Callback> after_invoke = new ArrayList<>();

			public static void addInvokeCallback(Callback func) {
				after_invoke.add(func);
			}

			public static ArrayList<Callback> before_return = new ArrayList<>();

			public static void addBeforeReturnCallback(Callback func) {
				before_return.add(func);
			}
		}
	}
}
