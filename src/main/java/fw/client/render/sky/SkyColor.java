package fw.client.render.sky;

import fw.client.render.color.TimeBasedColorLinearInterpolation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public class SkyColor {
	public static final LevelColor skyColor = new LevelColor();

	public static final LevelColor.LevelBasedResolver levelBasedResolver = new LevelColor.LevelBasedResolver();
	public static final LevelColor.BiomeBasedResolver biomeBasedResolver = new LevelColor.BiomeBasedResolver();

	static {
		skyColor.addColorResolver(levelBasedResolver);
		skyColor.addColorResolver(biomeBasedResolver);
	}

	public static Vec3 resolve(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) {
		return skyColor.resolve(orig, level, partialTick, biome, camPos, time);
	}

	public static final void setLevelSkyColorResolver(String levelKey, TimeBasedColorLinearInterpolation skyColor) {
		levelBasedResolver.addEntry(levelKey, LevelColor.resolver(skyColor));
	}

	public static final void setLevelSkyColorResolver(String levelKey, TimeBasedColorLinearInterpolation skyColor, double ratio) {
		levelBasedResolver.addEntry(levelKey, LevelColor.resolver(skyColor, ratio));
	}

	public static final void setLevelSkyColorResolver(String levelKey, TimeBasedColorLinearInterpolation skyColor, TimeBasedColorLinearInterpolation weight) {
		levelBasedResolver.addEntry(levelKey, LevelColor.resolver(skyColor, weight));
	}

	public static final void setBiomeSkyColorResolver(String biomeKey, TimeBasedColorLinearInterpolation skyColor) {
		biomeBasedResolver.addEntry(biomeKey, LevelColor.resolver(skyColor));
	}

	public static final void setBiomeSkyColorResolver(String levelKey, TimeBasedColorLinearInterpolation skyColor, double ratio) {
		biomeBasedResolver.addEntry(levelKey, LevelColor.resolver(skyColor, ratio));
	}

	public static final void setBiomeSkyColorResolver(String levelKey, TimeBasedColorLinearInterpolation skyColor, TimeBasedColorLinearInterpolation weight) {
		biomeBasedResolver.addEntry(levelKey, LevelColor.resolver(skyColor, weight));
	}
}