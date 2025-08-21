package fw.client.render.sky;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fw.client.render.VertexBufferManipulator;
import fw.client.render.scene.RenderableObject;
import fw.client.render.scene.SceneGraphNode;
import fw.mixins.internal.LevelRendererInternal;
import fw.resources.ResourceLocations;
import lyra.object.ObjectManipulator;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;

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

	public static final SceneGraphNode CELESTIAL_BODYS = SceneGraphNode.createSceneGraph();

	static {
		LevelRendererInternal.RenderSky.Callbacks.addAfter2nd_popPose(
				(LevelRenderer this_, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) -> {
					CELESTIAL_BODYS.render(frustumMatrix, projectionMatrix);
				});
	}

	/**
	 * 在天空中渲染物体
	 * 
	 * @param obj
	 */
	public static SceneGraphNode render(String path, RenderableObject obj) {
		return CELESTIAL_BODYS.createRenderableNode(path, obj);
	}

	public static SceneGraphNode renderNearEarthObject(String path, RenderableObject obj, NearEarthObject.Orbit orbit) {
		SceneGraphNode node = render(path, obj);
		NearEarthObject.bind(node, orbit);
		return node;
	}

	public static SceneGraphNode renderFixedNearEarthObject(String path, RenderableObject obj, float object_x, float object_z, float view_height) {
		SceneGraphNode node = render(path, obj);
		NearEarthObject.bind(node, object_x, object_z, view_height);
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
	public static SceneGraphNode renderCircleOrbitNearEarthObject(String path, RenderableObject obj, float center_x, float center_z, float radius, float view_height, float angular_speed, float initial_phase) {
		SceneGraphNode node = render(path, obj);
		NearEarthObject.bind(node, NearEarthObject.Orbit.circle(center_x, center_z, radius, view_height, angular_speed, initial_phase));
		return node;
	}

	public static SceneGraphNode renderCircleOrbitNearEarthObject(String path, RenderableObject obj, float center_x, float center_z, float radius, float view_height, float angular_speed) {
		SceneGraphNode node = render(path, obj);
		NearEarthObject.bind(node, NearEarthObject.Orbit.circle(center_x, center_z, radius, view_height, angular_speed));
		return node;
	}
}
