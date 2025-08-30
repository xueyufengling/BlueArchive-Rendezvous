package lepus.mc.client.render.sky;

import lepus.graphics.ColorRGBA;
import lepus.math.interpolation.ColorLinearInterpolation;
import lepus.mc.client.render.level.LevelColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

/**
 * 不支持Iris光影包，仅支持原版渲染
 */
@Deprecated
public class SkyColor {
	/**
	 * 修改最终的实际渲染颜色
	 */
	private static final LevelColor skyColor = new LevelColor();

	public static final LevelColor.LevelBasedResolver skyLevelBasedResolver = new LevelColor.LevelBasedResolver();
	public static final LevelColor.BiomeBasedResolver skyBiomeBasedResolver = new LevelColor.BiomeBasedResolver();

	static {
		skyColor.addResolver(skyLevelBasedResolver);
		skyColor.addResolver(skyBiomeBasedResolver);
	}

	/**
	 * @param orig        天空颜色与群系天空颜色混合后的颜色
	 * @param level
	 * @param partialTick
	 * @param biome
	 * @param camPos
	 * @param time
	 * @return 最终实际渲染颜色
	 */
	public static ColorRGBA resolveSky(ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) {
		return skyColor.resolve(orig, level, partialTick, biome, camPos, time);
	}

	public static final void setLevelSkyColorResolver(String levelKey, ColorLinearInterpolation skyColor) {
		skyLevelBasedResolver.addEntry(levelKey, LevelColor.lerp(skyColor));
	}

	public static final void setLevelSkyColorResolver(String levelKey, ColorLinearInterpolation skyColor, float ratio) {
		skyLevelBasedResolver.addEntry(levelKey, LevelColor.blendLerp(skyColor, ratio));
	}

	public static final void setLevelSkyColorResolver(String levelKey, ColorLinearInterpolation skyColor, ColorLinearInterpolation weight) {
		skyLevelBasedResolver.addEntry(levelKey, LevelColor.blendLerp(skyColor, weight));
	}

	public static final void setLevelFixedSkyColor(String levelKey, int r, int g, int b) {
		skyLevelBasedResolver.addEntry(levelKey, LevelColor.fixed(r, g, b));
	}

	public static final void setBiomeSkyColorResolver(String biomeKey, ColorLinearInterpolation skyColor) {
		skyBiomeBasedResolver.addEntry(biomeKey, LevelColor.lerp(skyColor));
	}

	public static final void setBiomeSkyColorResolver(String levelKey, ColorLinearInterpolation skyColor, float ratio) {
		skyBiomeBasedResolver.addEntry(levelKey, LevelColor.blendLerp(skyColor, ratio));
	}

	public static final void setBiomeSkyColorResolver(String levelKey, ColorLinearInterpolation skyColor, ColorLinearInterpolation weight) {
		skyBiomeBasedResolver.addEntry(levelKey, LevelColor.blendLerp(skyColor, weight));
	}

	/**
	 * 为任何时候设置一个固定天空最终颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public static final void setFixedSkyColor(int r, int g, int b) {
		skyColor.addResolver(LevelColor.fixed(r, g, b));
	}

	/**
	 * 修改最终渲染颜色
	 */
	private static final LevelColor cloudColor = new LevelColor();

	public static final LevelColor.LevelBasedResolver cloudLevelBasedResolver = new LevelColor.LevelBasedResolver();
	public static final LevelColor.BiomeBasedResolver cloudBiomeBasedResolver = new LevelColor.BiomeBasedResolver();

	static {
		cloudColor.addResolver(cloudLevelBasedResolver);
		cloudColor.addResolver(cloudBiomeBasedResolver);
	}

	public static ColorRGBA resolveCloud(ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) {
		return cloudColor.resolve(orig, level, partialTick, biome, camPos, time);
	}

	public static final void setLevelCloudColorResolver(String levelKey, ColorLinearInterpolation cloudColor) {
		cloudLevelBasedResolver.addEntry(levelKey, LevelColor.lerp(cloudColor));
	}

	public static final void setLevelCloudColorResolver(String levelKey, ColorLinearInterpolation skyColor, float ratio) {
		cloudLevelBasedResolver.addEntry(levelKey, LevelColor.blendLerp(skyColor, ratio));
	}

	public static final void setLevelCloudColorResolver(String levelKey, ColorLinearInterpolation skyColor, ColorLinearInterpolation weight) {
		cloudLevelBasedResolver.addEntry(levelKey, LevelColor.blendLerp(skyColor, weight));
	}

	public static final void setLevelFixedCloudColor(String levelKey, int r, int g, int b) {
		cloudLevelBasedResolver.addEntry(levelKey, LevelColor.fixed(r, g, b));
	}

	public static final void setBiomeCloudColorResolver(String biomeKey, ColorLinearInterpolation cloudColor) {
		cloudBiomeBasedResolver.addEntry(biomeKey, LevelColor.lerp(cloudColor));
	}

	public static final void setBiomeCloudColorResolver(String levelKey, ColorLinearInterpolation skyColor, float ratio) {
		cloudBiomeBasedResolver.addEntry(levelKey, LevelColor.blendLerp(skyColor, ratio));
	}

	public static final void setBiomeCloudColorResolver(String levelKey, ColorLinearInterpolation skyColor, ColorLinearInterpolation weight) {
		cloudBiomeBasedResolver.addEntry(levelKey, LevelColor.blendLerp(skyColor, weight));
	}

	/**
	 * 为任何时候指定一个固定云颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public static final void setFixedCloudColor(int r, int g, int b) {
		cloudColor.addResolver(LevelColor.fixed(r, g, b));
	}

	/**
	 * 日出颜色仅在日出日落时有效，该颜色将由下至上逐渐覆盖雾的颜色，日出日落结束后又立即消失
	 */
	public static final LevelColor sunriseColor = new LevelColor();

	public static final LevelColor.LevelBasedResolver sunriseLevelBasedResolver = new LevelColor.LevelBasedResolver();
	public static final LevelColor.BiomeBasedResolver sunriseBiomeBasedResolver = new LevelColor.BiomeBasedResolver();

	static {
		sunriseColor.addResolver(sunriseLevelBasedResolver);
		sunriseColor.addResolver(sunriseBiomeBasedResolver);
	}

	/**
	 * 为任何时候指定一个地平线（日出）颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public static final void setFixedSunriseColor(int r, int g, int b) {
		sunriseColor.addResolver(LevelColor.fixed(r, g, b));
	}
}