package fw.client.render;

import java.util.Arrays;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

import lyra.klass.InnerKlass;
import lyra.lang.InternalUnsafe;
import lyra.object.ObjectManipulator;

/**
 * 修改MeshData的顶点数据。<br>
 * 可用于在ByteBufferBuilder的build()储存顶点结果后使用此类修改顶点数据
 */
public class VertexBufferManipulator {
	private VertexFormat vertex_format;

	/**
	 * VAO信息，顶点各属性offset
	 */
	private int[] offsetsByElement;

	private MeshData mesh;

	/**
	 * mesh内部数据
	 */
	private long buffer;

	private VertexBufferManipulator(MeshData mesh, VertexFormat format) {
		this.vertex_format = format;
		this.offsetsByElement = format.getOffsetsByElement();
		this.mesh = mesh;
		ByteBufferBuilder.Result vertexBuffer = (ByteBufferBuilder.Result) ObjectManipulator.getDeclaredMemberObject(mesh, "vertexBuffer");
		ByteBufferBuilder builder = InnerKlass.getEnclosingClassInstance(ByteBufferBuilder.class, vertexBuffer);
		long ptr = ObjectManipulator.getDeclaredMemberLong(builder, "pointer");
		long offset = ObjectManipulator.getDeclaredMemberInt(vertexBuffer, "offset");
		this.buffer = ptr + offset;
	}

	public static VertexBufferManipulator from(MeshData mesh, VertexFormat format) {
		return new VertexBufferManipulator(mesh, format);
	}

	/**
	 * mesh内部储存数据的指针
	 * 
	 * @return
	 */
	public long buffer() {
		return buffer;
	}

	/**
	 * 指定类型数据的低位字节索引
	 * 
	 * @param vertexIdx
	 * @param vertexAttribute
	 * @return
	 */
	public final int bufferIndex(int vertexIdx, VertexFormatElement vertexAttribute) {
		return vertexIdx * this.vertex_format.getVertexSize() + this.offsetsByElement[vertexAttribute.id()];
	}

	/**
	 * 设置顶点位置
	 * 
	 * @param vertexIdx
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public VertexBufferManipulator setPos(int vertexIdx, float x, float y, float z) {
		int arrIdx = this.bufferIndex(vertexIdx, VertexFormatElement.POSITION);
		InternalUnsafe.putFloat(null, buffer + arrIdx, x);
		InternalUnsafe.putFloat(null, buffer + arrIdx + 4, y);
		InternalUnsafe.putFloat(null, buffer + arrIdx + 8, z);
		return this;
	}

	public float[] getPos(int vertexIdx) {
		int arrIdx = this.bufferIndex(vertexIdx, VertexFormatElement.POSITION);
		float[] pos = new float[3];
		pos[0] = InternalUnsafe.getFloat(null, buffer + arrIdx);
		pos[1] = InternalUnsafe.getFloat(null, buffer + arrIdx + 4);
		pos[2] = InternalUnsafe.getFloat(null, buffer + arrIdx + 8);
		return pos;
	}

	/**
	 * 设置纹理UV坐标
	 * 
	 * @param vertexIdx
	 * @param u
	 * @param v
	 * @return
	 */
	public VertexBufferManipulator setUv(int vertexIdx, float u, float v) {
		int arrIdx = this.bufferIndex(vertexIdx, VertexFormatElement.UV0);
		InternalUnsafe.putFloat(null, buffer + arrIdx, u);
		InternalUnsafe.putFloat(null, buffer + arrIdx + 4, v);
		return this;
	}

	public float[] getUv(int vertexIdx) {
		int arrIdx = this.bufferIndex(vertexIdx, VertexFormatElement.UV0);
		float[] uv = new float[2];
		uv[0] = InternalUnsafe.getFloat(null, buffer + arrIdx);
		uv[1] = InternalUnsafe.getFloat(null, buffer + arrIdx + 4);
		return uv;
	}

