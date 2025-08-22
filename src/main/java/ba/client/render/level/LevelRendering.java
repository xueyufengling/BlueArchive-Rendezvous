package ba.client.render.level;

import fw.client.render.level.BiomeColor;
import fw.client.render.sky.SkyColor;
import fw.client.render.sky.WeatherEffect;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LevelRendering {
	public static int[] COLOUR_INVASION_SKY = new int[] { 191, 38, 76 };
	public static int[] COLOUR_INVASION_CLOUD = new int[] { 204, 163, 204 };

	static {
		WeatherEffect.setRainTexture("ba:textures/sky/environment/rain.png");
	}

	public static final void renderColourInvasion() {
		WeatherEffect.setRainColor(255, 0, 0);
		SkyColor.setFixedSkyColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
		SkyColor.setFixedCloudColor(COLOUR_INVASION_CLOUD[0], COLOUR_INVASION_CLOUD[1], COLOUR_INVASION_CLOUD[2]);
		BiomeColor.setFixedWaterColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
		BiomeColor.setFixedWaterFogColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
		BiomeColor.setFixedGrayScaleFogColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
	}
}
