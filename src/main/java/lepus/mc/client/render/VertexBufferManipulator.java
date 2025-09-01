package lepus.mc.client.render;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

import lyra.klass.InnerKlass;
import lyra.lang.InternalUnsafe;
import lyra.object.ObjectManipulator;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 修改MeshData的顶点数据。<br>
 * 可用于在ByteBufferBuilder的build()储存顶点结果后使用此类修改顶点数据<br>
 */
@OnlyIn(Dist.CLIENT)
public class VertexBufferManipulator {
	private VertexFormat vertex_attributes;

	/**
	 * VAO信息，顶点各属性offset
	 */
	private int[] attribute_offsets;

	private MeshData mesh;

	/**
	 * 缓存和顶点相关信息
	 */
	private MeshData.DrawState buffer_info;

	/**
	 * mesh内部数据
	 */
	private long buffer;

	/**
	 * 顶点个数
	 */
	private int vertices_count;

	private VertexBufferManipulator(MeshData mesh) {
		this.mesh = mesh;
		this.buffer_info = (MeshData.DrawState) ObjectManipulator.getDeclaredMemberObject(mesh, "drawState");
		this.vertex_attributes = buffer_info.format();
		this.attribute_offsets = vertex_attributes.getOffsetsByElement();
		ByteBufferBuilder.Result vertexBuffer = (ByteBufferBuilder.Result) ObjectManipulator.getDeclaredMemberObject(mesh, "vertexBuffer");
		ByteBufferBuilder builder = InnerKlass.getEnclosingClassInstance(ByteBufferBuilder.class, vertexBuffer);
		long ptr = ObjectManipulator.getDeclaredMemberLong(builder, "pointer");
		long offset = ObjectManipulator.getDeclaredMemberInt(vertexBuffer, "offset");
		this.buffer = ptr + offset;
		this.vertices_count = buffer_info.vertexCount();
	}

	public static VertexBufferManipulator from(MeshData mesh) {
		return new VertexBufferManipulator(mesh);
	}

	public MeshData.DrawState bufferInfo() {
		return buffer_info;
	}

	/**
	 * 该缓冲的图元
	 * 
	 * @return
	 */
	public VertexFormat.Mode primitive() {
		return buffer_info.mode();
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
		return vertexIdx * this.vertex_attributes.getVertexSize() + this.attribute_offsets[vertexAttribute.id()];
	}

	/**
	 * 返回某个顶点的指定类型数据起始指针
	 * 
	 * @param vertexIdx
	 * @param vertexAttribute
	 * @return
	 */
	public final long vertexPointer(int vertexIdx, VertexFormatElement vertexAttribute) {
		return buffer + bufferIndex(vertexIdx, vertexAttribute);
	}

	public final int verticesCount() {
		return this.vertices_count;
	}

	/**
	 * 获取顶点属性信息
	 * 
	 * @return
	 */
	public final VertexFormat vertexAttributes() {
		return vertex_attributes;
	}

	/**
	 * 设置顶点位置
	 * 
	 * @param vertexIdx
	 * @param x         Minecraft坐标系中的坐标
	 * @param y
	 * @param z
	 * @return
	 */
	public VertexBufferManipulator setPos(int vertexIdx, float x, float y, float z) {
		int arrIdx = this.bufferIndex(vertexIdx, VertexFormatElement.POSITION);
		InternalUnsafe.putFloat(null, buffer + arrIdx, -x);
		InternalUnsafe.putFloat(null, buffer + arrIdx + 4, -z);// 注意Minecraft坐标系的y轴实际上是OpenGL的z轴，y和z顺序需要互换，且正方向相反，所有分量需要取反
		InternalUnsafe.putFloat(null, buffer + arrIdx + 8, -y);
		return this;
	}

