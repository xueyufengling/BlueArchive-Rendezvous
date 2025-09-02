package lepus.mc.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;

import lepus.graphics.ColorRGBA;
import lepus.mc.client.render.renderable.Renderable;
import lepus.mc.resources.ResourceLocations;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * OpenGL可渲染对象
 */
@OnlyIn(Dist.CLIENT)
public class VanillaRenderable implements Renderable {
	/**
	 * buffer使用策略
	 */
	VertexBuffer.Usage strategy;

	/**
	 * 图元
	 */
	VertexFormat.Mode primitive;

	/**
	 * VAO
	 */
	VertexFormat vertex_attributes;

	/**
	 * 绘制纹理
	 */
	ResourceLocation texture;

	/**
	 * 数据源
	 */
	BufferBuilder vertex_data_source;

	/**
	 * 顶点数据和索引
	 */
	VertexBuffer vertices;

	public VanillaRenderable(VertexBuffer.Usage strategy, VertexFormat.Mode primitive, VertexFormat vertex_attributes, ResourceLocation texture) {
		this.strategy = strategy;
		this.primitive = primitive;
		this.setVertexAttributes(vertex_attributes);
		this.texture = texture;
	}

	public VanillaRenderable(VertexFormat.Mode primitive, VertexFormat vertex_attributes, ResourceLocation texture) {
		this(VertexBuffer.Usage.STATIC, primitive, vertex_attributes, texture);
	}

	public VanillaRenderable(VertexFormat vertex_attributes, ResourceLocation texture) {
		this(VertexFormat.Mode.TRIANGLE_STRIP, vertex_attributes, texture);
	}

	public VanillaRenderable(ResourceLocation texture) {
		this(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_TEX_COLOR, texture);
	}

	public VanillaRenderable(VertexBuffer.Usage strategy, VertexFormat.Mode primitive, VertexFormat vertex_attributes) {
		this(strategy, primitive, vertex_attributes, (ResourceLocation) null);
	}

	public VanillaRenderable(VertexFormat.Mode primitive, VertexFormat vertex_attributes) {
		this(VertexBuffer.Usage.STATIC, primitive, vertex_attributes);
	}

	public VanillaRenderable(VertexFormat.Mode primitive) {
		this(primitive, DefaultVertexFormat.POSITION_COLOR);
	}

	public VanillaRenderable(VertexFormat vertex_attributes) {
		this(VertexFormat.Mode.TRIANGLE_STRIP, vertex_attributes);
	}

	/**
	 * 只使用位置-颜色着色器
	 */
	public VanillaRenderable() {
		this(VertexFormat.Mode.TRIANGLE_STRIP);
	}

	public VanillaRenderable(VertexBuffer.Usage strategy, VertexFormat.Mode primitive, VertexFormat vertex_attributes, String texture) {
		this(strategy, primitive, vertex_attributes, ResourceLocations.build(texture));
	}

	public VanillaRenderable(VertexFormat.Mode primitive, VertexFormat vertex_attributes, String texture) {
		this(VertexBuffer.Usage.STATIC, primitive, vertex_attributes, texture);
	}

	public VanillaRenderable(String texture) {
		this(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_TEX_COLOR, texture);
	}

	public VanillaRenderable setDrawStrategy(VertexBuffer.Usage strategy) {
		this.strategy = strategy;
		return this;
	}

	public VanillaRenderable setPrimitive(VertexFormat.Mode primitive) {
		this.primitive = primitive;
		return this;
	}

	public VanillaRenderable setTexture(ResourceLocation texture) {
		this.texture = texture;
		return this;
	}

	@FunctionalInterface
	static interface RenderFunc {
		public void render(VanillaRenderable obj, Matrix4f frustumMatrix, Matrix4f projectionMatrix);

		public static RenderFunc POSITION_TEX_COLOR = (VanillaRenderable obj, Matrix4f frustumMatrix, Matrix4f projectionMatrix) -> {
			obj.vertices.bind();
			RenderSystem.setShaderTexture(0, obj.texture);// 绑定ColorMap
			obj.vertices.drawWithShader(frustumMatrix, projectionMatrix, GameRenderer.getPositionTexColorShader());
			VertexBuffer.unbind();
		};

		public static RenderFunc POSITION_COLOR = (VanillaRenderable obj, Matrix4f frustumMatrix, Matrix4f projectionMatrix) -> {
			obj.vertices.bind();
			obj.vertices.drawWithShader(frustumMatrix, projectionMatrix, GameRenderer.getPositionColorShader());
			VertexBuffer.unbind();
		};

		public static RenderFunc POSITION_TEX = (VanillaRenderable obj, Matrix4f frustumMatrix, Matrix4f projectionMatrix) -> {
			obj.vertices.bind();
			RenderSystem.setShaderTexture(0, obj.texture);// 绑定ColorMap
			obj.vertices.drawWithShader(frustumMatrix, projectionMatrix, GameRenderer.getPositionTexShader());
			VertexBuffer.unbind();
		};
	}

	private RenderFunc render_func;

