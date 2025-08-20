package fw.mixins.defs;

import java.util.Arrays;

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
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;

import fw.client.render.VertexBufferManipulator;
import fw.client.render.sky.CloudColor;
import fw.client.render.sky.Sky;
import fw.client.render.sky.SkyColor;
import fw.client.render.sky.WeatherEffect;
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
	private void renderLevel_initParams(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		LevelRendererInternal.RenderLevel.Args.store((LevelRenderer) (Object) this, deltaTracker, renderBlockOutline, camera, gameRenderer, lightTexture, frustumMatrix, projectionMatrix, ci);
		LevelRendererInternal.RenderLevel.LocalVars.store(level, deltaTracker);
	}

	@WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"))
	private Vec3 renderSky_modifyAfterGetSkyColor(ClientLevel level, Vec3 pos, float partialTick, Operation<Vec3> orig) {
		Vec3 o = orig.call(level, pos, partialTick);
		return SkyColor.resolve(o, level, partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, LevelRendererInternal.RenderLevel.LocalVars.camPos, level.getDayTime());
	}

	@WrapOperation(method = "renderClouds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getCloudColor(F)Lnet/minecraft/world/phys/Vec3;"))
	private Vec3 renderClouds_modifyAfterGetCloudColor(ClientLevel level, float partialTick, Operation<Vec3> orig) {
		Vec3 o = orig.call(level, partialTick);
		return CloudColor.resolve(o, level, partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, LevelRendererInternal.RenderLevel.LocalVars.camPos, level.getDayTime());
	}

	@Inject(method = "renderLevel", at = @At(value = "RETURN", shift = Shift.BEFORE), cancellable = true)
	private void renderLevel_before_return(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		LevelRendererInternal.RenderLevel.Args.store((LevelRenderer) (Object) this, deltaTracker, renderBlockOutline, camera, gameRenderer, lightTexture, frustumMatrix, projectionMatrix, ci);
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_return);
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0, shift = Shift.BEFORE), cancellable = true)
	private void renderLevel_before_1st_checkPoseStack(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		LevelRendererInternal.RenderLevel.Args.store((LevelRenderer) (Object) this, deltaTracker, renderBlockOutline, camera, gameRenderer, lightTexture, frustumMatrix, projectionMatrix, ci);
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_1st_checkPoseStack);
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V", shift = Shift.BEFORE), cancellable = true)
	private void renderLevel_before_RenderSystem_disableBlend(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_RenderSystem_disableBlend);
	}

	/**
	 * renderSky()
	 * 
	 * @param frustumMatrix
	 * @param projectionMatrix
	 * @param partialTick
	 * @param camera
	 * @param isFoggy
	 * @param skyFogSetup
	 * @param ci
	 */
	@Inject(method = "renderSky", at = @At(value = "HEAD"))
	private void renderSky_initParams(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
		LevelRendererInternal.RenderSky.Args.store((LevelRenderer) (Object) this, frustumMatrix, projectionMatrix, partialTick, camera, isFoggy, skyFogSetup, ci);
	}

	@Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", ordinal = 1, shift = Shift.AFTER), cancellable = true)
	private void renderSky_after_2nd_popPose(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
		Internal.Callbacks.invoke(LevelRendererInternal.RenderSky.Callbacks.after_2nd_popPose);
	}

	/**
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha 与下雨有关
	 * @param orig
	 */
	@WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 2))
	private void renderSky_modifySunMoonShaderColor(float red, float green, float blue, float alpha, Operation<Void> orig) {
		float[] color = Sky.celestialColor.resolve(red, green, blue, alpha);
		orig.call(color[0], color[1], color[2], color[3]);
	}

	@WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V", ordinal = 1))
	private void renderSky_modifySunVertexBuffer(MeshData mesh, Operation<Void> orig) {
		VertexBufferManipulator.modifyVertexColor(mesh, DefaultVertexFormat.POSITION_TEX, Sky.sunColor);
		orig.call(mesh);
	}

	/**
	 * 下雨渲染
	 * 
	 * @param mesh
	 * @param orig
	 */
	@WrapOperation(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V", ordinal = 0))
	private void renderSnowAndRain_modifyRainVertexBuffer(MeshData mesh, Operation<Void> orig) {
		VertexBufferManipulator.modifyVertexColor(mesh, DefaultVertexFormat.PARTICLE, WeatherEffect.rainColorResolver);
		orig.call(mesh);
	}

	@WrapOperation(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V", ordinal = 1))
	private void renderSnowAndRain_modifySnowVertexBuffer(MeshData mesh, Operation<Void> orig) {
		VertexBufferManipulator.modifyVertexColor(mesh, DefaultVertexFormat.PARTICLE, WeatherEffect.snowColorResolver);
		orig.call(mesh);
	}
}
