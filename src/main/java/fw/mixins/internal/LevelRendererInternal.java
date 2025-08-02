package fw.mixins.internal;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public class LevelRendererInternal {

	public static class Args {
		public static DeltaTracker deltaTracker;
		public static boolean renderBlockOutline;
		public static Camera camera;
		public static GameRenderer gameRenderer;
		public static LightTexture lightTexture;
		public static Matrix4f frustumMatrix;
		public static Matrix4f projectionMatrix;

		public static final void store(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
			// 方法参数
			Args.deltaTracker = deltaTracker;
			Args.renderBlockOutline = renderBlockOutline;
			Args.camera = camera;
			Args.gameRenderer = gameRenderer;
			Args.lightTexture = lightTexture;
			Args.frustumMatrix = frustumMatrix;
			Args.projectionMatrix = projectionMatrix;
		}
	}

	public static class LocalVars {
		public static Vec3 camPos;
		public static Holder<Biome> camPosBiome;

		public static final void store(ClientLevel level) {
			// 局部变量
			Vec3 pos = Args.gameRenderer.getMainCamera().getPosition();
			LocalVars.camPos = pos;
			LocalVars.camPosBiome = level.getBiomeManager().getNoiseBiomeAtPosition(pos.x, pos.y, pos.z);
		}
	}
}
