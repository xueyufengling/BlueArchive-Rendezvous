package lepus.mc.mixins.defs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lepus.graphics.InterceptCopyFramebuffer;
import lepus.mc.ext.client.render.iris.IrisPostprocess;
import lepus.mc.mixins.internal.TargetDescriptors;

@Mixin(targets = { "net.irisshaders.iris.pipeline.FinalPassRenderer" })
public class FinalPassRendererMixin {
	@Unique
	private static InterceptCopyFramebuffer finalPassIntercept = new InterceptCopyFramebuffer();

	@Inject(method = "renderFinalPass", at = @At(value = "INVOKE", target = TargetDescriptors.Iris.LFullScreenQuadRenderer.renderQuad, shift = At.Shift.AFTER), cancellable = true)
	private void clear_blitInterceptIrisFramebuffer(CallbackInfo ci) {
		if (IrisPostprocess.finalPostProcess != null) {
			finalPassIntercept.postprocess(IrisPostprocess.finalPostProcess);
		}
	}
}
