package lepus.mc.client.render.sky;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import lepus.mc.client.render.VanillaRenderable;
import lepus.mc.client.render.SceneGraphNode;
import lepus.mc.client.render.VertexBufferManipulator;
import lepus.mc.client.render.sky.NearEarthObject.Pos;
import lepus.mc.mixins.internal.LevelRendererInternal;
import lepus.mc.resources.ResourceLocations;
import lyra.object.ObjectManipulator;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Sky {
	public static final String SUN_LOCATION = "SUN_LOCATION";

	public static ResourceLocation getSunTexture() {
		return (ResourceLocation) ObjectManipulator.getStaticObject(LevelRenderer.class, SUN_LOCATION);
	}

	public static void setSunTexture(String namespacedLoc) {
		ObjectManipulator.setStaticObject(LevelRenderer.class, SUN_LOCATION, ResourceLocations.build(namespacedLoc));
	}

	public static final String MOON_LOCATION = "MOON_LOCATION";

	public static ResourceLocation getMoonTexture() {
		return (ResourceLocation) ObjectManipulator.getStaticObject(LevelRenderer.class, MOON_LOCATION);
	}

	public static void setMoonTexture(String namespacedLoc) {
		ObjectManipulator.setStaticObject(LevelRenderer.class, MOON_LOCATION, ResourceLocations.build(namespacedLoc));
	}

	public static final String CLOUDS_LOCATION = "CLOUDS_LOCATION";

	public static ResourceLocation getCloudsTexture() {
		return (ResourceLocation) ObjectManipulator.getStaticObject(LevelRenderer.class, CLOUDS_LOCATION);
	}

	public static void setCloudsTexture(String namespacedLoc) {
		ObjectManipulator.setStaticObject(LevelRenderer.class, CLOUDS_LOCATION, ResourceLocations.build(namespacedLoc));
	}

	public static final String END_SKY_LOCATION = "END_SKY_LOCATION";

	public static ResourceLocation getEndSkyTexture() {
		return (ResourceLocation) ObjectManipulator.getStaticObject(LevelRenderer.class, END_SKY_LOCATION);
	}

	public static void setEndSkyTexture(String namespacedLoc) {
		ObjectManipulator.setStaticObject(LevelRenderer.class, END_SKY_LOCATION, ResourceLocations.build(namespacedLoc));
	}

	public static final String FORCEFIELD_LOCATION = "FORCEFIELD_LOCATION";

	public static ResourceLocation getForceFieldTexture() {
		return (ResourceLocation) ObjectManipulator.getStaticObject(LevelRenderer.class, FORCEFIELD_LOCATION);
	}

	public static void setForceFieldTexture(String namespacedLoc) {
		ObjectManipulator.setStaticObject(LevelRenderer.class, FORCEFIELD_LOCATION, ResourceLocations.build(namespacedLoc));
	}

	public static VertexBufferManipulator.NormalizedColorResolver celestialColor = VertexBufferManipulator.NormalizedColorResolver.NONE;

	/**
	 * 设置太阳、月亮的着色器颜色，即各个通道纹理颜色的贡献值
	 * 
	 * @param resolver
	 */
	public static final void setCelestialColorResolver(VertexBufferManipulator.NormalizedColorResolver resolver) {
		celestialColor = resolver;
	}

	public static final void setFixedCelestialColor(float red, float green, float blue, float alpha) {
		setCelestialColorResolver(VertexBufferManipulator.NormalizedColorResolver.fixed(red, green, blue, alpha));
	}

	public static final void setFixedCelestialColor(float red, float green, float blue) {
		setCelestialColorResolver(VertexBufferManipulator.NormalizedColorResolver.fixedRGB(red, green, blue));
	}

	public static final void setFixedCelestialAlpha(float alpha) {
		setCelestialColorResolver(VertexBufferManipulator.NormalizedColorResolver.fixedA(alpha));
	}

	/**
	 * 天空渲染高度为16，天体渲染高度最好只略低于16。<br>
	 * 即视觉上天体透视大小相等时，要选择位置高体积大，而不能选位置低体积小，否则会受到严重的玩家走动镜头晃动影响
	 */
	public static final SceneGraphNode CELESTIAL_BODYS = SceneGraphNode.createSceneGraph();

	private static final SceneGraphNode SKY_BG = SceneGraphNode.createSceneGraph();

	static {
		LevelRendererInternal.RenderLevel.Callbacks.addAfter_popPush_sky(
				(LevelRenderer this_, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) -> {
					RenderSystem.disableBlend();
					RenderSystem.depthMask(false);
					SKY_BG.render(frustumMatrix, projectionMatrix);
					RenderSystem.depthMask(true);
					RenderSystem.enableBlend();
					CELESTIAL_BODYS.render(frustumMatrix, projectionMatrix);
				});
	}

	public static SceneGraphNode renderSkyBackground(String path, VanillaRenderable sky) {
		return SKY_BG.createRenderableNode(path, sky);
	}

	/**
	 * 在天空中渲染物体
	 * 
	 * @param path
	 * @param obj
	 */
	public static SceneGraphNode render(String path, VanillaRenderable obj) {
		return CELESTIAL_BODYS.createRenderableNode(path, obj);
	}

	public static SceneGraphNode renderNearEarthObject(String path, VanillaRenderable obj, NearEarthObject.Orbit orbit) {
		SceneGraphNode node = render(path, obj);
		NearEarthObject.bind(node, orbit);
		return node;
	}

	public static SceneGraphNode renderFixedNearEarthObject(String path, VanillaRenderable obj, float object_x, float object_y, float object_z) {
		SceneGraphNode node = render(path, obj);
		NearEarthObject.bind(node, object_x, object_y, object_z);
		return node;
	}

	public static SceneGraphNode renderFixedNearEarthObject(String path, VanillaRenderable obj, float object_x, float object_y, float object_z, Pos.ViewHeight final_view_height) {
		SceneGraphNode node = render(path, obj);
		NearEarthObject.bind(node, object_x, object_y, object_z, final_view_height);
		return node;
	}

	/**
	 * 渲染圆形轨道近地物体
	 * 
	 * @param path
	 * @param obj
	 * @param center_x
	 * @param center_z
	 * @param radius
	 * @param view_height
	 * @param angular_speed
	 * @param initial_phase
	 * @return
	 */
	public static SceneGraphNode renderCircleOrbitNearEarthObject(String path, VanillaRenderable obj, float center_x, float center_z, float radius, float view_height, float angular_speed, float initial_phase) {
		SceneGraphNode node = render(path, obj);
		NearEarthObject.bind(node, NearEarthObject.Orbit.circle(center_x, center_z, radius, view_height, angular_speed, initial_phase));
		return node;
	}

	public static SceneGraphNode renderCircleOrbitNearEarthObject(String path, VanillaRenderable obj, float center_x, float center_z, float radius, float view_height, float angular_speed) {
		SceneGraphNode node = render(path, obj);
		NearEarthObject.bind(node, NearEarthObject.Orbit.circle(center_x, center_z, radius, view_height, angular_speed));
		return node;
	}

}
