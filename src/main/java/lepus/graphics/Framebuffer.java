package lepus.graphics;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL33;

import net.minecraft.client.Minecraft;

/**
 * 帧缓冲
 */
public class Framebuffer {
	protected int framebuffer = -1;
	protected int color_attachment = -1;
	protected int ds_attachment = -1;
	/**
	 * 是否有深度模板附件
	 */
	private boolean hasDepthStencil = true;

	private int width;
	private int height;

	public Framebuffer() {

	}

	public Framebuffer(int width, int height, boolean hasDepthStencil) {
		this.allocate(width, height, hasDepthStencil);
	}

	private void free() {
		freeFramebuffer(framebuffer, color_attachment, ds_attachment);
		framebuffer = -1;
		color_attachment = -1;
		ds_attachment = -1;
	}

	private void allocate(int width, int height, boolean hasDepthStencil) {
		this.width = width;
		this.height = height;
		this.hasDepthStencil = hasDepthStencil;
		int[] names = allocateFramebuffer(width, height, hasDepthStencil);
		this.framebuffer = names[0];
		this.color_attachment = names[1];
		if (hasDepthStencil)
			this.ds_attachment = names[2];
		else
			this.ds_attachment = -1;
	}

	/*
	 * 当尺寸改变或者添加、删除深度模板附件时重新分配内存
	 */
	public Framebuffer resize(int width, int height, boolean hasDepthStencil) {
		if (this.width != width || this.height != height || this.hasDepthStencil != hasDepthStencil) {
			free();
			allocate(width, height, hasDepthStencil);
		}
		return this;
	}

	public Framebuffer resize(int width, int height) {
		return this.resize(width, height, this.hasDepthStencil);
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public int framebuffer() {
		return framebuffer;
	}

	public int colorAttachment() {
		return color_attachment;
	}

	public int depthStencilAttachment() {
		return ds_attachment;
	}

	/**
	 * 矫正视口使得渲染画面包含整个帧缓冲
	 */
	public void correctViewport() {
		GL33.glViewport(0, 0, width, height);
	}

	/**
	 * 创建帧缓冲，颜色附件始终是GL_COLOR_ATTACHMENT0，且只有0级纹理
	 * 
	 * @param width
	 * @param height
	 * @param hasDepthStencil 是否有深度模板
	 * @return
	 */
	public static int[] allocateFramebuffer(int width, int height, boolean hasDepthStencil) {
		int[] names = new int[hasDepthStencil ? 3 : 2];
		int framebuffer = GL33.glGenFramebuffers();
		names[0] = framebuffer;
		GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, framebuffer);
		int color_attachment = GL33.glGenTextures();
		names[1] = color_attachment;
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, color_attachment);
		GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAX_LEVEL, 0);// 设置无Mipmap
		GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_LOD, 0);
		GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAX_LOD, 0);
		GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_LINEAR);
		GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_LINEAR);
		GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA8, width, height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL33.glFramebufferTexture2D(GL33.GL_FRAMEBUFFER, GL33.GL_COLOR_ATTACHMENT0, GL33.GL_TEXTURE_2D, color_attachment, 0);// 绑定颜色附件
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
		int ds_attachment = -1;
		if (hasDepthStencil) {
			ds_attachment = GL33.glGenRenderbuffers();
			names[2] = ds_attachment;
			GL33.glBindRenderbuffer(GL33.GL_RENDERBUFFER, ds_attachment);
			GL33.glRenderbufferStorage(GL33.GL_RENDERBUFFER, GL33.GL_DEPTH24_STENCIL8, width, height);
			GL33.glFramebufferRenderbuffer(GL33.GL_FRAMEBUFFER, GL33.GL_DEPTH_STENCIL_ATTACHMENT, GL33.GL_RENDERBUFFER, ds_attachment);// 绑定深度模板附件
			GL33.glBindRenderbuffer(GL33.GL_RENDERBUFFER, 0);
		}
		boolean complete = GL33.glCheckFramebufferStatus(GL33.GL_FRAMEBUFFER) == GL33.GL_FRAMEBUFFER_COMPLETE;
		GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, 0);
		if (complete)
			return names;
		else {
			freeFramebuffer(framebuffer, color_attachment, ds_attachment);
			throw new IllegalStateException("Allocated Framebuffer is not complete.");
		}
	}

	public static void freeFramebuffer(int framebuffer, int color_attachment, int ds_attachment) {
		if (framebuffer > 0)
			GL33.glDeleteTextures(color_attachment);
		if (ds_attachment > 0)
			GL33.glDeleteRenderbuffers(ds_attachment);
		if (framebuffer > 0)
			GL33.glDeleteFramebuffers(framebuffer);
	}

	private static int mainFramebuffer = -1;

	public static final int mainFramebuffer() {
		if (mainFramebuffer < 0)
			mainFramebuffer = Minecraft.getInstance().getMainRenderTarget().frameBufferId;
		return mainFramebuffer;
	}

	/**
	 * 查询当前绑定的帧缓冲
	 * 
	 * @return
	 */
	public static int currentBindFramebuffer() {
		return GL33.glGetInteger(GL33.GL_FRAMEBUFFER_BINDING);
	}

	public static int currentBindReadFramebuffer() {
		return GL33.glGetInteger(GL33.GL_READ_FRAMEBUFFER_BINDING);
	}

	public static int currentBindWriteFramebuffer() {
		return GL33.glGetInteger(GL33.GL_DRAW_FRAMEBUFFER_BINDING);
	}

	/**
	 * 获取帧缓冲绑定的颜色附件
	 * 
	 * @param framebuffer
	 * @param color_attachment_idx
	 * @return
	 */
	public static int currentBindColorAttachment(int framebuffer, int color_attachment_idx) {
		int prev_framebuffer = currentBindFramebuffer();
		GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, framebuffer);
		int tex = GL33.glGetFramebufferAttachmentParameteri(GL33.GL_FRAMEBUFFER, GL33.GL_COLOR_ATTACHMENT0 + color_attachment_idx, GL33.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME);// 获取帧缓冲绑定的颜色附件
		GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, prev_framebuffer);
		return tex;
	}

	public static int currentBindColorAttachment(int framebuffer) {
		return currentBindColorAttachment(framebuffer, 0);
	}

	public static int framebufferWidth(int framebuffer) {
		return Texture2D.textureWidth(currentBindColorAttachment(framebuffer), 0);
	}

	public static int framebufferHeight(int framebuffer) {
		return Texture2D.textureHeight(currentBindColorAttachment(framebuffer), 0);
	}

	public static float[] currentClearColor() {
		float[] color = new float[4];
		GL33.glGetFloatv(GL33.GL_COLOR_CLEAR_VALUE, color);
		return color;
	}
}
