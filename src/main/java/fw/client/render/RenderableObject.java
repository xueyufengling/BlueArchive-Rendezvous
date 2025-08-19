package fw.client.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;

import fw.client.render.renderable.Renderable;
import fw.client.render.renderable.Texture;
import fw.resources.ResourceLocations;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * OpenGL可渲染对象
 */
public class RenderableObject implements Renderable {
	/**
	 * buffer使用策略
	 */
	private VertexBuffer.Usage strategy;

	/**
	 * 图元
	 */
	private VertexFormat.Mode primitive;

	/**
	 * 绘制纹理
	 */
	private ResourceLocation texture;

	/**
	 * 数据源
	 */
	private BufferBuilder vertex_data_source;

	/**
	 * 顶点数据和索引
	 */
	private VertexBuffer vertices;

	public RenderableObject(VertexBuffer.Usage strategy, VertexFormat.Mode primitive, ResourceLocation texture) {
		this.strategy = strategy;
		this.primitive = primitive;
		this.texture = texture;
	}

	public RenderableObject(VertexFormat.Mode primitive, ResourceLocation texture) {
		this(VertexBuffer.Usage.STATIC, primitive, texture);
	}

	public RenderableObject(ResourceLocation texture) {
		this(VertexFormat.Mode.TRIANGLE_STRIP, texture);
	}

	public RenderableObject(VertexBuffer.Usage strategy, VertexFormat.Mode primitive, String texture) {
		this(strategy, primitive, ResourceLocations.build(texture));
	}

	public RenderableObject(VertexFormat.Mode primitive, String texture) {
		this(VertexBuffer.Usage.STATIC, primitive, texture);
	}

	public RenderableObject(String texture) {
		this(VertexFormat.Mode.TRIANGLE_STRIP, texture);
	}

	public RenderableObject setDrawStrategy(VertexBuffer.Usage strategy) {
		this.strategy = strategy;
		return this;
	}

	public RenderableObject setPrimitive(VertexFormat.Mode primitive) {
		this.primitive = primitive;
		return this;
	}

	public RenderableObject setTexture(ResourceLocation texture) {
		this.texture = texture;
		return this;
	}

	public RenderableObject begin() {
		if (vertex_data_source == null)
			vertex_data_source = TesselatorInstance.begin(primitive, DefaultVertexFormat.POSITION_TEX_COLOR);// 默认三角形带图元，VAO为顶点位置-UV坐标-顶点颜色
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
	public RenderableObject addVertex(float x, float y, float z, float u, float v, float r, float g, float b, float a) {
		TesselatorInstance.posUvColorVertex(vertex_data_source, x, y, z, u, v, r, g, b, a);
		return this;
	}

	public RenderableObject addVertex(float x, float y, float z, float u, float v) {
		return addVertex(x, y, z, u, v, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	public RenderableObject end() {
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
		RenderSystem.setShaderTexture(0, texture);// 绑定ColorMap
		RenderSystem.enableBlend();
		this.vertices.bind();
		this.vertices.drawWithShader(frustumMatrix, projectionMatrix, GameRenderer.getPositionTexColorShader());
		VertexBuffer.unbind();
	}

	public void render(PoseStack poseStack, Matrix4f projectionMatrix) {
		render(poseStack.last().pose(), projectionMatrix);
	}

	public void render(PoseStack poseStack) {
		render(poseStack.last().pose(), RenderSystem.getProjectionMatrix());
	}

	public void render() {
		render(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix());
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

	/**
	 * 生成纹理的矩形四边形
	 * 
	 * @param texture
	 * @param width
	 * @param z
	 * @return
	 */
	public static RenderableObject quad(Texture texture, float width, float z) {
		float hw_ratio = texture.height() / texture.width();
		float height = width * hw_ratio;
		RenderableObject obj = new RenderableObject(texture.location()).begin();
		obj.addVertex(-width / 2, -height / 2, z, texture.u1(), texture.v1());
		obj.addVertex(-width / 2, height / 2, z, texture.u1(), texture.v2());
		obj.addVertex(width / 2, -height / 2, z, texture.u2(), texture.v1());
		obj.addVertex(width / 2, height / 2, z, texture.u2(), texture.v2());
		return obj.end();
	}

	public static RenderableObject quad(Texture texture, float width) {
		return quad(texture, width, 10);
	}

	public static RenderableObject quad(Texture texture) {
		return quad(texture, texture.width());
	}
}
