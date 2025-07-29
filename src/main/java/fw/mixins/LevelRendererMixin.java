package fw.mixins;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

@Mixin(targets = { "net.minecraft.client.renderer.LevelRenderer" })
public abstract class LevelRendererMixin implements ResourceManagerReloadListener, AutoCloseable {
	public static interface LevelRendererRenderFunc {
		public default void renderLevel(LevelRenderer levelRenderer, PoseStack poseStack, float partialTick, long l, boolean renderOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {

		}
	}

	public static class Callbacks {
		public static ArrayList<LevelRendererRenderFunc> after_renderLevel_Funcs = new ArrayList<>();

		public static void add_after_renderLevel_Func(LevelRendererRenderFunc func) {
			after_renderLevel_Funcs.add(func);
		}

		public static ArrayList<LevelRendererRenderFunc> before_renderLevel_1st_checkPoseStack_Funcs = new ArrayList<>();

		public static void add_before_renderLevel_1st_checkPoseStack_Func(LevelRendererRenderFunc func) {
			before_renderLevel_1st_checkPoseStack_Funcs.add(func);
		}

		public static ArrayList<LevelRendererRenderFunc> before_renderLevel_3rd_applyModelViewMatrix_Funcs = new ArrayList<>();

		public static void add_before_renderLevel_3rd_applyModelViewMatrix_Func(LevelRendererRenderFunc func) {
			before_renderLevel_3rd_applyModelViewMatrix_Funcs.add(func);
		}

		public static ArrayList<LevelRendererRenderFunc> before_renderLevel_RenderSystem_disableBlend_Funcs = new ArrayList<>();

		public static void add_before_renderLevel_RenderSystem_disableBlend_Func(LevelRendererRenderFunc func) {
			before_renderLevel_RenderSystem_disableBlend_Funcs.add(func);
		}
	}

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private ClientLevel level;

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V", shift = Shift.BEFORE), cancellable = true)
	private void rsky(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		System.err.println("Mixin get " + level.getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), 0));
	}
}
