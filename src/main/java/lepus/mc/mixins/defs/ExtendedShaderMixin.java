package lepus.mc.mixins.defs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lepus.graphics.InterceptCopyFramebuffer;
import lepus.graphics.InterceptOperationFramebuffer;
import lepus.graphics.shader.ScreenShader;
import lepus.mc.ext.client.render.iris.IrisPostprocess;
import net.irisshaders.iris.Iris;

@Mixin(targets = { "net.irisshaders.iris.pipeline.programs.ExtendedShader" })
public class ExtendedShaderMixin {
	@Unique
	private boolean intercepted;

	@Unique
	private boolean blend;

	@Unique
	private String phase_str;

	@Unique
	private ScreenShader postprocessShader;

	@Inject(method = "apply", at = @At(value = "RETURN"), require = 0)
	private void apply_interceptIrisFramebuffer(CallbackInfo ci) {
		String phase = Iris.getPipelineManager().getPipelineNullable().getPhase().name();
		this.phase_str = phase;
		ScreenShader postprocessShader = IrisPostprocess.phasePostProcess.get(phase);
		this.postprocessShader = postprocessShader;
		if (postprocessShader != null) {
			this.intercepted = true;
			switch (phase) {
			case "RAIN_SNOW":
				InterceptOperationFramebuffer.intercept(phase);
				this.blend = true;
				break;
			default:
				InterceptCopyFramebuffer.intercept(phase); // 将天空渲染在自定义帧缓冲内
				this.blend = false;
				break;
			}
		}
	}

	@Inject(method = "clear", at = @At(value = "HEAD"), require = 0)
	private void clear_blitInterceptIrisFramebuffer(CallbackInfo ci) {
		if (this.intercepted) {
			if (this.blend)
				InterceptOperationFramebuffer.writeback(this.phase_str, this.postprocessShader);
			else
				InterceptCopyFramebuffer.writeback(this.phase_str, this.postprocessShader);
			this.intercepted = false;
		}
	}
}
