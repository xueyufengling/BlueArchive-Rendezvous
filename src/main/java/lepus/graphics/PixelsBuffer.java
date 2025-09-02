package lepus.graphics;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL33;

/**
 * 帧缓冲的RGBA8颜色附件，PBO，用于读取<br>
 * 写入时会与正在执行渲染的GPU数据竞争
 */
public class PixelsBuffer {
	/**
	 * 记录上次渲染时的窗口高度宽度，如果检测到尺寸变化则需要重新分配PBO
	 */
	private int recorded_width, recorded_height;

	/**
	 * 由于窗口可以resize，因此高度需要实时获取
	 */
	public Framebuffer framebuffer;

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

	public static final int RGBA8_PIXEL_SIZE = 4;

	private PixelsBuffer(Framebuffer framebuffer) {
		this.framebuffer = framebuffer;
		int colorTex = framebuffer.color_attachment;
		this.associated_texture = colorTex;
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, colorTex);
		this.pixel_format = GL33.glGetTexLevelParameteri(GL33.GL_TEXTURE_2D, 0, GL33.GL_TEXTURE_INTERNAL_FORMAT);// 获取纹理数据格式
		this.pixel_data_type = GL33.GL_UNSIGNED_BYTE;// 实际上此值与pixel_format相关，但是太多了懒得写，原版均为GL_UNSIGNED_BYTE
		this.resize(framebuffer.width(), framebuffer.height());
	}

	private void resize(int width, int height) {
		GL33.glDeleteBuffers(read_buffers);// 先删除之前的缓冲区
		for (int idx = 0; idx < 2; ++idx) {
			int buffer = GL33.glGenBuffers();
			GL33.glBindBuffer(GL33.GL_PIXEL_PACK_BUFFER, buffer);// 从纹理中读取像素
			GL33.glBufferData(GL33.GL_PIXEL_PACK_BUFFER, width * height * RGBA8_PIXEL_SIZE, GL33.GL_STREAM_READ);
			this.read_buffers[idx] = buffer;
		}
		GL33.glBindBuffer(GL33.GL_PIXEL_PACK_BUFFER, 0);
		this.recorded_width = width;
		this.recorded_height = height;
	}

	public static PixelsBuffer createFrom(Framebuffer framebuffer) {
		return new PixelsBuffer(framebuffer);
	}

	public int width() {
		return framebuffer.width();
	}

	public int height() {
		return framebuffer.height();
	}

	/**
	 * 映射读取到的帧缓冲颜色数据到内存
	 * 
	 * @return 是否映射成功，成功后再读取
	 */
	public boolean map() {
		int width = framebuffer.width();
		int height = framebuffer.height();
		if (width != this.recorded_width || height != this.recorded_height)
			this.resize(width, height);// 检测到窗口尺寸变化，则重新分配PBO
		int next_idx = (current_buffer_idx + 1) % 2;
		GL33.glBindBuffer(GL33.GL_PIXEL_PACK_BUFFER, read_buffers[next_idx]);// 下一个缓存先读取
		GL33.glReadPixels(0, 0, width, height, pixel_format, pixel_data_type, 0);// 读取帧缓冲颜色到PBO
		op_buffer = read_buffers[current_buffer_idx];
		GL33.glBindBuffer(GL33.GL_PIXEL_PACK_BUFFER, op_buffer);
		ByteBuffer pixels = GL33.glMapBuffer(GL33.GL_PIXEL_PACK_BUFFER, GL33.GL_READ_WRITE);
		this.pixels = pixels;
		current_buffer_idx = next_idx;
		return pixels != null;
	}

	public void unmap() {
		GL33.glUnmapBuffer(GL33.GL_PIXEL_PACK_BUFFER);
		GL33.glBindBuffer(GL33.GL_PIXEL_PACK_BUFFER, 0);// 解绑PBO
	}

	/**
	 * 将读取并修改后的缓存数据写入指定纹理
	 * 
	 * @param texture
	 */
	public void write(int texture) {
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture);
		GL33.glBindBuffer(GL33.GL_PIXEL_UNPACK_BUFFER, op_buffer);
		GL33.glTexSubImage2D(GL33.GL_TEXTURE_2D, 0, 0, 0, framebuffer.width(), framebuffer.height(), pixel_format, pixel_data_type, 0);// 从op_buffer拷贝数据
	}

	/**
	 * 将映射获取的颜色数据写入关联纹理
	 */
	public void write() {
		write(associated_texture);
	}

	public ColorRGBA getRGBA8Color(int x, int y) {
		return ColorRGBA.of(pixels.getInt((x + y * framebuffer.width()) * RGBA8_PIXEL_SIZE));
	}

	public void setRGBA8Color(int x, int y, ColorRGBA color) {
		pixels.putInt((x + y * framebuffer.width()) * RGBA8_PIXEL_SIZE, color.packBGRA());
	}
}
