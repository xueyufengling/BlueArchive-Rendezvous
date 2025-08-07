package fw.client.render.sky;

import fw.client.render.color.TimeBasedColorLinearInterpolation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public class CloudColor {
	public static final LevelColor cloudColor = new LevelColor();

	public static final LevelColor.LevelBasedResolver levelBasedResolver = new LevelColor.LevelBasedResolver();
	public static final LevelColor.BiomeBasedResolver biomeBasedResolver = new LevelColor.BiomeBasedResolver();

	static {
		cloudColor.addColorResolver(levelBasedResolver);
		cloudColor.addColorResolver(biomeBasedResolver);
	}

	public static Vec3 resolve(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) {
		return cloudColor.resolve(orig, level, partialTick, biome, camPos, time);
	}

	public static final void setLevelCloudColorResolver(String levelKey, TimeBasedColorLinearInterpolation cloudColor) {
		levelBasedResolver.addEntry(levelKey, LevelColor.resolver(cloudColor));
	}

	public static final void setLevelCloudColorResolver(String levelKey, TimeBasedColorLinearInterpolation skyColor, double ratio) {
		levelBasedResolver.addEntry(levelKey, LevelColor.resolver(skyColor, ratio));
	}

	public static final void setLevelCloudColorResolver(String levelKey, TimeBasedColorLinearInterpolation skyColor, TimeBasedColorLinearInterpolation weight) {
		levelBasedResolver.addEntry(levelKey, LevelColor.resolver(skyColor, weight));
	}

	public static final void setBiomeCloudColorResolver(String biomeKey, TimeBasedColorLinearInterpolation cloudColor) {
		biomeBasedResolver.addEntry(biomeKey, LevelColor.resolver(cloudColor));
	}

	public static final void setBiomeCloudColorResolver(String levelKey, TimeBasedColorLinearInterpolation skyColor, double ratio) {
		biomeBasedResolver.addEntry(levelKey, LevelColor.resolver(skyColor, ratio));
	}

	public static final void setBiomeCloudColorResolver(String levelKey, TimeBasedColorLinearInterpolation skyColor, TimeBasedColorLinearInterpolation weight) {
		biomeBasedResolver.addEntry(levelKey, LevelColor.resolver(skyColor, weight));
	}

}
