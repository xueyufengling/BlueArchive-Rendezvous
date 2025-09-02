package lepus.graphics;

import org.lwjgl.opengl.GL33;

import lepus.graphics.shader.ScreenShader;

/**
 * 拦截上下文帧缓冲，不拷贝被拦截的帧缓冲，并在渲染结束后将拦截的渲染结果写回目标帧缓冲
 */
public abstract class InterceptFramebuffer extends Framebuffer {
	private FramebufferRenderer framebuffer_render;

	public InterceptFramebuffer() {
		framebuffer_render = FramebufferRenderer.createUnbound();
	}

	private final void update(int target_framebuffer) {
		framebuffer_render.setSourceColorAttachment(super.framebuffer, super.color_attachment);
		framebuffer_render.setTargetFramebuffer(target_framebuffer);
	}

	/**
	 * 捕获后的操作，可选择拷贝原帧缓冲或者clear本帧缓冲
	 */
	protected abstract void postcapture(int target_framebuffer, int target_color_attachment, int width, int height);

	/**
	 * 设定目标帧缓冲，并执行子类重写的捕获后操作
	 * 
	 * @param target_framebuffer
	 * @return
	 */
	public final InterceptFramebuffer capture(int target_framebuffer) {
		int color_attachment = Framebuffer.currentBindColorAttachment(target_framebuffer);
		int width = Texture2D.textureWidth(color_attachment, 0);// 每次获取时先判断尺寸是否改变
		int height = Texture2D.textureHeight(color_attachment, 0);
		((InterceptFramebuffer) this.resize(width, height)).update(target_framebuffer);
		this.postcapture(target_framebuffer, color_attachment, width, height);
		return this;
	}

	/**
	 * 捕捉上下文帧缓冲
	 * 
	 * @return
	 */
	public final InterceptFramebuffer capture() {
		return capture(Framebuffer.currentBindFramebuffer());
	}

	public final InterceptFramebuffer captureWrite() {
		return capture(Framebuffer.currentBindWriteFramebuffer());
	}

	/**
	 * 拷贝当前上下文帧缓冲的内容并拦截渲染到当前上下文帧缓冲的操作并渲染到本帧缓冲中
	 */
	public final void intercept() {
		GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, super.framebuffer);
	}

	/**
	 * 写回拦截的结果
	 * 
	 * @param blitShader
	 * @param blend      是否与目标帧缓冲混合
	 */
	public final void writeback(ScreenShader blitShader, boolean blend) {
		framebuffer_render.setShader(blitShader);
		framebuffer_render.render(blend);
		GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, framebuffer_render.target_framebuffer);// 将渲染目标帧缓冲交还
	}

	public final void writeback(boolean blend) {
		writeback(ScreenShader.SCREEN_BLIT_SHADER, blend);
	}

	/**
	 * 对当前上下文帧缓冲进行后处理
	 * 
	 * @param blitShader
	 * @param blend
	 */
	public final void postprocess(ScreenShader blitShader, boolean blend) {
		this.capture();
		this.writeback(blitShader, blend);
	}

	public final void postprocess(ScreenShader blitShader) {
		this.postprocess(blitShader, false);
	}
}
