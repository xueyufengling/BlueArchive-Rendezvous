package fw.client.render.gl;

import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.pipeline.RenderTarget;

/**
 * 读取帧缓冲颜色附件，并重新渲染进去
 */
public class FramebufferRenderer {
	/**
	 * 由要读取渲染结果的帧缓冲<<br>
	 * 此缓冲和target_framebuffer不可一样，否则会引发数据竞争
	 */
	public RenderTarget source_framebuffer;

	/**
	 * 要渲染到的目标帧缓冲
	 */
	public RenderTarget target_framebuffer;

	private FramebufferRenderer(RenderTarget source_framebuffer, RenderTarget target_framebuffer) {
		if (source_framebuffer.frameBufferId == target_framebuffer.frameBufferId)
			throw new IllegalArgumentException("Source and target framebuffer should not be the same.");
		this.source_framebuffer = source_framebuffer;
		this.target_framebuffer = target_framebuffer;
	}

	public static FramebufferRenderer createFrom(RenderTarget source_framebuffer, RenderTarget target_framebuffer) {
		return new FramebufferRenderer(source_framebuffer, target_framebuffer);
	}

	public static FramebufferRenderer targetMain(RenderTarget source_framebuffer) {
		return createFrom(source_framebuffer, EquivalentFramebuffer.mainFramebuffer());
	}

	public int width() {
		return target_framebuffer.width;
	}

	public int height() {
		return target_framebuffer.height;
	}

	public static int currentBindFramebuffer() {
		return GL30.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
	}

	private ScreenShader framebuffer_process_shader;

	public void setShader(ScreenShader framebuffer_process_shader) {
		this.framebuffer_process_shader = framebuffer_process_shader;
	}

	public ScreenShader getShader() {
		return framebuffer_process_shader;
	}

	public void render() {
		int prev_framebuffer = currentBindFramebuffer();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, target_framebuffer.frameBufferId);
		GL30.glDisable(GL30.GL_DEPTH_TEST);
		GL30.glDisable(GL30.GL_BLEND);// 关闭混合，缓冲区结果只有渲染的QUAD
		framebuffer_process_shader.renderScreen(source_framebuffer.getColorTextureId());
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, prev_framebuffer);
	}
}
