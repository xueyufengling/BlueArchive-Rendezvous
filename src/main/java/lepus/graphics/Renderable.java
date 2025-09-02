package lepus.graphics;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;

import lepus.graphics.shader.Shader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * OpenGL可渲染对象
 */
@OnlyIn(Dist.CLIENT)
public class Renderable {
	/**
	 * buffer使用策略
	 */
	private int strategy;

	/**
	 * 图元
	 */
	private int primitive;

	/**
	 * VAO
	 */
	private VertexAttribute vertex_attributes;

	/**
	 * 数据源
	 */
	private ByteBuffer vertex_data;

	private ByteBuffer element_data;

	private int vao;

	/**
	 * 顶点数据
	 */
	private int vbo;

	/**
	 * 顶点索引
	 */
	private int ebo;

	private int vertex_count;

	private int element_count;

	ArrayList<Texture2D> textures;

	private Shader shader;

	public Renderable(int strategy, int primitive, VertexAttribute vertex_attributes) {
		this.strategy = strategy;
		this.primitive = primitive;
		this.vertex_attributes = vertex_attributes;
	}

	public static final int DEFAULT_DRAW_STATEGY = GL33.GL_STATIC_DRAW;
	public static final int DEFAULT_PRIMITIVE = GL33.GL_TRIANGLE_STRIP;
	public static final VertexAttribute DEFAULT_VERTEX_ATTRIBUTE = VertexAttribute.POSITION_COLOR_TEX;

	public Renderable(int primitive, VertexAttribute vertex_attributes) {
		this(DEFAULT_DRAW_STATEGY, primitive, vertex_attributes);
	}

	public Renderable(VertexAttribute vertex_attributes) {
		this(DEFAULT_PRIMITIVE, vertex_attributes);
	}

	public Renderable() {
		this(DEFAULT_PRIMITIVE, DEFAULT_VERTEX_ATTRIBUTE);
	}

	private void useTextures() {
		for (int i = 0; i < textures.size(); ++i)
			textures.get(i).use(i);
	}

	public int getVertexCount() {
		return vertex_count;
	}

	public int getElementCount() {
		return element_count;
	}

	public Renderable newBufferData(int vertex_count, int element_count) {
		vertex_data = ByteBuffer.allocateDirect(vertex_count * vertex_attributes.getVertexSize());
		element_data = ByteBuffer.allocateDirect(element_count * 4);
		return this;
	}

	public Renderable newBufferData() {
		return newBufferData(4 * 1024, 8 * 1024);
	}

