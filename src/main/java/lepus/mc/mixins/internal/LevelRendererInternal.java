package lepus.mc.mixins.internal;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
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
			public static Minecraft minecraft;
			public static ClientLevel level;
			public static Vec3 camPos;
			public static Holder<Biome> camPosBiome;
			public static float partialTick;
			/**
			 * 存档创建起的总时间
			 */
			public static float worldTime;

			/**
			 * 一天的时间
			 */
			public static float dayTime;

			public static final void store(Minecraft minecraft, ClientLevel level, DeltaTracker deltaTracker) {
				LocalVars.minecraft = minecraft;
				LocalVars.level = level;
				// 局部变量
				Vec3 pos = Args.gameRenderer.getMainCamera().getPosition();
				LocalVars.camPos = pos;
				LocalVars.camPosBiome = level.getBiomeManager().getNoiseBiomeAtPosition(pos.x, pos.y, pos.z);
				LocalVars.partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
				LocalVars.worldTime = level.getGameTime() + partialTick;
				LocalVars.dayTime = level.getDayTime() + partialTick;
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

			public static ArrayList<Callback> before_popPush_fog = new ArrayList<>();

			public static void addBefore_popPush_fog(Callback func) {
				before_popPush_fog.add(func);
			}

			public static ArrayList<Callback> after_popPush_sky = new ArrayList<>();

			public static void addAfter_popPush_sky(Callback func) {
				after_popPush_sky.add(func);
			}

			public static int c = 0;
		}
	}

	public static class RenderSky {

		public static class Args {
			public static LevelRenderer this_;
			public static Matrix4f frustumMatrix;
			public static Matrix4f projectionMatrix;
			public static float partialTick;
			public static Camera camera;
			public static boolean isFoggy;
			public static Runnable skyFogSetup;
			public static CallbackInfo ci;

			public static final void store(LevelRenderer this_, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
				// 方法参数
				Args.this_ = this_;
				Args.frustumMatrix = frustumMatrix;
				Args.projectionMatrix = projectionMatrix;
				Args.partialTick = partialTick;
				Args.camera = camera;
				Args.isFoggy = isFoggy;
				Args.skyFogSetup = skyFogSetup;
				Args.ci = ci;
			}
		}

		@FunctionalInterface
		public static interface Callback extends Internal.Callback {
			public void renderSky(LevelRenderer this_, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci);

			public default void execute() {
				renderSky(Args.this_, Args.frustumMatrix, Args.projectionMatrix, Args.partialTick, Args.camera, Args.isFoggy, Args.skyFogSetup, Args.ci);
			}
		}

		public static class Callbacks {
			public static ArrayList<Callback> after_skyBuffer_draw = new ArrayList<>();

			/**
			 * 装了Iris后整个renderSky函数将由光影着色器渲染，而现有的光影几乎都会丢弃OpenGL管线传入的颜色而使用其内部硬编码颜色计算，在此阶段内的渲染将可能失效。
			 */
			public static void addAfter_skyBuffer_draw(Callback func) {
				after_skyBuffer_draw.add(func);
			}

			public static ArrayList<Callback> after_darkBuffer_draw = new ArrayList<>();

			public static void addAfter_darkBuffer_draw(Callback func) {
				after_darkBuffer_draw.add(func);
			}
		}
	}
}
