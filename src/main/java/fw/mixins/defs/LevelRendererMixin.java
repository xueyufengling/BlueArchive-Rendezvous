package fw.mixins.defs;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import fw.client.render.sky.CloudColor;
import fw.mixins.internal.LevelRendererInternal;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.phys.Vec3;

@Mixin(targets = { "net.minecraft.client.renderer.LevelRenderer" })
public abstract class LevelRendererMixin implements ResourceManagerReloadListener, AutoCloseable {
	public static interface LevelRendererCallback {
		public default void renderLevel(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {

		}
	}

	public static class Callbacks {
		public static ArrayList<LevelRendererCallback> after_renderLevel_Funcs = new ArrayList<>();

		public static void add_after_renderLevel_Func(LevelRendererCallback func) {
			after_renderLevel_Funcs.add(func);
		}

		public static ArrayList<LevelRendererCallback> before_renderLevel_1st_checkPoseStack_Funcs = new ArrayList<>();

		public static void add_before_renderLevel_1st_checkPoseStack_Func(LevelRendererCallback func) {
			before_renderLevel_1st_checkPoseStack_Funcs.add(func);
		}

		public static ArrayList<LevelRendererCallback> before_renderLevel_3rd_applyModelViewMatrix_Funcs = new ArrayList<>();

		public static void add_before_renderLevel_3rd_applyModelViewMatrix_Func(LevelRendererCallback func) {
			before_renderLevel_3rd_applyModelViewMatrix_Funcs.add(func);
		}

		public static ArrayList<LevelRendererCallback> before_renderLevel_RenderSystem_disableBlend_Funcs = new ArrayList<>();

		public static void add_before_renderLevel_RenderSystem_disableBlend_Func(LevelRendererCallback func) {
			before_renderLevel_RenderSystem_disableBlend_Funcs.add(func);
		}
	}

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private ClientLevel level;

	@Inject(method = "renderLevel", at = @At(value = "HEAD"))
	private void initLocalVars(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		LevelRendererInternal.Args.store(deltaTracker, renderBlockOutline, camera, gameRenderer, lightTexture, frustumMatrix, projectionMatrix, ci);
		LevelRendererInternal.LocalVars.store(level);
	}

	@WrapOperation(method = "renderClouds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getCloudColor(F)Lnet/minecraft/world/phys/Vec3;"))
	private Vec3 renderClouds_modifyAfterGetCloudColor(ClientLevel level, float partialTick, Operation<Vec3> orig) {
		Vec3 o = orig.call(level, partialTick);
		return CloudColor.resolve(o, level, partialTick, LevelRendererInternal.LocalVars.camPosBiome, level.getDayTime());
	}

}
