package lepus.mc.mixins.defs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import lepus.graphics.ColorRGBA;
import lepus.mc.client.render.level.BiomeColor;
import lepus.mc.mixins.internal.LevelRendererInternal;
import lepus.mc.mixins.internal.TargetDescriptors;
import net.minecraft.world.level.biome.Biome;

@Mixin(targets = { "net.minecraft.client.renderer.FogRenderer" })
public class FogRendererMixin {
	@WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = TargetDescriptors.LBiome.getWaterFogColor))
	private static int setupColor_modifyAfterGetWaterFogColor(Biome biome, Operation<Integer> op) {
		ColorRGBA orig = ColorRGBA.of(op.call(biome));
		ColorRGBA result = BiomeColor.waterFogColor.resolve(orig, LevelRendererInternal.RenderLevel.LocalVars.level, LevelRendererInternal.RenderLevel.LocalVars.partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, LevelRendererInternal.RenderLevel.LocalVars.camPos, LevelRendererInternal.RenderLevel.LocalVars.dayTime);
		return result.pack();
	}

	@WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = TargetDescriptors.LRenderSystem.clearColor))
	private static void setupColor_modifyAfterGetRawFogColor(float fogRed, float fogGreen, float fogBlue, float fogAlpha, Operation<Void> op) {
		ColorRGBA orig = ColorRGBA.of(fogRed, fogGreen, fogBlue, fogAlpha);
		ColorRGBA result = BiomeColor.fogColor.resolve(orig, LevelRendererInternal.RenderLevel.LocalVars.level, LevelRendererInternal.RenderLevel.LocalVars.partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, LevelRendererInternal.RenderLevel.LocalVars.camPos, LevelRendererInternal.RenderLevel.LocalVars.dayTime);
		op.call(result.r, result.g, result.b, result.a);
	}
}
