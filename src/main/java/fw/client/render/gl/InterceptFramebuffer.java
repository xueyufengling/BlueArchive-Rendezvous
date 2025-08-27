package fw.client.render.gl;

import java.util.HashMap;

import org.lwjgl.opengl.GL30;

/**
 * 拦截上下文帧缓冲，并在渲染结束后将拦截的渲染结果写回目标帧缓冲
 */
public class InterceptFramebuffer extends Framebuffer {
	private FramebufferRenderer framebuffer_render;

	public InterceptFramebuffer() {
		framebuffer_render = FramebufferRenderer.createUnbound();
	}

	private InterceptFramebuffer update(int target_framebuffer) {
		framebuffer_render.setSourceColorAttachment(super.framebuffer, super.color_attachment);
		framebuffer_render.setTargetFramebuffer(target_framebuffer);
		return this;
	}

	/**
	 * 设定目标帧缓冲
	 * 
	 * @param target_framebuffer
	 * @return
	 */
	public InterceptFramebuffer capture(int target_framebuffer) {
		int color_attachment = Framebuffer.currentBindColorAttachment(target_framebuffer);
		int width = Framebuffer.textureWidth(color_attachment, 0);// 每次获取时先判断尺寸是否改变
		int height = Framebuffer.textureHeight(color_attachment, 0);
		return ((InterceptFramebuffer) this.resize(width, height)).update(target_framebuffer);
	}

	/**
	 * 捕捉上下文帧缓冲
	 * 
	 * @return
	 */
	public InterceptFramebuffer capture() {
		return capture(Framebuffer.currentBindFramebuffer());
	}

	/**
	 * 拦截渲染到当前上下文帧缓冲的操作并渲染到本帧缓冲中
	 */
	public void intercept() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, super.framebuffer);
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * 写回拦截的结果
	 * 
	 * @param blitShader
	 */
	public void writeback(ScreenShader blitShader) {
		framebuffer_render.setShader(blitShader);
		framebuffer_render.render();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer_render.target_framebuffer);// 将渲染目标帧缓冲交还
	}

	public void writeback() {
		writeback(ScreenShader.SCREEN_BLIT_SHADER);
	}

	private static final HashMap<String, InterceptFramebuffer> interceptFramebuffers = new HashMap<>();

	public static InterceptFramebuffer capture(String name, int target_framebuffer) {
		return interceptFramebuffers.computeIfAbsent(name, (String s) -> new InterceptFramebuffer()).capture(target_framebuffer);
	}

	public static InterceptFramebuffer capture(String name) {
		return capture(name, Framebuffer.currentBindFramebuffer());
	}

	public static InterceptFramebuffer of(String name) {
		return interceptFramebuffers.get(name);
	}

	/**
	 * 拦截上下文帧缓冲，在此语句后所有渲染操作均写入本帧缓冲
	 * 
	 * @param name
	 */
	public static void intercept(String name) {
		InterceptFramebuffer.capture(name).intercept();
	}

	/**
	 * 恢复上下文帧缓冲，在此语句时所有拦截的渲染操作均写入目标帧缓冲
	 * 
	 * @param name
	 */
	public static void writeback(String name, ScreenShader blitShader) {
		InterceptFramebuffer.of(name).writeback(blitShader);
	}

	public static void writeback(String name) {
		writeback(name, ScreenShader.SCREEN_BLIT_SHADER);
	}
}
