package lepus.graphics;

import java.util.HashMap;

import org.lwjgl.opengl.GL33;

import lepus.graphics.shader.ScreenShader;

/**
 * 拦截上下文帧缓冲，并将拦截的帧缓冲内容拷贝到本帧缓冲中
 */
public class InterceptCopyFramebuffer extends InterceptFramebuffer {

	/**
	 * 设定目标帧缓冲，并将其内容拷贝到本帧缓冲
	 * 
	 * @param target_framebuffer
	 * @return
	 */
	protected void postcapture(int target_framebuffer, int target_color_attachment, int width, int height) {
		int prev_read = Framebuffer.currentBindReadFramebuffer();
		int prev_write = Framebuffer.currentBindWriteFramebuffer();
		GL33.glBindFramebuffer(GL33.GL_READ_FRAMEBUFFER, target_framebuffer);
		GL33.glBindFramebuffer(GL33.GL_DRAW_FRAMEBUFFER, super.framebuffer);
		GL33.glBlitFramebuffer(0, 0, super.width(), super.height(), 0, 0, super.width(), super.height(), GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT | GL33.GL_STENCIL_BUFFER_BIT, GL33.GL_NEAREST);
		GL33.glBindFramebuffer(GL33.GL_READ_FRAMEBUFFER, prev_read);
		GL33.glBindFramebuffer(GL33.GL_READ_FRAMEBUFFER, prev_write);
	}

	private static final HashMap<String, InterceptCopyFramebuffer> interceptFramebuffers = new HashMap<>();

	public static InterceptCopyFramebuffer capture(String name, int target_framebuffer) {
		return (InterceptCopyFramebuffer) interceptFramebuffers.computeIfAbsent(name, (String s) -> new InterceptCopyFramebuffer()).capture(target_framebuffer);
	}

	public static InterceptCopyFramebuffer capture(String name) {
		return capture(name, Framebuffer.currentBindFramebuffer());
	}

	public static InterceptCopyFramebuffer captureWrite(String name) {
		return capture(name, Framebuffer.currentBindWriteFramebuffer());
	}

	public static InterceptCopyFramebuffer of(String name) {
		return interceptFramebuffers.get(name);
	}

	/**
	 * 拦截上下文帧缓冲，在此语句后所有渲染操作均写入本帧缓冲
	 * 
	 * @param name
	 */
	public static void intercept(String name) {
		InterceptCopyFramebuffer.capture(name).intercept();
	}

	/**
	 * 恢复上下文帧缓冲，在此语句时所有拦截的渲染操作均写入目标帧缓冲
	 * 
	 * @param name
	 */
	public static void writeback(String name, ScreenShader blitShader, boolean blend) {
		InterceptCopyFramebuffer.of(name).writeback(blitShader, blend);
	}

	public static void writeback(String name, boolean blend) {
		writeback(name, ScreenShader.SCREEN_BLIT_SHADER, blend);
	}

	/**
	 * 默认不启用混合直接覆盖目标缓冲
	 * 
	 * @param name
	 * @param blitShader
	 */
	public static void writeback(String name, ScreenShader blitShader) {
		InterceptCopyFramebuffer.of(name).writeback(blitShader, false);
	}

	public static void writeback(String name) {
		writeback(name, ScreenShader.SCREEN_BLIT_SHADER, false);
	}
}