	/**
	 * 尽管BufferBuilder可以使用float类型设置颜色，但实际储存依然是unsigned char
	 * 
	 * @param vertexIdx
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public VertexBufferManipulator setColor(int vertexIdx, int r, int g, int b, int a) {
		int arrIdx = this.bufferIndex(vertexIdx, VertexFormatElement.COLOR);
		InternalUnsafe.putByte(null, buffer + arrIdx, (byte) r);
		InternalUnsafe.putByte(null, buffer + arrIdx + 1, (byte) g);
		InternalUnsafe.putByte(null, buffer + arrIdx + 2, (byte) b);
		InternalUnsafe.putByte(null, buffer + arrIdx + 3, (byte) a);
		return this;
	}

	/**
	 * 读取指定顶点的颜色
	 * 
	 * @param vertexIdx
	 * @return
	 */
	public int[] getColor(int vertexIdx) {
		int arrIdx = this.bufferIndex(vertexIdx, VertexFormatElement.COLOR);
		int[] color = new int[4];
		color[0] = InternalUnsafe.getByte(null, buffer + arrIdx);
		color[1] = InternalUnsafe.getByte(null, buffer + arrIdx + 1);
		color[2] = InternalUnsafe.getByte(null, buffer + arrIdx + 2);
		color[3] = InternalUnsafe.getByte(null, buffer + arrIdx + 3);
		return color;
	}

	public VertexBufferManipulator setColor(int vertexIdx, float r, float g, float b, float a) {
		return setColor(vertexIdx, 255 * r, 255 * g, 255 * b, 255 * a);
	}

	public MeshData mesh() {
		return mesh;
	}

	@FunctionalInterface
	public static interface ColorResolver {
		public int[] color(int orig_r, int orig_g, int orig_b, int orig_a);

		public static ColorResolver fixed(int r, int g, int b, int a) {
			return (int orig_r, int orig_g, int orig_b, int orig_a) -> new int[] { r, g, b, a };
		}

		public static ColorResolver fixed(float r, float g, float b, float a) {
			return (int orig_r, int orig_g, int orig_b, int orig_a) -> new int[] { (int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255) };
		}

		public static ColorResolver fixedRGB(int r, int g, int b) {
			return (int orig_r, int orig_g, int orig_b, int orig_a) -> new int[] { r, g, b, orig_a };
		}

		public static ColorResolver fixedRGB(float r, float g, float b) {
			return (int orig_r, int orig_g, int orig_b, int orig_a) -> new int[] { (int) (r * 255), (int) (g * 255), (int) (b * 255), orig_a };
		}

		public static ColorResolver fixedA(int a) {
			return (int orig_r, int orig_g, int orig_b, int orig_a) -> new int[] { orig_r, orig_g, orig_b, a };
		}

		public static ColorResolver fixedA(float a) {
			return (int orig_r, int orig_g, int orig_b, int orig_a) -> new int[] { orig_r, orig_g, orig_b, (int) (a * 255) };
		}

		public static final ColorResolver NONE = (int orig_r, int orig_g, int orig_b, int orig_a) -> new int[] { orig_r, orig_g, orig_b, orig_a };
	}

	public static MeshData modifyVertexColor(MeshData mesh, VertexFormat format, ColorResolver resolver) {
		if (resolver != null) {
			VertexBufferManipulator buffer = VertexBufferManipulator.from(mesh, format);
			for (int idx = 0; idx < 4; ++idx) {// 遍历4个顶点
				int[] orig_color = buffer.getColor(idx);
				System.err.println("orig_color=" + Arrays.toString(orig_color));
				int[] new_color = resolver.color(orig_color[0], orig_color[1], orig_color[2], orig_color[3]);
				buffer.setColor(idx, new_color[0], new_color[1], new_color[2], new_color[3]);
			}
		}
		return mesh;
	}
}
