package fw.mixins.defs;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import fw.client.render.sky.CloudColor;
import fw.mixins.internal.Internal;
import fw.mixins.internal.LevelRendererInternal;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.phys.Vec3;

@Mixin(targets = { "net.minecraft.client.renderer.LevelRenderer" })
public abstract class LevelRendererMixin implements ResourceManagerReloadListener, AutoCloseable {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private ClientLevel level;

	@Inject(method = "renderLevel", at = @At(value = "HEAD"))
	private void initParams(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		LevelRendererInternal.RenderLevel.Args.store((LevelRenderer) (Object) this, deltaTracker, renderBlockOutline, camera, gameRenderer, lightTexture, frustumMatrix, projectionMatrix, ci);
		LevelRendererInternal.RenderLevel.LocalVars.store(level);
	}

	@WrapOperation(method = "renderClouds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getCloudColor(F)Lnet/minecraft/world/phys/Vec3;"))
	private Vec3 renderClouds_modifyAfterGetCloudColor(ClientLevel level, float partialTick, Operation<Vec3> orig) {
		Vec3 o = orig.call(level, partialTick);
		return CloudColor.resolve(o, level, partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, level.getDayTime());
	}

	@Inject(method = "renderLevel", at = @At(value = "RETURN", shift = Shift.BEFORE), cancellable = true)
	private void before_return(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		LevelRendererInternal.RenderLevel.Args.store((LevelRenderer) (Object) this, deltaTracker, renderBlockOutline, camera, gameRenderer, lightTexture, frustumMatrix, projectionMatrix, ci);
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_return);
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0, shift = Shift.BEFORE), cancellable = true)
	private void before_1st_checkPoseStack(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		LevelRendererInternal.RenderLevel.Args.store((LevelRenderer) (Object) this, deltaTracker, renderBlockOutline, camera, gameRenderer, lightTexture, frustumMatrix, projectionMatrix, ci);
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_1st_checkPoseStack);
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V", shift = Shift.BEFORE), cancellable = true)
	private void before_RenderSystem_disableBlend(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_RenderSystem_disableBlend);
	}
}