	/**
	 * 设置顶点属性和对应的着色器
	 * 
	 * @param vertex_attributes
	 * @return
	 */
	public VanillaRenderable setVertexAttributes(VertexFormat vertex_attributes) {
		this.vertex_attributes = vertex_attributes;
		if (vertex_attributes == DefaultVertexFormat.POSITION_TEX_COLOR)
			this.render_func = RenderFunc.POSITION_TEX_COLOR;
		else if (vertex_attributes == DefaultVertexFormat.POSITION_COLOR)
			this.render_func = RenderFunc.POSITION_COLOR;
		else if (vertex_attributes == DefaultVertexFormat.POSITION_TEX)
			this.render_func = RenderFunc.POSITION_TEX;
		else
			throw new UnsupportedOperationException("Vertex attribute " + vertex_attributes + " is not supported.");
		return this;
	}

	/**
	 * 开始加载数据
	 * 
	 * @return
	 */
	public VanillaRenderable loadBuffer() {
		if (vertex_data_source == null)
			vertex_data_source = TesselatorInstance.begin(primitive, vertex_attributes);// 默认三角形带图元，VAO为顶点位置-UV坐标-顶点颜色
		return this;
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
	 * @return
	 */
	public VanillaRenderable addVertex(float x, float y, float z, float u, float v, float r, float g, float b, float a) {
		TesselatorInstance.posUvColorVertex(vertex_data_source, x, y, z, u, v, r, g, b, a);
		return this;
	}

	public VanillaRenderable addVertex(float x, float y, float z, float r, float g, float b, float a) {
		TesselatorInstance.posColorVertex(vertex_data_source, x, y, z, r, g, b, a);
		return this;
	}

	public VanillaRenderable addVertex(float x, float y, float z, float u, float v) {
		TesselatorInstance.posUvVertex(vertex_data_source, x, y, z, u, v);
		return this;
	}

	/**
	 * 加载数据结束并刷入缓存
	 * 
	 * @return
	 */
	public VanillaRenderable flushBuffer() {
		this.invalidateData();
		this.bufferData();
		this.vertex_data_source = null;
		return this;
	}

	/**
	 * 渲染
	 * 
	 * @param frustumMatrix    视野矩阵
	 * @param projectionMatrix 投影矩阵
	 */
	public void render(Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
		render_func.render(this, frustumMatrix, projectionMatrix);
	}

	public void render(PoseStack poseStack, Matrix4f projectionMatrix) {
		render_func.render(this, poseStack.last().pose(), projectionMatrix);
	}

	public void render(PoseStack poseStack) {
		render_func.render(this, poseStack.last().pose(), RenderSystem.getProjectionMatrix());
	}

	public void render() {
		render_func.render(this, RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix());
	}

	/**
	 * 刷入顶点数据
	 */
	private void bufferData() {
		if (vertices == null)
			vertices = new VertexBuffer(strategy);
		vertices.bind();
		vertices.upload(vertex_data_source.buildOrThrow());
		VertexBuffer.unbind();
	}

	/**
	 * 弃用顶点数据，需要重新刷入数据才能渲染
	 */
	public void invalidateData() {
		if (vertices != null) {
			vertices.close();
			vertices = null;
		}
	}

	public static class Instance {
		VanillaRenderable object;

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

		Instance(VanillaRenderable object, Matrix4f transform, ColorRGBA shader_uniform_color, boolean visible) {
			this.object = object;
			this.transform = transform;
			this.shader_uniform_color = shader_uniform_color;
			this.visible = visible;
		}

		Instance(VanillaRenderable object, Matrix4f transform, boolean visible) {
			this(object, transform, ColorRGBA.WHITE, visible);
		}

		Instance(VanillaRenderable object, ColorRGBA shader_uniform_color, boolean visible) {
			this(object, new Matrix4f(), shader_uniform_color, visible);
		}

		Instance(VanillaRenderable object, boolean visible) {
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

		public void render(Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
			if (visible && object != null) {// 没有可渲染对象则直接返回
				float[] prevColor = RenderSystem.getShaderColor();
				RenderSystem.setShaderColor(shader_uniform_color.r, shader_uniform_color.g, shader_uniform_color.b, shader_uniform_color.a);
				Matrix4f mat = new Matrix4f(frustumMatrix);
				mat.mul(this.transform);
				object.render(mat, projectionMatrix);
				RenderSystem.setShaderColor(prevColor[0], prevColor[1], prevColor[2], prevColor[3]);// 恢复顶点颜色
			}
		}

		public void render(PoseStack poseStack, Matrix4f projectionMatrix) {
			if (visible) {// 即便该实例没有可渲染对象，也要对poseStack应用变换
				float[] prevColor = RenderSystem.getShaderColor();
				RenderSystem.setShaderColor(shader_uniform_color.r, shader_uniform_color.g, shader_uniform_color.b, shader_uniform_color.a);
				poseStack.pushPose();
				poseStack.mulPose(this.transform);// 应用节点变换
				if (object != null)// 有绑定可渲染对象则渲染
					object.render(poseStack.last().pose(), projectionMatrix);
				poseStack.popPose();
				RenderSystem.setShaderColor(prevColor[0], prevColor[1], prevColor[2], prevColor[3]);// 恢复顶点颜色
			}
		}

		public void render(PoseStack poseStack) {
			render(poseStack, RenderSystem.getProjectionMatrix());
		}

		public void render() {
			render(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix());
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