	/**
	 * 将顶点和索引数据传给GPU并且绑定好VAO
	 * 
	 * @return
	 */
	public Renderable bufferVertexData() {
		if (vertex_count != 0) {
			this.invalidateData();
			vao = GL33.glGenVertexArrays();
			GL33.glBindVertexArray(vao);
			vbo = GL33.glGenBuffers();
			GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo);
			GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertex_data.slice(0, vertex_count * vertex_attributes.getVertexSize()), strategy);
			if (element_data != null && element_count != 0) {
				ebo = GL33.glGenBuffers();
				GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, ebo);
				GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, element_data.slice(0, element_count * 4), strategy);
			}
			vertex_attributes.load(vao);
			GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
			GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
		return this;
	}

	public void setVertexPosition(int idx, float x, float y, float z) {
		vertex_attributes.setVertexAttribValue(this.vertex_data, idx, VertexAttribute.ATTRIB_POSITION, x, y, z);
	}

	public void setVertexColor(int idx, float r, float g, float b, float a) {
		vertex_attributes.setVertexAttribValue(this.vertex_data, idx, VertexAttribute.ATTRIB_COLOR, r, g, b, a);
	}

	public void setVertexTexcoord(int idx, float u, float v) {
		vertex_attributes.setVertexAttribValue(this.vertex_data, idx, VertexAttribute.ATTRIB_TEXTURE_COORD, u, v);
	}

	public void setElement(int idx, int vertex_idx) {
		this.element_data.putInt(idx * 4, vertex_idx);
	}

	/**
	 * 添加顶点
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param u
	 * @param v
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return 当前添加的这个顶点的索引
	 */
	public int addVertex(float x, float y, float z, float r, float g, float b, float a, float u, float v) {
		this.setVertexPosition(vertex_count, x, y, z);
		this.setVertexColor(vertex_count, r, g, b, a);
		this.setVertexTexcoord(vertex_count, u, v);
		return vertex_count++;
	}

	public int addVertex(float x, float y, float z, float r, float g, float b, float a) {
		this.setVertexPosition(vertex_count, x, y, z);
		this.setVertexColor(vertex_count, r, g, b, a);
		this.vertex_count++;
		return vertex_count++;
	}

	public int addVertex(float x, float y, float z, float u, float v) {
		this.setVertexPosition(vertex_count, x, y, z);
		this.setVertexTexcoord(vertex_count, u, v);
		this.vertex_count++;
		return vertex_count++;
	}

	public int addElement(int idx) {
		this.setElement(element_count, idx);
		return element_count++;
	}

	/**
	 * 渲染
	 */
	public void render() {
		shader.use();
		useTextures();
		GL33.glBindVertexArray(vao);
		if (ebo != 0)
			GL33.glDrawElements(primitive, element_count, GL33.GL_UNSIGNED_INT, 0);
		else
			GL33.glDrawArrays(primitive, 0, vertex_count);
	}

	/**
	 * MC兼容渲染
	 * 
	 * @param frustumMatrix    视野矩阵
	 * @param projectionMatrix 投影矩阵
	 */
	public void render(Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
		if (frustumMatrix != null)
			shader.setUniform("u_ModelViewMatrix", false, frustumMatrix);
		if (projectionMatrix != null)
			shader.setUniform("u_ProjectionMatrix", false, projectionMatrix);
		this.render();
	}

	/**
	 * 弃用顶点数据，需要重新刷入数据才能渲染
	 */
	public void invalidateData() {
		GL33.glDeleteBuffers(vbo);
		vbo = 0;
		GL33.glDeleteBuffers(ebo);
		ebo = 0;
		GL33.glDeleteVertexArrays(vao);
		vao = 0;
		vertex_data = null;
		element_data = null;
	}

	public static class Instance {
		Renderable object;

		/**
		 * 该节点的变换矩阵，此变换矩阵将应用在所有子节点及其嵌套节点上<br>
		 * 默认恒等矩阵，不进行任何变换
		 */
		Matrix4f transform;

		/**
		 * 着色器颜色，不同的vertex_attributes采用的着色器不同，其着色器颜色互相独立<br>
		 * 该属性是否有效取决于着色器是否具有或使用uniform颜色，对于原版着色器来说都具备，但大部分光影包完全不使用，此时该属性无效
		 */
		ColorRGBA shader_uniform_color;

		/**
		 * 对象可见性，不可见则不渲染
		 */
		boolean visible = true;

		Instance(Renderable object, Matrix4f transform, ColorRGBA shader_uniform_color, boolean visible) {
			this.object = object;
			this.transform = transform;
			this.shader_uniform_color = shader_uniform_color;
			this.visible = visible;
		}

		Instance(Renderable object, Matrix4f transform, boolean visible) {
			this(object, transform, ColorRGBA.WHITE, visible);
		}

		Instance(Renderable object, ColorRGBA shader_uniform_color, boolean visible) {
			this(object, new Matrix4f(), shader_uniform_color, visible);
		}

		Instance(Renderable object, boolean visible) {
			this(object, new Matrix4f(), ColorRGBA.WHITE, visible);
		}

		public static Instance empty(Matrix4f transform, ColorRGBA shader_uniform_color, boolean visible) {
			return new Instance(null, transform, shader_uniform_color, visible);
		}

		public static Instance empty(Matrix4f transform, boolean visible) {
			return new Instance(null, transform, ColorRGBA.WHITE, visible);
		}

		public static Instance empty(ColorRGBA shader_uniform_color, boolean visible) {
			return new Instance(null, new Matrix4f(), shader_uniform_color, visible);
		}

		public static Instance empty(boolean visible) {
			return new Instance(null, new Matrix4f(), ColorRGBA.WHITE, visible);
		}

		/**
		 * 设置着色器颜色，该颜色将点乘顶点颜色得到最终颜色
		 * 
		 * @param shader_uniform_color
		 * @return
		 */
		public Instance setShaderUniformColor(ColorRGBA shader_uniform_color) {
			this.shader_uniform_color = shader_uniform_color;
			return this;
		}

		public ColorRGBA getShaderUniformColor() {
			return shader_uniform_color;
		}

		public Instance setTransform(Matrix4f transform) {
			this.transform = transform;
			return this;
		}

		public Matrix4f getTransform() {
			return transform;
		}

		public Instance setVisible(boolean visible) {
			this.visible = visible;
			return this;
		}

		public boolean isVisible() {
			return this.visible;
		}
	}

	/**
	 * 为该渲染对象生成新的实例
	 * 
	 * @param transform
	 * @param shader_uniform_color
	 * @return
	 */
	public Instance newInstance(Matrix4f transform, ColorRGBA shader_uniform_color) {
		return new Instance(this, transform, shader_uniform_color, true);
	}

	public Instance newInstance(Matrix4f transform) {
		return new Instance(this, transform, true);
	}

	public Instance newInstance(ColorRGBA shader_uniform_color) {
		return new Instance(this, shader_uniform_color, true);
	}

	public Instance newInstance() {
		return new Instance(this, true);
	}
}
