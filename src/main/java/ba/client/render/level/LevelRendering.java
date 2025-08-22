package ba.client.render.level;

import fw.client.render.level.BiomeColor;
import fw.client.render.renderable.Texture;
import fw.client.render.scene.RenderableObject;
import fw.client.render.scene.SceneGraphNode;
import fw.client.render.sky.NearEarthObject;
import fw.client.render.sky.Sky;
import fw.client.render.sky.SkyColor;
import fw.client.render.sky.WeatherEffect;
import fw.client.render.sky.NearEarthObject.Pos;
import fw.core.ExecuteIn;
import fw.core.ModInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LevelRendering {
	public static int[] COLOUR_INVASION_SKY = new int[] { 191, 38, 76 };
	public static int[] COLOUR_INVASION_CLOUD = new int[] { 204, 163, 204 };

	static {
		WeatherEffect.setRainTexture("ba:textures/sky/environment/rain.png");
		ModInit.Initializer.forInit();
	}

	public static final void renderColourInvasion() {
		WeatherEffect.setRainColor(255, 0, 0);
		SkyColor.setFixedSkyColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
		SkyColor.setFixedCloudColor(COLOUR_INVASION_CLOUD[0], COLOUR_INVASION_CLOUD[1], COLOUR_INVASION_CLOUD[2]);
		BiomeColor.setFixedWaterColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
		BiomeColor.setFixedWaterFogColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
		BiomeColor.setFixedGrayScaleFogColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
	}

	@ModInit(exec_stage = ModInit.Stage.CLIENT_CONNECT)
	private static void initHalos() {
		ExecuteIn.Client(() -> {
			float halo_z = 0.4f;
			Pos.ViewHeight final_view_height = Pos.ViewHeight.lerpDecayTo(500, 1, 50, 0.5f);
			RenderableObject halo5 = RenderableObject.quad(Texture.of("ba:textures/sky/halo/halo5.png"), 4.5f, halo_z, 0.8f);
			RenderableObject halo10 = RenderableObject.quad(Texture.of("ba:textures/sky/halo/halo10.png"), 4.0f, halo_z, 0.8f);
			RenderableObject halo15 = RenderableObject.quad(Texture.of("ba:textures/sky/halo/halo15.png"), 3.5f, halo_z, 0.8f);
			RenderableObject halo20 = RenderableObject.quad(Texture.of("ba:textures/sky/halo/halo20.png"), 3.0f, halo_z, 0.8f);
			RenderableObject halo25 = RenderableObject.quad(Texture.of("ba:textures/sky/halo/halo25.png"), 2.5f, halo_z, 0.8f);
			RenderableObject halo30 = RenderableObject.quad(Texture.of("ba:textures/sky/halo/halo30.png"), 2.0f, halo_z, 0.8f);
			RenderableObject halo35 = RenderableObject.quad(Texture.of("ba:textures/sky/halo/halo35.png"), 1.5f, halo_z, 0.8f);
			RenderableObject halo40 = RenderableObject.quad(Texture.of("ba:textures/sky/halo/halo40.png"), 1.0f, halo_z, 0.8f);
			SceneGraphNode central_halo0 = Sky.renderFixedNearEarthObject("kivotos/halo/central_0", halo40, 0, 300, 0, final_view_height);
			SceneGraphNode central_halo1 = Sky.renderFixedNearEarthObject("kivotos/halo/central_1", halo20, 0, 300, 0, final_view_height);
			SceneGraphNode central_halo2 = Sky.renderFixedNearEarthObject("kivotos/halo/central_2", halo10, 0, 300, 0, final_view_height);
			float omega = 3.14f / 200f;
			NearEarthObject.Orbit orbit = NearEarthObject.Orbit.circle(0, 300, 0, 224, omega / 8, final_view_height);
			SceneGraphNode inner1_halo1 = Sky.renderNearEarthObject("kivotos/halo/i1", halo30, orbit);
			NearEarthObject.Orbit orbit2 = NearEarthObject.Orbit.circle(orbit, 80, omega, final_view_height);
			SceneGraphNode inner1_inner1_halo1 = Sky.renderNearEarthObject("kivotos/halo/i1/i1", halo40, orbit2);
			// LevelRendering.renderColourInvasion();
		});
		System.err.println("Rendering halos");
	}
}
