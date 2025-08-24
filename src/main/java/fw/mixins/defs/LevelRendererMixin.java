package fw.mixins.defs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
import com.mojang.blaze3d.vertex.MeshData;

import fw.client.render.gl.PixelsBuffer;
import fw.client.render.gl.VertexBufferManipulator;
import fw.client.render.level.LevelColor;
import fw.client.render.sky.Sky;
import fw.client.render.sky.SkyColor;
import fw.client.render.sky.WeatherEffect;
import fw.common.ColorRGBA;
import fw.mixins.internal.Internal;
import fw.mixins.internal.LevelRendererInternal;
import fw.mixins.internal.TargetDescriptors;
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
		LevelRendererInternal.RenderLevel.LocalVars.store(minecraft, level, deltaTracker);
	}

	@WrapOperation(method = "renderClouds", at = @At(value = "INVOKE", target = TargetDescriptors.LClientLevel.getCloudColor))
	private Vec3 renderClouds_modifyAfterGetCloudColor(ClientLevel level, float partialTick, Operation<Vec3> op) {
		Vec3 orig = op.call(level, partialTick);
		return SkyColor.resolveCloud(ColorRGBA.of(orig), level, partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, LevelRendererInternal.RenderLevel.LocalVars.camPos, LevelRendererInternal.RenderLevel.LocalVars.dayTime).vec3();
	}

	@Inject(method = "renderLevel", at = @At(value = "RETURN", shift = Shift.BEFORE), cancellable = true)
	private void renderLevel_before_return(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		LevelRendererInternal.RenderLevel.Args.store((LevelRenderer) (Object) this, deltaTracker, renderBlockOutline, camera, gameRenderer, lightTexture, frustumMatrix, projectionMatrix, ci);
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_return);
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = TargetDescriptors.LLevelRenderer.checkPoseStack, ordinal = 0, shift = Shift.BEFORE), cancellable = true)
	private void renderLevel_before_1st_checkPoseStack(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		LevelRendererInternal.RenderLevel.Args.store((LevelRenderer) (Object) this, deltaTracker, renderBlockOutline, camera, gameRenderer, lightTexture, frustumMatrix, projectionMatrix, ci);
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_1st_checkPoseStack);
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = TargetDescriptors.LRenderSystem.disableBlend, shift = Shift.BEFORE), cancellable = true)
	private void renderLevel_before_RenderSystem_disableBlend(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_RenderSystem_disableBlend);
	}

	/**
	 * 在Iris渲染天空阶段之后
	 * 
	 * @param frustumMatrix
	 * @param projectionMatrix
	 * @param partialTick
	 * @param camera
	 * @param isFoggy
	 * @param skyFogSetup
	 * @param ci
	 */
	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = TargetDescriptors.LProfilerFiller.popPush, ordinal = 6, shift = Shift.BEFORE), cancellable = true)
	private void renderLevel_before_popPush_fog(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_popPush_fog);
		PixelsBuffer pixels = PixelsBuffer.main();
		if (pixels.map()) {
			int w = pixels.width();
			int h = pixels.height();
			for (int x = 0; x < w; ++x)
				for (int y = 0; y < h; ++y) {
					pixels.setColor(x, y, ColorRGBA.of(1f, 1f, 1f, 0.5f));
				}
			pixels.unmap();
			pixels.write();
		}
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = TargetDescriptors.LLevelRenderer.renderDebug, shift = Shift.BEFORE), cancellable = true)
	private void renderLevel_before_renderDebug(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		Internal.Callbacks.invoke(LevelRendererInternal.RenderLevel.Callbacks.before_renderDebug);
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

	@Inject(method = "renderSky", at = @At(value = "INVOKE", target = TargetDescriptors.LPoseStack.popPose, ordinal = 1, shift = Shift.AFTER), cancellable = true)
	private void renderSky_after_2nd_popPose(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
		Internal.Callbacks.invoke(LevelRendererInternal.RenderSky.Callbacks.after_2nd_popPose);
	}

	@Inject(method = "renderSky", at = @At(value = "INVOKE", target = TargetDescriptors.LVertexBuffer.unbind, ordinal = 0, shift = Shift.AFTER), cancellable = true)
	private void renderSky_after_skyBuffer_draw(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
		Internal.Callbacks.invoke(LevelRendererInternal.RenderSky.Callbacks.after_skyBuffer_draw);
	}

	@Inject(method = "renderSky", at = @At(value = "INVOKE", target = TargetDescriptors.LVertexBuffer.unbind, ordinal = 2, shift = Shift.AFTER), cancellable = true)
	private void renderSky_after_darkBuffer_draw(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
		Internal.Callbacks.invoke(LevelRendererInternal.RenderSky.Callbacks.after_darkBuffer_draw);
	}

	@WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = TargetDescriptors.LRenderSystem.setShaderColor))
	private void renderSky_modifyAfterGetSkyColor(float r, float g, float b, float a, Operation<Void> op) {
		ColorRGBA final_color = SkyColor.resolveSky(ColorRGBA.of(r, g, b, a), level, LevelRendererInternal.RenderLevel.LocalVars.partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, LevelRendererInternal.RenderLevel.LocalVars.camPos, LevelRendererInternal.RenderLevel.LocalVars.dayTime);
		op.call(final_color.r, final_color.g, final_color.b, final_color.a);
	}

	@WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = TargetDescriptors.LBufferUploader.drawWithShader, ordinal = 0))
	private void renderSky_modifyVertexBuffer(MeshData mesh, Operation<Void> orig) {
		LevelColor.modifyVertexColor(mesh, SkyColor.sunriseColor);
		orig.call(mesh);
	}

	/**
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha 与下雨有关
	 * @param orig
	 */
	@WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = TargetDescriptors.LRenderSystem.setShaderColor, ordinal = 2))
	private void renderSky_modifySunMoonShaderColor(float red, float green, float blue, float alpha, Operation<Void> orig) {
		float[] color = Sky.celestialColor.color(red, green, blue, alpha);
		orig.call(color[0], color[1], color[2], color[3]);
	}

	/**
	 * 下雨渲染
	 * 
	 * @param mesh
	 * @param orig
	 */
	@WrapOperation(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = TargetDescriptors.LBufferUploader.drawWithShader, ordinal = 0))
	private void renderSnowAndRain_modifyRainVertexBuffer(MeshData mesh, Operation<Void> orig) {
		VertexBufferManipulator.modifyVertexColor(mesh, WeatherEffect.rainColorResolver);
		orig.call(mesh);
	}

	@WrapOperation(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = TargetDescriptors.LBufferUploader.drawWithShader, ordinal = 1))
	private void renderSnowAndRain_modifySnowVertexBuffer(MeshData mesh, Operation<Void> orig) {
		VertexBufferManipulator.modifyVertexColor(mesh, WeatherEffect.snowColorResolver);
		orig.call(mesh);
	}

	@WrapOperation(method = "renderSnowAndRain", at = @At(value = "INVOKE", target = TargetDescriptors.LBufferUploader.drawWithShader, ordinal = 2))
	private void renderSnowAndRain_modifyVertexBuffer(MeshData mesh, Operation<Void> orig) {
		VertexBufferManipulator.modifyVertexColor(mesh, WeatherEffect.rainColorResolver);
		orig.call(mesh);
	}
}
