package fw.client.render.vanilla;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import fw.client.render.renderable.Texture;
import net.minecraft.util.Mth;

/**
 * 生成常用形状的顶点数据
 */
public class RenderableObjects {

	/**
	 * 生成纹理的矩形四边形
	 * 
	 * @param texture
	 * @param width
	 * @param z
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public static RenderableObject quad(Texture texture, float scale, float z, float r, float g, float b, float a) {
		float width = texture.width() * scale;
		float height = texture.height() * scale;
		RenderableObject obj = new RenderableObject(texture.location()).loadBuffer();
		obj.addVertex(-width / 2, -height / 2, z, texture.u1(), texture.v1(), r, g, b, a);
		obj.addVertex(-width / 2, height / 2, z, texture.u1(), texture.v2(), r, g, b, a);
		obj.addVertex(width / 2, -height / 2, z, texture.u2(), texture.v1(), r, g, b, a);
		obj.addVertex(width / 2, height / 2, z, texture.u2(), texture.v2(), r, g, b, a);
		return obj.flushBuffer();
	}

	public static RenderableObject quad(Texture texture, float scale, float z, float a) {
		return quad(texture, scale, z, 1.0f, 1.0f, 1.0f, a);
	}

	public static RenderableObject quad(Texture texture, float z, float a) {
		return quad(texture, 1, z, a);
	}

	public static RenderableObject quad(Texture texture, float z) {
		return quad(texture, z, 1.0f);
	}

	/**
	 * 构造与原版形状一致的天空穹，地平线以上和以下需要分开构造，渲染时使用的是两个不同天空穹
	 * 
	 * @param y
	 * @return
	 */
	public static RenderableObject sky(float y, float radius, float r, float g, float b, float a) {
		float x_signed_r = Math.signum(y) * radius;
		RenderableObject skyVertices = new RenderableObject(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR).loadBuffer();
		skyVertices.addVertex(0, y, 0, r, g, b, a);// 天顶
		for (int i = -180; i <= 180; i += 45) {
			skyVertices.addVertex(x_signed_r * Mth.cos(i * (float) (Math.PI / 180.0)), y, radius * Mth.sin(i * (float) (Math.PI / 180.0)), r, g, b, a);
		}
		return skyVertices.flushBuffer();
	}

	/**
	 * 纯白不透明的天空穹，需要使用RenderSystem.setShaderColor()设置其颜色和透明度
	 * 
	 * @param y
	 * @param radius
	 * @return
	 */
	public static RenderableObject sky(float y, float radius) {
		return sky(y, radius, 1, 1, 1, 1);
	}

	/**
	 * 地平线上的可见天空穹
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public static RenderableObject skyAboveHorizonVanilla(float r, float g, float b, float a) {
		return sky(16, 512, r, g, b, a);
	}

	/**
	 * 地平线上的天空穹的缩放版本
	 * 
	 * @param scale
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public static RenderableObject scaledSkyAboveHorizonVanilla(float scale, float r, float g, float b, float a) {
		return sky(16 * scale, 512, r, g, b, a);
	}

	public static RenderableObject scaledSkyAboveHorizonVanilla(float scale) {
		return scaledSkyAboveHorizonVanilla(scale, 1, 1, 1, 1);
	}

	/**
	 * 地平线下的天空穹
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public static RenderableObject skyBelowHorizonVanilla(float r, float g, float b, float a) {
		return sky(-16, 512, r, g, b, a);
	}

	/**
	 * 地平线下的天空穹的缩放版本
	 * 
	 * @param scale
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public static RenderableObject scaledSkyBelowHorizonVanilla(float scale, float r, float g, float b, float a) {
		return sky(-16 * scale, 512, r, g, b, a);
	}

	public static RenderableObject scaledSkyBelowHorizonVanilla(float scale) {
		return scaledSkyAboveHorizonVanilla(scale, 1, 1, 1, 1);
	}
}
