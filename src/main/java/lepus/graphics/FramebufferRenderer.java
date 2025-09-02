package lepus.graphics;

import org.lwjgl.opengl.GL33;

import lepus.graphics.shader.ScreenShader;

/**
 * 读取帧缓冲颜色附件，并重新渲染进去
 */
public class FramebufferRenderer {
	/**
	 * 由要读取渲染结果的帧缓冲<<br>
	 * 此缓冲和target_framebuffer不可一样，否则会引发数据竞争
	 */
	protected int source_framebuffer;

	protected int source_framebuffer_color_attachment;

	/**
	 * 要渲染到的目标帧缓冲
	 */
	protected int target_framebuffer;

	protected FramebufferRenderer() {

	}

	protected FramebufferRenderer(int source_framebuffer, int target_framebuffer) {
		setSourceFramebuffer(source_framebuffer);
		setTargetFramebuffer(target_framebuffer);
	}

	void setSourceFramebuffer(int source_framebuffer) {
		this.source_framebuffer = source_framebuffer;
		this.source_framebuffer_color_attachment = -1;
		if (source_framebuffer > 0) {// 仅当source_framebuffer为有效帧缓冲时才获取颜色
			this.source_framebuffer_color_attachment = Framebuffer.currentBindColorAttachment(source_framebuffer);
			GL33.glBindTexture(GL33.GL_TEXTURE_2D, source_framebuffer_color_attachment);
			GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAX_LEVEL, 0);// 设置无Mipmap
			GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_LOD, 0);
			GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAX_LOD, 0);
			GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
		}
	}

	void setSourceColorAttachment(int source_framebuffer, int source_framebuffer_color_attachment) {
		this.source_framebuffer = source_framebuffer;
		this.source_framebuffer_color_attachment = source_framebuffer_color_attachment;
	}

	void setTargetFramebuffer(int target_framebuffer) {
		this.target_framebuffer = target_framebuffer;
	}

	public boolean available() {
		return source_framebuffer_color_attachment > 0 && target_framebuffer > 0;
	}

	/**
	 * 创建一个渲染纹理到整个帧缓冲颜色附件的渲染器
	 * 
	 * @param source_framebuffer 源帧缓冲
	 * @param target_framebuffer 目标帧缓冲
	 * @return
	 */
	public static FramebufferRenderer createFrom(int source_framebuffer, int target_framebuffer) {
		return new FramebufferRenderer(source_framebuffer, target_framebuffer);
	}

	public static FramebufferRenderer createUnbound() {
		return new FramebufferRenderer();
	}

	public int sourceFramebuffer() {
		return source_framebuffer;
	}

	public int sourceColorAttachment() {
		return source_framebuffer_color_attachment;
	}

	public int targetFramebuffer() {
		return target_framebuffer;
	}

	private ScreenShader framebuffer_process_shader;

	public final void setShader(ScreenShader framebuffer_process_shader) {
		this.framebuffer_process_shader = framebuffer_process_shader;
	}

	public final ScreenShader getShader() {
		return framebuffer_process_shader;
	}

	/**
	 * 将源纹理渲染到整个目标帧缓冲上
	 * 
	 * @param blend 是否与目标混合
	 */
	public final void render(boolean blend) {
		int prev_framebuffer = Framebuffer.currentBindFramebuffer();
		GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, target_framebuffer);
		GL33.glDisable(GL33.GL_DEPTH_TEST);
		if (blend) {
			GL33.glDisable(GL33.GL_BLEND);// 关闭混合，缓冲区结果只有渲染的QUAD
			framebuffer_process_shader.renderScreen(source_framebuffer_color_attachment);
			GL33.glEnable(GL33.GL_BLEND);
		} else {
			GL33.glEnable(GL33.GL_BLEND);
			framebuffer_process_shader.renderScreen(source_framebuffer_color_attachment);
		}
		GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, prev_framebuffer);
	}

	public final void render() {
		render(false);
	}
}
