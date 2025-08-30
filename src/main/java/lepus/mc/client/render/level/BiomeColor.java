package lepus.mc.client.render.level;

import lepus.graphics.ColorRGBA;
import lepus.math.interpolation.ColorLinearInterpolation;
import lepus.mc.event.LevelTickTrigger;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BiomeColor {
	public static final ColorResolver VANILLA_GRASS_COLOR_RESOLVER;
	public static final ColorResolver VANILLA_FOLIAGE_COLOR_RESOLVER;
	public static final ColorResolver VANILLA_WATER_COLOR_RESOLVER;

	private static boolean dirtyFlag = false;

	private static ColorResolver GRASS_COLOR_RESOLVER;
	private static ColorResolver FOLIAGE_COLOR_RESOLVER;
	private static ColorResolver WATER_COLOR_RESOLVER;

	static {
		VANILLA_GRASS_COLOR_RESOLVER = BiomeColors.GRASS_COLOR_RESOLVER;
		VANILLA_FOLIAGE_COLOR_RESOLVER = BiomeColors.FOLIAGE_COLOR_RESOLVER;
		VANILLA_WATER_COLOR_RESOLVER = BiomeColors.WATER_COLOR_RESOLVER;
		GRASS_COLOR_RESOLVER = VANILLA_GRASS_COLOR_RESOLVER;
		FOLIAGE_COLOR_RESOLVER = VANILLA_FOLIAGE_COLOR_RESOLVER;
		WATER_COLOR_RESOLVER = VANILLA_WATER_COLOR_RESOLVER;
		LevelTickTrigger.PRE_CLIENT_LEVEL_TICK.addCallback((Level level) -> {
			if (dirtyFlag) {
				LevelCache.updateColorResolverCache((ClientLevel) level, GRASS_COLOR_RESOLVER, FOLIAGE_COLOR_RESOLVER, WATER_COLOR_RESOLVER);
				dirtyFlag = false;
			}
		});
	}

	public static final void setColorResolvers(ColorResolver grass, ColorResolver foliage, ColorResolver water) {
		GRASS_COLOR_RESOLVER = grass;
		FOLIAGE_COLOR_RESOLVER = foliage;
		WATER_COLOR_RESOLVER = water;
		dirtyFlag = true;
	}

	public static final void setGrassColorResolver(ColorResolver grass) {
		GRASS_COLOR_RESOLVER = grass;
		dirtyFlag = true;
	}

	public static final void setFoliageColorResolver(ColorResolver foliage) {
		FOLIAGE_COLOR_RESOLVER = foliage;
		dirtyFlag = true;
	}

	public static final void setWaterColorResolver(ColorResolver water) {
		WATER_COLOR_RESOLVER = water;
		dirtyFlag = true;
	}

	public static final ColorResolver fixedVanilla(int r, int g, int b) {
		return (Biome biome, double x, double z) -> ColorRGBA.pack(r, g, b);
	}

	public static final void setFixedGrassColor(int r, int g, int b) {
		setGrassColorResolver(fixedVanilla(r, g, b));
	}

	public static final void setFixedFoliageColor(int r, int g, int b) {
		setFoliageColorResolver(fixedVanilla(r, g, b));
	}

	public static final void setFixedWaterColor(int r, int g, int b) {
		setWaterColorResolver(fixedVanilla(r, g, b));
	}

	public static final LevelColor waterFogColor = new LevelColor();

	/**
	 * 修改水中的雾颜色
	 * 
	 * @param resolver
	 */
	public static final void addWaterFogColorResolver(LevelColor.Resolver resolver) {
		waterFogColor.addResolver(resolver);
	}

	public static final void addWaterFogColorResolver(ColorLinearInterpolation color) {
		waterFogColor.addColorResolver(color);
	}

	public static final void setFixedWaterFogColor(int r, int g, int b) {
		addWaterFogColorResolver(LevelColor.fixed(r, g, b));
	}

	/**
	 * 修改生物群系返回的原始雾颜色
	 */
	public static final LevelColor fogColor = new LevelColor();

	public static final void addFogColorResolver(LevelColor.Resolver resolver) {
		fogColor.addResolver(resolver);
	}

	public static final void addFogColorResolver(ColorLinearInterpolation color) {
		fogColor.addColorResolver(color);
	}

	public static final void setFixedFogColor(int r, int g, int b) {
		addFogColorResolver(LevelColor.fixed(r, g, b, 0));// 雾的alpha通道为0
	}

	public static final void setFixedGrayScaleFogColor(int r, int g, int b) {
		addFogColorResolver(LevelColor.fixedGrayScale(r, g, b, 0));// 雾的alpha通道为0
	}
}
