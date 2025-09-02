package lepus.mc.client.render.sky;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import lepus.graphics.ColorRGBA;
import lepus.mc.client.render.VanillaRenderable;
import lepus.mc.client.render.RenderableObjects;
import lepus.mc.client.render.renderable.Renderable;
import lepus.mc.mixins.internal.LevelRendererInternal;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;

/**
 * 一片天套一片天
 */
public class ScaledSky implements Renderable {
	private VanillaRenderable.Instance skyAboveHorizon;
	private VanillaRenderable.Instance skyBelowHorizon;

	private boolean visiable = true;

	private ScaledSky(float scale, float r, float g, float b, float a, ColorRGBA shaderColor) {
		this.skyAboveHorizon = RenderableObjects.scaledSkyAboveHorizonVanilla(scale, r, g, b, a).newInstance(shaderColor);
		this.skyBelowHorizon = RenderableObjects.scaledSkyBelowHorizonVanilla(scale, r, g, b, a).newInstance(shaderColor);
	}

	private static final ArrayList<ScaledSky> scaledSkys = new ArrayList<>();

	static {
		LevelRendererInternal.RenderLevel.Callbacks.addBefore_popPush_fog(
				(LevelRenderer this_, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) -> {
					RenderSystem.enableBlend();// 原版天空未开启颜色混合，由于这里要渲染多重天空，必须开启混合
					RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					ClientLevel level = LevelRendererInternal.RenderLevel.LocalVars.level;
					// 地平线以下渲染skyBelowHorizon
					double dh = LevelRendererInternal.RenderLevel.LocalVars.minecraft.player.getEyePosition(LevelRendererInternal.RenderLevel.LocalVars.partialTick).y - level.getLevelData().getHorizonHeight(level);
					for (ScaledSky sky : scaledSkys) {
						if (sky.visiable) {
							sky.skyAboveHorizon.render(frustumMatrix, projectionMatrix);
							if (dh < 0.0) {
								sky.skyBelowHorizon.render(frustumMatrix, projectionMatrix);
							}
						}
					}
					RenderSystem.setShader(GameRenderer::getPositionShader);
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					RenderSystem.depthMask(true);
				});
	}

	public static ScaledSky create(float scale, float r, float g, float b, float a, ColorRGBA shaderColor) {
		return new ScaledSky(scale, r, g, b, a, shaderColor);
	}

	public static ScaledSky create(float scale, float r, float g, float b, float a) {
		return create(scale, r, g, b, a, ColorRGBA.WHITE);
	}

	public static ScaledSky create(float scale) {
		return create(scale, 1, 1, 1, 1);
	}

	/**
	 * 启用该天空的渲染，是否真的渲染需要判断可见性visiable
	 */
	public void enableRendering() {
		if (!scaledSkys.contains(this))
			scaledSkys.add(this);
	}

	public ScaledSky setVisible(boolean visiable) {
		this.visiable = visiable;
		return this;
	}

	public boolean isVisible() {
		return visiable;
	}

	/**
	 * 设置着色器颜色，该颜色将点乘顶点颜色得到最终颜色
	 * 
	 * @param shaderColor
	 * @return
	 */
	public ScaledSky setShaderColor(ColorRGBA shaderColor) {
		this.skyAboveHorizon.setShaderUniformColor(shaderColor);
		this.skyBelowHorizon.setShaderUniformColor(shaderColor);
		return this;
	}

	public ColorRGBA getShaderColor() {
		return skyAboveHorizon.getShaderUniformColor();
	}
}
