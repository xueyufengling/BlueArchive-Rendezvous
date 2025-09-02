package lepus.graphics;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL33;

public class VertexAttribute {
	public static class Info // 属性的信息
	{
		int index = -1;
		String name = null;
		int type = GL33.GL_NONE;
		int type_size = 0;
		boolean normalized = false;
		int num = 0;
		int offset = 0; // 在单个顶点中的位移(因为顶点各个属性长度可以不一样，故统一用字节数表示位移)
		int divisor = 0;// 几个实例共享数据

		Info(String info) {
			String[] attrib_str = info.split(":");
			String[] attrib_name_index = attrib_str[0].split("\\.");
			name = attrib_name_index[0]; // 属性名称
			if (attrib_name_index.length > 1)
				index = Integer.parseInt(attrib_str[1]);
			String attrib_data_info_str = attrib_str[1];
			int pos = 0;
			if (attrib_data_info_str.charAt(0) == 'n') // 需要规范化
			{
				pos = 1;
				normalized = true;
			}
			num = attrib_data_info_str.charAt(pos++) - '0';
			type = GL.getGLType(attrib_data_info_str.substring(pos));
			type_size = GL.getGLTypeSize(type);
			int type_str_len = 0;
			switch (type)// 可选divisor
			{
			case GL33.GL_BYTE:
			case GL33.GL_SHORT:
			case GL33.GL_INT:
			case GL33.GL_FLOAT:
			case GL33.GL_DOUBLE:
				type_str_len = 1;
				break;
			case GL33.GL_UNSIGNED_BYTE:
			case GL33.GL_UNSIGNED_SHORT:
			case GL33.GL_UNSIGNED_INT:
			case GL33.GL_2_BYTES:
			case GL33.GL_3_BYTES:
			case GL33.GL_4_BYTES:
				type_str_len = 2;
				break;
			}
			pos += type_str_len;
			if (attrib_data_info_str.charAt(pos++) == '/')// 有divisor
				divisor = Integer.parseInt(attrib_data_info_str.substring(pos));
		}

		public int getIndex() {
			return index;
		}

		public int getType() {
			return type;
		}

		public boolean getNormalized() {
			return normalized;
		}

		public int getNum() {
			return num;
		}

		public int getOffset() {
			return offset;
		}

		public String getName() {
			return name;
		}

		public int getDivisor() {
			return divisor;
		}
	}

	private int vertex_size;
	private final ArrayList<Info> attribs = new ArrayList<>();

	public VertexAttribute(String attrib_str) {
		String[] attrib_str_arr = attrib_str.split(","); // 析构时自动释放内存
		int offset = 0;
		for (int i = 0; i < attrib_str_arr.length; ++i) {
			Info attrib_info = new Info(attrib_str_arr[i]);
			attribs.add(attrib_info);
			if (attrib_info.index == -1)
				attrib_info.index = i;
			attrib_info.offset = offset;
			offset += attrib_info.num * attrib_info.type_size; // 下一个顶点（如果存在）的初始位置
		}
		vertex_size = offset;
	}

	public Info getVertexAttributeInfo(String attrib_name) {
		for (int i = 0; i < attribs.size(); ++i) {
			Info info = attribs.get(i);
			if (info.name.equals(attrib_name))
				return info;
		}
		return null; // 无效查询
	}

	void load(int vao) {
		GL33.glBindVertexArray(vao);// 在glBindBuffer()后、glDraw*()前调用，用于把顶点属性格式装载进指定的VAO中
		for (int i = 0; i < attribs.size(); ++i) {
			Info attrib = attribs.get(i);
			GL33.glVertexAttribPointer(attrib.index, attrib.num, attrib.type, attrib.normalized, vertex_size, attrib.offset);
			GL33.glEnableVertexAttribArray(attrib.index);
			if (attrib.divisor != 0)
				GL33.glVertexAttribDivisor(attrib.index, attrib.divisor);
		}
		GL33.glBindVertexArray(0);
	}

	public Info getVertexAttributeInfo(int index) {
		return attribs.get(index);
	}

	public int getVertexSize() {
		return vertex_size;
	}

	/**
	 * 设置第索引idx的顶点的指定属性的float数值
	 * 
	 * @param data
	 * @param idx
	 * @param name
	 * @param values
	 */
	public void setVertexAttribValue(ByteBuffer data, int idx, String name, float... values) {
		int base = this.vertex_size * idx + this.getVertexAttributeInfo(name).offset;
		for (int i = 0; i < values.length; ++i)
			data.putFloat(base + i * 4, values[i]);
	}

	public static final String ATTRIB_POSITION = "position";

	public static final String ATTRIB_TEXTURE_COORD = "texture_coord";

	public static final String ATTRIB_COLOR = "color";

	public static final VertexAttribute POSITION_TEX = new VertexAttribute(ATTRIB_POSITION + ".0:3f," + ATTRIB_TEXTURE_COORD + ".1:2f");

	public static final VertexAttribute POSITION_COLOR = new VertexAttribute(ATTRIB_POSITION + ".0:3f," + ATTRIB_COLOR + ".1:4f");

	public static final VertexAttribute POSITION_COLOR_TEX = new VertexAttribute(ATTRIB_POSITION + ",0:3f" + ATTRIB_COLOR + ".1:4f," + ATTRIB_TEXTURE_COORD + ".2:2f");
}
