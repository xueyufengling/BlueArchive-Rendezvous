package fw.client.render.gl;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.pipeline.RenderTarget;

import fw.common.ColorRGBA;
import net.minecraft.client.Minecraft;

/**
 * 帧缓冲的RGBA8颜色附件，PBO
 */
public class PixelsBuffer {
	/**
	 * 记录上次渲染时的窗口高度宽度，如果检测到尺寸变化则需要重新分配PBO
	 */
	private int recorded_width, recorded_height;

	/**
	 * 由于窗口可以resize，因此高度需要实时获取
	 */
	public RenderTarget framebuffer;

	/**
	 * 原版帧缓存是GL_RGBA8
	 */
	private int pixel_format;

	/**
	 * 原版帧缓存是GL_UNSIGNED_BYTE
	 */
	private int pixel_data_type;

	/**
	 * 与本映射缓冲相关联的纹理ID
	 */
	private int associated_texture;

	/**
	 * 映射缓冲
	 */
	private ByteBuffer pixels;

	/**
	 * 双缓冲读取像素
	 */
	private int current_buffer_idx = 0;

	/**
	 * 当前进行操作的PBO
	 */
	private int op_buffer;

	private int[] read_buffers = new int[2];

	public static final int PIXEL_SIZE = 4;

	private PixelsBuffer(RenderTarget framebuffer) {
		this.framebuffer = framebuffer;
		int colorTex = framebuffer.getColorTextureId();
		this.associated_texture = colorTex;
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, colorTex);
		this.pixel_format = GL30.glGetTexLevelParameteri(GL30.GL_TEXTURE_2D, 0, GL30.GL_TEXTURE_INTERNAL_FORMAT);// 获取纹理数据格式
		this.pixel_data_type = GL30.GL_UNSIGNED_BYTE;// 实际上此值与pixel_format相关，但是太多了懒得写，原版均为GL_UNSIGNED_BYTE
		this.resize(framebuffer.width, framebuffer.height);
	}

	private void resize(int width, int height) {
		GL30.glDeleteBuffers(read_buffers);// 先删除之前的缓冲区
		for (int idx = 0; idx < 2; ++idx) {
			int buffer = GL30.glGenBuffers();
			GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, buffer);// 从纹理中读取像素
			GL30.glBufferData(GL30.GL_PIXEL_PACK_BUFFER, width * height * PIXEL_SIZE, GL30.GL_STREAM_READ);
			this.read_buffers[idx] = buffer;
		}
		GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, 0);
		this.recorded_width = width;
		this.recorded_height = height;
	}

	public static PixelsBuffer createFrom(RenderTarget framebuffer) {
		return new PixelsBuffer(framebuffer);
	}

	public static int currentBindFramebuffer() {
		return GL30.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
	}

	private static PixelsBuffer mainPixelsBuffer;

	public static PixelsBuffer main() {
		if (mainPixelsBuffer == null)
			mainPixelsBuffer = createFrom(Minecraft.getInstance().getMainRenderTarget());
		return mainPixelsBuffer;
	}

	public int width() {
		return framebuffer.width;
	}

	public int height() {
		return framebuffer.height;
	}

	/**
	 * 映射读取到的帧缓冲颜色数据到内存
	 * 
	 * @return 是否映射成功，成功后再读取
	 */
	public boolean map() {
		int width = framebuffer.width;
		int height = framebuffer.height;
		if (width != this.recorded_width || height != this.recorded_height)
			this.resize(width, height);// 检测到窗口尺寸变化，则重新分配PBO
		int next_idx = (current_buffer_idx + 1) % 2;
		GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, read_buffers[next_idx]);// 下一个缓存先读取
		GL30.glReadPixels(0, 0, width, height, pixel_format, pixel_data_type, 0);// 读取帧缓冲颜色到PBO
		op_buffer = read_buffers[current_buffer_idx];
		GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, op_buffer);
		ByteBuffer pixels = GL30.glMapBuffer(GL30.GL_PIXEL_PACK_BUFFER, GL30.GL_READ_WRITE);
		this.pixels = pixels;
		current_buffer_idx = next_idx;
		return pixels != null;
	}

	public void unmap() {
		GL30.glUnmapBuffer(GL30.GL_PIXEL_PACK_BUFFER);
		GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, 0);// 解绑PBO
	}

	/**
	 * 将读取并修改后的缓存数据写入指定纹理
	 * 
	 * @param texture
	 */
	public void write(int texture) {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
		GL30.glBindBuffer(GL30.GL_PIXEL_UNPACK_BUFFER, op_buffer);
		GL30.glTexSubImage2D(GL30.GL_TEXTURE_2D, 0, 0, 0, framebuffer.width, framebuffer.height, pixel_format, pixel_data_type, 0);// 从op_buffer拷贝数据
	}

	/**
	 * 将映射获取的颜色数据写入关联纹理
	 */
	public void write() {
		write(associated_texture);
	}

	public ColorRGBA getColor(int x, int y) {
		return ColorRGBA.of(pixels.getInt((x + y * framebuffer.width) * PIXEL_SIZE));
	}

	public void setColor(int x, int y, ColorRGBA color) {
		pixels.putInt((x + y * framebuffer.width) * PIXEL_SIZE, color.packBGRA());
	}
}
