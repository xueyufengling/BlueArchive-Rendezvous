package fw.mixins.defs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import fw.client.render.level.BiomeColor;
import fw.common.ColorRGBA;
import fw.mixins.internal.LevelRendererInternal;
import net.minecraft.world.level.biome.Biome;

@Mixin(targets = { "net.minecraft.client.renderer.FogRenderer" })
public class FogRendererMixin {
	@WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I"))
	private static int setupColor_modifyAfterGetWaterFogColor(Biome biome, Operation<Integer> op) {
		ColorRGBA orig = ColorRGBA.of(op.call(biome));
		ColorRGBA result = BiomeColor.waterFogColor.resolve(orig, LevelRendererInternal.RenderLevel.LocalVars.level, LevelRendererInternal.RenderLevel.LocalVars.partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, LevelRendererInternal.RenderLevel.LocalVars.camPos, LevelRendererInternal.RenderLevel.LocalVars.dayTime);
		return result.pack();
	}

	@WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V"))
	private static void setupColor_modifyAfterGetRawFogColor(float fogRed, float fogGreen, float fogBlue, float fogAlpha, Operation<Void> op) {
		ColorRGBA orig = ColorRGBA.of(fogRed, fogGreen, fogBlue, fogAlpha);
		ColorRGBA result = BiomeColor.fogColor.resolve(orig, LevelRendererInternal.RenderLevel.LocalVars.level, LevelRendererInternal.RenderLevel.LocalVars.partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, LevelRendererInternal.RenderLevel.LocalVars.camPos, LevelRendererInternal.RenderLevel.LocalVars.dayTime);
		op.call(result.r, result.g, result.b, result.a);
	}
}
