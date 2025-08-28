package ba.client.render.level;

import fw.client.render.gl.ScreenShader;
import fw.client.render.gl.Shader;
import fw.client.render.level.BiomeColor;
import fw.client.render.renderable.Texture;
import fw.client.render.sky.NearEarthObject;
import fw.client.render.sky.NearEarthObject.Pos;
import fw.client.render.sky.Sky;
import fw.client.render.sky.WeatherEffect;
import fw.client.render.vanilla.RenderableObject;
import fw.client.render.vanilla.RenderableObjects;
import fw.client.render.vanilla.SceneGraphNode;
import fw.core.ExecuteIn;
import fw.core.ModInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LevelRendering {
	public static int[] COLOUR_INVASION_SKY = new int[] { 191, 38, 76 };

	static {
		WeatherEffect.setRainTexture("ba:textures/sky/environment/rain.png");
		ModInit.Initializer.forInit();
	}

	public static final void renderColourInvasion() {
		WeatherEffect.setRainColor(255, 0, 0);
		Sky.setSkyPostprocessShader(colour_invasion_sky_shader);
		BiomeColor.setFixedWaterFogColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
		BiomeColor.setFixedGrayScaleFogColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
	}

	private static final String colour_invasion_sky_shader_source = "#version 330 core\n" +
			"uniform sampler2D Texture0;\n" +
			"in vec2 TexCoord;\n" +
			"out vec4 FragColor;\n\n" +
			Shader.hsl_model +
			"void main()\n" +
			"{\n" +
			"   vec4 color = texture(Texture0, TexCoord);\n" +
			"   vec3 hsl = rgb2hsl(vec3(color));\n" +
			"   hsl.x = 0;\n" +
			"   hsl.y -= 0.3;\n" +
			"   hsl.z -= 0.3;\n" +
			"   FragColor = vec4(hsl2rgb(hsl), color.a);\n" +
			"}";

	private static ScreenShader colour_invasion_sky_shader;

	@SuppressWarnings("unused")
	@ModInit(exec_stage = ModInit.Stage.CLIENT_CONNECT)
	private static void initHalos() {
		ExecuteIn.Client(() -> {
			colour_invasion_sky_shader = ScreenShader.createShaderProgram(colour_invasion_sky_shader_source, "Texture0");
			// 光环初始化
			float halo_z = 0.4f;
			Pos.ViewHeight final_view_height = Pos.ViewHeight.lerpDecayTo(500, 1, 50, 0.5f);
			RenderableObject halo5 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo5.png"), 4.5f, halo_z, 0.8f);
			RenderableObject halo10 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo10.png"), 4.0f, halo_z, 0.8f);
			RenderableObject halo15 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo15.png"), 3.5f, halo_z, 0.8f);
			RenderableObject halo20 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo20.png"), 3.0f, halo_z, 0.8f);
			RenderableObject halo25 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo25.png"), 2.5f, halo_z, 0.8f);
			RenderableObject halo30 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo30.png"), 2.0f, halo_z, 0.8f);
			RenderableObject halo35 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo35.png"), 1.5f, halo_z, 0.8f);
			RenderableObject halo40 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo40.png"), 1.0f, halo_z, 0.8f);
			SceneGraphNode central_halo0 = Sky.renderFixedNearEarthObject("kivotos/halo/central_0", halo40, 0, 300, 0, final_view_height);
			SceneGraphNode central_halo1 = Sky.renderFixedNearEarthObject("kivotos/halo/central_1", halo20, 0, 300, 0, final_view_height);
			SceneGraphNode central_halo2 = Sky.renderFixedNearEarthObject("kivotos/halo/central_2", halo10, 0, 300, 0, final_view_height);
			float omega = 3.14f / 200f;
			NearEarthObject.Orbit orbit = NearEarthObject.Orbit.circle(0, 300, 0, 224, omega / 8, final_view_height);
			SceneGraphNode inner1_halo1 = Sky.renderNearEarthObject("kivotos/halo/i1", halo30, orbit);
			NearEarthObject.Orbit orbit2 = NearEarthObject.Orbit.circle(orbit, 80, omega, final_view_height);
			SceneGraphNode inner1_inner1_halo1 = Sky.renderNearEarthObject("kivotos/halo/i1/i1", halo40, orbit2);
			LevelRendering.renderColourInvasion();
		});
	}
}
