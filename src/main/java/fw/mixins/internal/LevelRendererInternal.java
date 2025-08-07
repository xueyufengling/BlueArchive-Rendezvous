package fw.mixins.internal;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public class LevelRendererInternal {

	public static class RenderLevel {

		public static class Args {
			public static LevelRenderer this_;
			public static DeltaTracker deltaTracker;
			public static boolean renderBlockOutline;
			public static Camera camera;
			public static GameRenderer gameRenderer;
			public static LightTexture lightTexture;
			public static Matrix4f frustumMatrix;
			public static Matrix4f projectionMatrix;
			public static CallbackInfo ci;

			public static final void store(LevelRenderer this_, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
				// 方法参数
				Args.this_ = this_;
				Args.deltaTracker = deltaTracker;
				Args.renderBlockOutline = renderBlockOutline;
				Args.camera = camera;
				Args.gameRenderer = gameRenderer;
				Args.lightTexture = lightTexture;
				Args.frustumMatrix = frustumMatrix;
				Args.projectionMatrix = projectionMatrix;
				Args.ci = ci;
			}
		}

		public static class LocalVars {
			public static Vec3 camPos;
			public static Holder<Biome> camPosBiome;

			public static final void store(ClientLevel level) {
				// 局部变量
				Vec3 pos = Args.gameRenderer.getMainCamera().getPosition();
				LocalVars.camPos = pos;
				LocalVars.camPosBiome = level.getBiomeManager().getNoiseBiomeAtPosition(pos.x, pos.y, pos.z);
			}
		}

		@FunctionalInterface
		public static interface Callback extends Internal.Callback {
			public void renderLevel(LevelRenderer this_, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci);

			public default void execute() {
				renderLevel(Args.this_, Args.deltaTracker, Args.renderBlockOutline, Args.camera, Args.gameRenderer, Args.lightTexture, Args.frustumMatrix, Args.projectionMatrix, Args.ci);
			}
		}

		public static class Callbacks {
			public static ArrayList<Callback> before_return = new ArrayList<>();

			public static void addBeforeReturnCallback(Callback func) {
				before_return.add(func);
			}

			public static ArrayList<Callback> before_1st_checkPoseStack = new ArrayList<>();

			public static void addBefore1stCheckPoseStackCallback(Callback func) {
				before_1st_checkPoseStack.add(func);
			}

			public static ArrayList<Callback> before_RenderSystem_disableBlend = new ArrayList<>();

			public static void addBeforeRenderSystemDisableBlendCallback(Callback func) {
				before_RenderSystem_disableBlend.add(func);
			}
		}
	}
}
