package fw.client.render.gl;

import java.util.HashMap;

import org.lwjgl.opengl.GL30;

/**
 * 拦截上下文帧缓冲
 */
public class InterceptFramebuffer extends Framebuffer {
	public FramebufferRenderer framebuffer_render;

	private static final HashMap<String, InterceptFramebuffer> interceptFramebuffers = new HashMap<>();

	public InterceptFramebuffer() {
		framebuffer_render = FramebufferRenderer.createUnbound();
	}

	private InterceptFramebuffer updateFramebufferRenderer(int target_framebuffer) {
		framebuffer_render.setSourceColorAttachment(super.framebuffer, super.color_attachment);
		framebuffer_render.setTargetFramebuffer(target_framebuffer);
		return this;
	}

	private static InterceptFramebuffer intercept(String name, int target_framebuffer) {
		int width = Framebuffer.framebufferWidth(target_framebuffer);// 每次获取时先判断尺寸是否改变
		int height = Framebuffer.framebufferHeight(target_framebuffer);
		return ((InterceptFramebuffer) interceptFramebuffers.computeIfAbsent(name, (String s) -> new InterceptFramebuffer()).resize(width, height)).updateFramebufferRenderer(target_framebuffer);
	}

	public static InterceptFramebuffer intercept(String name) {
		return intercept(name, Framebuffer.currentBindFramebuffer());
	}

	public static InterceptFramebuffer of(String name) {
		return (InterceptFramebuffer) interceptFramebuffers.get(name);
	}

	public void setShader(ScreenShader framebuffer_process_shader) {
		framebuffer_render.setShader(framebuffer_process_shader);
	}

	/**
	 * 拦截渲染到当前上下文帧缓冲的操作并渲染到本帧缓冲中
	 */
	public void bind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, super.framebuffer);
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
	}

	public static void bind(String name) {
		InterceptFramebuffer.intercept(name).bind();
	}

	public void blitToTarget() {
		framebuffer_render.render();
	}

	public static void blitToTarget(String name, ScreenShader blitShader) {
		InterceptFramebuffer framebuffer = InterceptFramebuffer.of(name);
		framebuffer.setShader(blitShader);
		framebuffer.blitToTarget();
	}

	public static void blitToTarget(String name) {
		blitToTarget(name, ScreenShader.SCREEN_BLIT_SHADER);
	}
}
