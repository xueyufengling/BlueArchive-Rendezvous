package lepus.graphics;

import java.util.HashMap;

import org.lwjgl.opengl.GL33;

import lepus.graphics.shader.ScreenShader;

/**
 * 拦截上下文帧缓冲，不拷贝被拦截的帧缓冲，而是自身作为一个空白帧缓冲供绘制操作。<br>
 * 可用作图层
 */
public class InterceptOperationFramebuffer extends InterceptFramebuffer {

	/**
	 * 设定目标帧缓冲，并将其内容拷贝到本帧缓冲
	 * 
	 * @param target_framebuffer
	 * @return
	 */
	protected void postcapture(int target_framebuffer, int target_color_attachment, int width, int height) {
		float[] prev_clear_color = Framebuffer.currentClearColor();
		GL33.glClearColor(0, 0, 0, 0);// 全透明黑色背景
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT | GL33.GL_STENCIL_BUFFER_BIT);
		GL33.glClearColor(prev_clear_color[0], prev_clear_color[1], prev_clear_color[2], prev_clear_color[3]);
	}

	private static final HashMap<String, InterceptOperationFramebuffer> interceptFramebuffers = new HashMap<>();

	public static InterceptOperationFramebuffer capture(String name, int target_framebuffer) {
		return (InterceptOperationFramebuffer) interceptFramebuffers.computeIfAbsent(name, (String s) -> new InterceptOperationFramebuffer()).capture(target_framebuffer);
	}

	public static InterceptOperationFramebuffer capture(String name) {
		return capture(name, Framebuffer.currentBindFramebuffer());
	}

	public static InterceptOperationFramebuffer captureWrite(String name) {
		return capture(name, Framebuffer.currentBindWriteFramebuffer());
	}

	public static InterceptOperationFramebuffer of(String name) {
		return interceptFramebuffers.get(name);
	}

	/**
	 * 拦截上下文帧缓冲，在此语句后所有渲染操作均写入本帧缓冲
	 * 
	 * @param name
	 */
	public static void intercept(String name) {
		InterceptOperationFramebuffer.capture(name).intercept();
	}

	/**
	 * 恢复上下文帧缓冲，在此语句时所有拦截的渲染操作均写入目标帧缓冲<br>
	 * 需要混合到目标上<br>
	 * 
	 * @param name
	 */
	public static void writeback(String name, ScreenShader blitShader) {
		InterceptOperationFramebuffer.of(name).writeback(blitShader, true);
	}

	public static void writeback(String name) {
		writeback(name, ScreenShader.SCREEN_BLIT_SHADER);
	}
}
