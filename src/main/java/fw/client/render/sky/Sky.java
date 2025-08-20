package fw.client.render.sky;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

	public static final String RAIN_LOCATION = "RAIN_LOCATION";

	public static ResourceLocation getRainTexture() {
		return (ResourceLocation) ObjectManipulator.getStaticObject(LevelRenderer.class, RAIN_LOCATION);
	}

	public static void setRainTexture(String namespacedLoc) {
		ObjectManipulator.setStaticObject(LevelRenderer.class, RAIN_LOCATION, ResourceLocations.build(namespacedLoc));
	}

	public static final String SNOW_LOCATION = "SNOW_LOCATION";

	public static ResourceLocation getSnowTexture() {
		return (ResourceLocation) ObjectManipulator.getStaticObject(LevelRenderer.class, SNOW_LOCATION);
	}

	public static void setSnowTexture(String namespacedLoc) {
		ObjectManipulator.setStaticObject(LevelRenderer.class, SNOW_LOCATION, ResourceLocations.build(namespacedLoc));
	}

	@FunctionalInterface
	public static interface CelestialColorResolver {
		public float[] resolve(float... colorRGBA);

		public static final CelestialColorResolver NONE = (float... colorRGBA) -> colorRGBA;

		/**
		 * RGBA均设置成固定颜色
		 * 
		 * @param red
		 * @param green
		 * @param blue
		 * @param alpha
		 * @return
		 */
		public static CelestialColorResolver fixed(float red, float green, float blue, float alpha) {
			return (float... colorRGBA) -> new float[] { red, green, blue, alpha };
		}

		/**
		 * RGB重新设置成固定颜色，alpha保持不变
		 * 
		 * @param red
		 * @param green
		 * @param blue
		 * @return
		 */
		public static CelestialColorResolver fixed(float red, float green, float blue) {
			return (float... colorRGBA) -> new float[] { red, green, blue, colorRGBA[3] };
		}
	}

	public static CelestialColorResolver celestialColor = CelestialColorResolver.NONE;

	public static final void setCelestialColorResolver(CelestialColorResolver resolver) {
		celestialColor = resolver;
	}

	public static final void setFixedCelestialColor(float red, float green, float blue, float alpha) {
		setCelestialColorResolver(CelestialColorResolver.fixed(red, green, blue, alpha));
	}

	public static final void setFixedCelestialColor(float red, float green, float blue) {
		setCelestialColorResolver(CelestialColorResolver.fixed(red, green, blue));
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
	public static SceneGraphNode render(String name, RenderableObject obj) {
		return CELESTIAL_BODYS.attachRenderableNode(name, obj);
	}
}