	public float[] getPos(int vertexIdx) {
		int arrIdx = this.bufferIndex(vertexIdx, VertexFormatElement.POSITION);
		float[] pos = new float[3];
		pos[0] = -InternalUnsafe.getFloat(null, buffer + arrIdx);
		pos[1] = -InternalUnsafe.getFloat(null, buffer + arrIdx + 8);
		pos[2] = -InternalUnsafe.getFloat(null, buffer + arrIdx + 4);
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
	 * 尽管BufferBuilder可以使用float类型设置颜色，但实际储存依然是unsigned char。<br>
	 * 需要人为确定该Mesh有颜色属性，否则操作行为将引发不期望的数据属性被修改，结果未定义
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
		return setColor(vertexIdx, (int) (255 * r), (int) (255 * g), (int) (255 * b), (int) (255 * a));
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

	public static MeshData modifyVertexColor(MeshData mesh, ColorResolver resolver) {
		if (resolver != null) {
			VertexBufferManipulator buffer = VertexBufferManipulator.from(mesh);
			for (int idx = 0; idx < buffer.verticesCount(); ++idx) {// 遍历4个顶点
				int[] orig_color = buffer.getColor(idx);
				int[] new_color = resolver.color(orig_color[0], orig_color[1], orig_color[2], orig_color[3]);
				buffer.setColor(idx, new_color[0], new_color[1], new_color[2], new_color[3]);
			}
		}
		return mesh;
	}

	@FunctionalInterface
	public static interface NormalizedColorResolver {
		public float[] color(float r, float g, float b, float a);

		public static final NormalizedColorResolver NONE = (float orig_r, float orig_g, float orig_b, float orig_a) -> new float[] { orig_r, orig_g, orig_b, orig_a };

		/**
		 * RGBA均设置成固定颜色
		 * 
		 * @paramr
		 * @param g
		 * @param b
		 * @param a
		 * @return
		 */
		public static NormalizedColorResolver fixed(float r, float g, float b, float a) {
			return (float orig_r, float orig_g, float orig_b, float orig_a) -> new float[] { r, g, b, a };
		}

		/**
		 * RGB重新设置成固定颜色，alpha保持不变
		 * 
		 * @paramr
		 * @param g
		 * @param b
		 * @return
		 */
		public static NormalizedColorResolver fixedRGB(float r, float g, float b) {
			return (float orig_r, float orig_g, float orig_b, float orig_a) -> new float[] { r, g, b, orig_a };
		}

		public static NormalizedColorResolver fixedA(float a) {
			return (float orig_r, float orig_g, float orig_b, float orig_a) -> new float[] { orig_r, orig_g, orig_b, a };
		}
	}

	@FunctionalInterface
	public static interface PositionResolver {
		public float[] position(float orig_x, float orig_y, float orig_z);

		public static PositionResolver fixed(float x, float y, float z) {
			return (float orig_x, float orig_y, float orig_z) -> new float[] { x, y, z };
		}

		public static PositionResolver scale(float f) {
			return (float orig_x, float orig_y, float orig_z) -> new float[] { orig_x * f, orig_y * f, orig_z * f };
		}

		public static PositionResolver scaleXZ(float f) {
			return (float orig_x, float orig_y, float orig_z) -> new float[] { orig_x * f, orig_y, orig_z * f };
		}

		public static PositionResolver scaleY(float f) {
			return (float orig_x, float orig_y, float orig_z) -> new float[] { orig_x, orig_y * f, orig_z };
		}

		public static final PositionResolver NONE = (float orig_x, float orig_y, float orig_z) -> new float[] { orig_x, orig_y, orig_z };
	}

	public static MeshData modifyVertexPosition(MeshData mesh, PositionResolver resolver) {
		if (resolver != null) {
			VertexBufferManipulator buffer = VertexBufferManipulator.from(mesh);
			for (int idx = 0; idx < buffer.verticesCount(); ++idx) {// 遍历4个顶点
				float[] orig_pos = buffer.getPos(idx);
				float[] new_pos = resolver.position(orig_pos[0], orig_pos[1], orig_pos[2]);
				buffer.setPos(idx, new_pos[0], new_pos[1], new_pos[2]);
			}
		}
		return mesh;
	}

	@FunctionalInterface
	public static interface UvResolver {
		public float[] texcoord(float orig_u, float orig_v);

		public static final UvResolver NONE = (float orig_u, float orig_v) -> new float[] { orig_u, orig_v };
	}

	public static MeshData modifyVertexUv(MeshData mesh, UvResolver resolver) {
		if (resolver != null) {
			VertexBufferManipulator buffer = VertexBufferManipulator.from(mesh);
			for (int idx = 0; idx < buffer.verticesCount(); ++idx) {// 遍历4个顶点
				float[] orig_uv = buffer.getUv(idx);
				float[] new_uv = resolver.texcoord(orig_uv[0], orig_uv[1]);
				buffer.setUv(idx, new_uv[0], new_uv[1]);
			}
		}
		return mesh;
	}
}
