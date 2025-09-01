package lepus.mc.client.render.sky;

import lepus.graphics.shader.ScreenShader;
import lepus.mc.client.render.VertexBufferManipulator;
import lepus.mc.ext.client.render.iris.IrisPostprocess;
import lepus.mc.resources.ResourceLocations;
import lyra.object.ObjectManipulator;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WeatherEffect {

	public static VertexBufferManipulator.ColorResolver rainColorResolver;

	public static VertexBufferManipulator.ColorResolver snowColorResolver;

	public static void setRainColorResolver(VertexBufferManipulator.ColorResolver resolver) {
		rainColorResolver = resolver;
	}

	/**
	 * 设置下雨颜色为固定颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public static void setRainColor(int r, int g, int b, int a) {
		setRainColorResolver(VertexBufferManipulator.ColorResolver.fixed(r, g, b, a));
	}

	public static void setRainColor(int r, int g, int b) {
		setRainColorResolver(VertexBufferManipulator.ColorResolver.fixedRGB(r, g, b));
	}

	public static void setRainAlpha(int a) {
		setRainColorResolver(VertexBufferManipulator.ColorResolver.fixedA(a));
	}

	public static void setSnowColorResolver(VertexBufferManipulator.ColorResolver resolver) {
		snowColorResolver = resolver;
	}

	public static void setSnowColor(int r, int g, int b, int a) {
		setSnowColorResolver(VertexBufferManipulator.ColorResolver.fixed(r, g, b, a));
	}

	public static void setSnowColor(int r, int g, int b) {
		setSnowColorResolver(VertexBufferManipulator.ColorResolver.fixedRGB(r, g, b));
	}

	public static void setSnowAlpha(int a) {
		setSnowColorResolver(VertexBufferManipulator.ColorResolver.fixedA(a));
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

	public static final void setWeatherPostprocessShader(ScreenShader sky_postprocess_shader) {
		IrisPostprocess.setPhasePostprocessShader("RAIN_SNOW", sky_postprocess_shader);
	}
}
