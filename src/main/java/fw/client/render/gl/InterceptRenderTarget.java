package fw.client.render.gl;

import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;

import net.minecraft.client.Minecraft;

/**
 * 用于替换目标帧缓冲，使得本该渲染到目标帧缓冲的图形渲染在本帧缓冲上<br>
 * 并且可以使用blitToTarget()将渲染在本帧缓冲上的内容写入目标帧缓冲(将禁用混合和深度测试)。
 */
public class InterceptRenderTarget extends MainTarget {
	private RenderTarget target_framebuffer;
	private FramebufferRenderer framebuffer_render;

	private InterceptRenderTarget(RenderTarget target_framebuffer) {
		super(target_framebuffer.width, target_framebuffer.height);
		this.target_framebuffer = target_framebuffer;
		this.framebuffer_render = FramebufferRenderer.createFrom(super.colorTextureId, target_framebuffer.frameBufferId);
	}

	public static InterceptRenderTarget createFrom(RenderTarget target_framebuffer) {
		return new InterceptRenderTarget(target_framebuffer);
	}

	private static MainTarget mainFramebuffer;

	public static final MainTarget mainTarget() {
		if (mainFramebuffer == null)
			mainFramebuffer = (MainTarget) Minecraft.getInstance().getMainRenderTarget();
		return mainFramebuffer;
	}

	public static InterceptRenderTarget createFromMain() {
		return new InterceptRenderTarget(mainTarget());
	}

	/**
	 * 记录上次渲染时的窗口高度宽度，如果检测到尺寸变化则需要跟着resize
	 */
	private int recorded_width, recorded_height;

	/**
	 * 检查是否target_framebuffer的尺寸改变，如果改变则跟着resize
	 */
	private void checkSize() {
		int width = target_framebuffer.width;
		int height = target_framebuffer.height;
		if (recorded_width != width || recorded_height != height) {
			this.destroyBuffers();
			this.createBuffers(width, height, Minecraft.ON_OSX);
			recorded_width = width;
			recorded_height = height;
		}
	}

	public void setShader(ScreenShader framebuffer_process_shader) {
		framebuffer_render.setShader(framebuffer_process_shader);
	}

	/**
	 * 拦截渲染到当前上下文帧缓冲的操作并渲染到本帧缓冲中
	 */
	public void intercept() {
		checkSize();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.frameBufferId);
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
	}

	public void blitToTarget() {
		framebuffer_render.render();
	}
}
