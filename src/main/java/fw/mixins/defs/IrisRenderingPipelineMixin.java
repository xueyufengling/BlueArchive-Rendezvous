package fw.mixins.defs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fw.client.render.gl.InterceptFramebuffer;
import fw.client.render.sky.Sky;
import fw.mixins.internal.TargetDescriptors;

@Mixin(targets = { "net.irisshaders.iris.pipeline.IrisRenderingPipeline" })
public class IrisRenderingPipelineMixin {
/*
	@Inject(method = "beginLevelRendering", at = @At(value = "INVOKE", target = TargetDescriptors.LIrisRenderingPipeline.setPhase, shift = Shift.AFTER), require = 0)
	private void beginLevelRendering_interceptIrisFramebuffer(CallbackInfo ci) {
		InterceptFramebuffer.intercept("sky");// 将天空渲染在自定义帧缓冲内
	}

	@Inject(method = "beginLevelRendering", at = @At(value = "RETURN"), require = 0)
	private void beginLevelRendering_blitInterceptIrisFramebuffer(CallbackInfo ci) {
		InterceptFramebuffer.writeback("sky", Sky.sky_postprocess_shader);
	}*/
}
