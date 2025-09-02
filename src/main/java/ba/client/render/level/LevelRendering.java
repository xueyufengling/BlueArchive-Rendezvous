package ba.client.render.level;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mojang.math.Axis;

import lepus.graphics.ColorRGBA;
import lepus.graphics.shader.HSLPostprocessShader;
import lepus.mc.client.render.VanillaRenderable;
import lepus.mc.client.render.RenderableObjects;
import lepus.mc.client.render.SceneGraphNode;
import lepus.mc.client.render.level.BiomeColor;
import lepus.mc.client.render.renderable.Texture;
import lepus.mc.client.render.sky.NearEarthObject;
import lepus.mc.client.render.sky.Sky;
import lepus.mc.client.render.sky.WeatherEffect;
import lepus.mc.client.render.sky.NearEarthObject.Pos;
import lepus.mc.core.ExecuteIn;
import lepus.mc.core.ModInit;
import lepus.mc.ext.client.render.iris.IrisPostprocess;
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
		IrisPostprocess.setFinalPostProcess(colour_invasion_sky_shader);
		BiomeColor.setFixedWaterColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
		BiomeColor.setFixedWaterFogColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
		BiomeColor.setFixedGrayScaleFogColor(COLOUR_INVASION_SKY[0], COLOUR_INVASION_SKY[1], COLOUR_INVASION_SKY[2]);
	}

	private static HSLPostprocessShader colour_invasion_sky_shader;

	@SuppressWarnings("unused")
	@ModInit(exec_stage = ModInit.Stage.CLIENT_CONNECT)
	private static void initBgs() {
		ExecuteIn.Client(() -> {
			VanillaRenderable universe = RenderableObjects.sphere(16, 20, 20, Texture.of("ba:textures/sky/universe/milky_way.png"), true);
			Sky.renderSkyBackground("universe", universe);

			float earth_radius = 128;
			VanillaRenderable earth = RenderableObjects.sphere(earth_radius, 200, 200, Texture.of("ba:textures/sky/universe/earth.png"), true);
			SceneGraphNode earth_node = Sky.renderSkyBackground("earth", earth);
			earth_node.setTransform(new Matrix4f().translate(0, -earth_radius - 1, 0).rotate((float) Math.PI / 2, new Vector3f(1, 0, 0)));

			VanillaRenderable sun = RenderableObjects.sphere(1f, 30, 30, Texture.of("ba:textures/sky/universe/sun.png"), true);
			SceneGraphNode sun_node = Sky.renderSkyBackground("sun", sun);
			sun_node.setTransform(new Matrix4f().translate(20, 10, 20));
		});
	}

	@SuppressWarnings("unused")
	// @ModInit(exec_stage = ModInit.Stage.CLIENT_CONNECT)
	private static void initHalos() {
		ExecuteIn.Client(() -> {
			colour_invasion_sky_shader = HSLPostprocessShader.fixed(0, 0.2f, -0.15f);
			// 光环初始化
			float halo_z = 0.4f;
			float scale = 10;
			Pos.ViewHeight final_view_height = Pos.ViewHeight.lerpDecayTo(500, 15, 50, 8);
			VanillaRenderable halo5 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo5.png"), 4.5f * scale, halo_z, 0.8f);
			VanillaRenderable halo10 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo10.png"), 4.0f * scale, halo_z, 0.8f);
			VanillaRenderable halo15 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo15.png"), 3.5f * scale, halo_z, 0.8f);
			VanillaRenderable halo20 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo20.png"), 3.0f * scale, halo_z, 0.8f);
			VanillaRenderable halo25 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo25.png"), 2.5f * scale, halo_z, 0.8f);
			VanillaRenderable halo30 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo30.png"), 2.0f * scale, halo_z, 0.8f);
			VanillaRenderable halo35 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo35.png"), 1.5f * scale, halo_z, 0.8f);
			VanillaRenderable halo40 = RenderableObjects.quad(Texture.of("ba:textures/sky/halo/halo40.png"), 1.0f * scale, halo_z, 0.8f);
			SceneGraphNode central_halo0 = Sky.renderFixedNearEarthObject("kivotos/halo/central_0", halo40, 0, 300, 0, final_view_height);
			SceneGraphNode central_halo1 = Sky.renderFixedNearEarthObject("kivotos/halo/central_1", halo20, 0, 300, 0, final_view_height);
			SceneGraphNode central_halo2 = Sky.renderFixedNearEarthObject("kivotos/halo/central_2", halo10, 0, 300, 0, final_view_height);
			float omega = 3.14f / 200f;
			NearEarthObject.Orbit orbit = NearEarthObject.Orbit.circle(0, 300, 0, 224, omega / 8, final_view_height);
			SceneGraphNode inner1_halo1 = Sky.renderNearEarthObject("kivotos/halo/i1", halo30, orbit);
			NearEarthObject.Orbit orbit2 = NearEarthObject.Orbit.circle(orbit, 80, omega, final_view_height);
			SceneGraphNode inner1_inner1_halo1 = Sky.renderNearEarthObject("kivotos/halo/i1/i1", halo40, orbit2);

			SceneGraphNode missile = Sky.render("missile", RenderableObjects.gradualColorDroplet(0.1f, 3f, 10, ColorRGBA.WHITE, ColorRGBA.ORANGE));
			missile.setUpdate((SceneGraphNode node, float cam_x, float cam_y, float cam_z, float time) -> {
				Matrix4f incline = new Matrix4f();
				incline.rotate(Axis.XP.rotationDegrees(-70.0f));// 绕正x轴旋转90°，让物体位于仰视视角
				incline.rotate(Axis.YP.rotationDegrees(-30.0f));
				incline.rotate(Axis.ZP.rotationDegrees(-50.0f));
				incline.translate(0, 0, 10);
				node.setTransform(incline);
			});
			LevelRendering.renderColourInvasion();
		});
	}
}
