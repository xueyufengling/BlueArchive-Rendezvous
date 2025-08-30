package lepus.mc.client.render.renderable;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mojang.blaze3d.vertex.PoseStack;

import lepus.mc.client.render.gui.GuiGraphicsContext;
import lepus.mc.resources.ResourceLocations;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 纹理图片，内部UV坐标使用原点在左下角，向右向上为正的UV坐标系
 */
@OnlyIn(Dist.CLIENT)
public class Texture implements Cloneable {

	@Override
	public Texture clone() {
		try {
			return (Texture) super.clone();
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private ResourceLocation location;
	/**
	 * 图像原始宽度
	 */
	private int img_width;
	/**
	 * 图像原始高度
	 */
	private int img_height;
	/**
	 * 渲染宽度
	 */
	private float width;
	/**
	 * 渲染高度
	 */
	private float height;
	private float u1;
	private float v1;
	private float u2;
	private float v2;
	private float depth;// 绘制深度，深度高的在上层

	public ResourceLocation location() {
		return location;
	}

	public float u1() {
		return u1;
	}

	public float v1() {
		return v1;
	}

	public float u2() {
		return u2;
	}

	public float v2() {
		return v2;
	}

	public float depth() {
		return depth;
	}

	public int imageHeight() {
		return img_height;
	}

	public int imageWidth() {
		return img_width;
	}

	public float height() {
		return height;
	}

	public float width() {
		return width;
	}

	public static BufferedImage bufferedImage(ResourceLocation loc) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream("/assets/" + loc.getNamespace() + '/' + loc.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public static BufferedImage bufferedImage(String loc) {
		return bufferedImage(ResourceLocations.build(loc));
	}

	private void initImageSize(ResourceLocation loc) {
		BufferedImage img = bufferedImage(loc);
		if (img == null)
			throw new IllegalArgumentException("Texture " + loc.toString() + " doesn't exists");
		this.img_width = img.getWidth();
		this.img_height = img.getHeight();
		img.flush();
	}

	protected Texture setAreaWh(float u1, float v1, float w, float h) {
		this.u1 = u1;
		this.v1 = v1;
		this.u2 = u1 + w;
		this.v2 = v1 + h;
		this.width = Math.abs(w);
		this.height = Math.abs(h);
		return this;
	}

	protected Texture setAreaWh(float w, float h) {
		this.u2 = u1 + w;
		this.v2 = v1 + h;
		this.width = Math.abs(w);
		this.height = Math.abs(h);
		return this;
	}

	protected Texture setArea(float u1, float v1, float u2, float v2) {
		this.u1 = u1;
		this.v1 = v1;
		this.u2 = u2;
		this.v2 = v2;
		this.width = Math.abs(u2 - u1);
		this.height = Math.abs(v2 - v1);
		return this;
	}

	public Texture(ResourceLocation loc, float u1, float v1, float u2, float v2, float depth) {
		this.location = loc;
		this.depth = depth;
		initImageSize(loc);
		setArea(u1, v1, u2, v2);
	}

	public Texture(ResourceLocation loc, float u1, float v1, float u2, float v2) {
		this(loc, u1, v1, u2, v2, 0.0f);
	}

	public Texture(ResourceLocation loc, int u1, int v1, int u2, int v2, float depth) {
		this.location = loc;
		this.depth = depth;
		initImageSize(loc);
		this.u1 = (float) u1 / this.img_width;
		this.v1 = (float) v1 / this.img_height;
		this.u2 = (float) u2 / this.img_width;
		this.v2 = (float) v2 / this.img_height;
	}

	public Texture(ResourceLocation loc, int u1, int v1, int u2, int v2) {
		this(loc, u1, v1, u2, v2, 0.0f);
	}

	public Texture(ResourceLocation loc, float depth) {
		this(loc, 0.0f, 0.0f, 1.0f, 1.0f, depth);
	}

	public Texture(ResourceLocation loc) {
		this(loc, 0.0f, 0.0f, 1.0f, 1.0f);
	}

	public static Texture of(ResourceLocation loc) {
		return new Texture(loc);
	}

	public static Texture of(String loc) {
		return new Texture(ResourceLocations.build(loc));
	}

	public static Texture of(ResourceLocation loc, float u1, float v1, float u2, float v2) {
		return new Texture(loc, u1, v1, u2, v2);
	}

	public static Texture of(String loc, float u1, float v1, float u2, float v2) {
		return new Texture(ResourceLocations.build(loc), u1, v1, u2, v2);
	}

	public static Texture of(ResourceLocation loc, float u1, float v1, float u2, float v2, float depth) {
		return new Texture(loc, u1, v1, u2, v2, depth);
	}

	public static Texture of(String loc, float u1, float v1, float u2, float v2, float depth) {
		return new Texture(ResourceLocations.build(loc), u1, v1, u2, v2, depth);
	}

	public static Texture of(ResourceLocation loc, int u1, int v1, int u2, int v2) {
		return new Texture(loc, u1, v1, u2, v2);
	}

	public static Texture of(String loc, int u1, int v1, int u2, int v2) {
		return new Texture(ResourceLocations.build(loc), u1, v1, u2, v2);
	}

	public static Texture of(ResourceLocation loc, int u1, int v1, int u2, int v2, float depth) {
		return new Texture(loc, u1, v1, u2, v2, depth);
	}

	public static Texture of(String loc, int u1, int v1, int u2, int v2, float depth) {
		return new Texture(ResourceLocations.build(loc), u1, v1, u2, v2, depth);
	}

	public static Texture of(ResourceLocation loc, float depth) {
		return new Texture(loc, depth);
	}

	public static Texture of(String loc, float depth) {
		return new Texture(ResourceLocations.build(loc), depth);
	}

	/**
	 * 构建指定的纹理列表，其中的Texture的深度depth第一个参数为0，而后依次递增
	 * 
	 * @param locs
	 * @return
	 */
	public static Texture[] of(ResourceLocation... locs) {
		Texture[] textures = new Texture[locs.length];
		for (int i = 0; i < locs.length; ++i)
			textures[i] = new Texture(locs[i], i);
		return textures;
	}

	public static Texture[] of(String... locs) {
		Texture[] textures = new Texture[locs.length];
		for (int i = 0; i < locs.length; ++i)
			textures[i] = new Texture(ResourceLocations.build(locs[i]), i);
		return textures;
	}

	/**
	 * 裁剪图像，使用GUI坐标系，左上角为原点，向右向下为正
	 * 
	 * @param x1
	 * @param y1
	 * @param width
	 * @param height
	 * @return
	 */
	public Texture clip(float x1, float y1, float width, float height) {
		float v1 = 1.0f - y1;
		return this.clone().setArea(x1, v1, x1 + width, v1 - height);
	}

	public Texture clip(int x1, int y1, int width, int height) {
		int v1 = this.img_height - y1;
		return this.clone().setArea(x1, v1, x1 + width, v1 - height);
	}

	public final float normalizedX(int x) {
		return ((float) this.img_width) / x;
	}

	public final float normalizedY(int y) {
		return ((float) this.img_height) / y;
	}

	public final int absoluteX(float normalizedX) {
		return (int) (normalizedX * this.img_width);
	}

	public final int absoluteY(float normalizedY) {
		return (int) (normalizedY * this.img_height);
	}

	@Override
	public String toString() {
		return location.toString();
	}

	/**
	 * 纹理局部区域实例<br>
	 */
	public class Area extends Renderable2D.Instance {

		Area(float offset_x, float offset_y, float width_scale, float height_scale) {
			super(offset_x, offset_y, width_scale, height_scale);
		}

		Area(float x1_offset, float y1_offset) {
			super(x1_offset, y1_offset);
		}

		Area() {
			super();
		}

		@Override
		public Renderable2D.Instance setRenderingSize(float x, float y) {
			this.width_scale = x / Texture.this.width;
			this.height_scale = y / Texture.this.height;
			return this;
		}

		@Override
		public String toString() {
			return "{texture=" + Texture.this.toString() + ", begin_offset=(" + offset_x + ", " + offset_y + ")}";
		}

		static Area[] of(Texture... textures) {
			Area[] layers = new Area[textures.length];
			for (int i = 0; i < textures.length; ++i)
				layers[i] = textures[i].new Area();
			return layers;
		}

		public void render(PoseStack poseStack, float x1, float y1) {
			GuiGraphicsContext.blitImage(poseStack, Texture.this.location, x1 + offset_x, x1 + Texture.this.width * width_scale, y1 + offset_y, y1 + Texture.this.height * height_scale, Texture.this.depth, Texture.this.u1, Texture.this.u2, Texture.this.v1, Texture.this.v2);
		}
	}

	public Area areaOf(float x1_offset, float y1_offset, float width_scale, float height_scale) {
		return this.new Area(x1_offset, y1_offset, width_scale, height_scale);
	}

	public Area areaOf(float x1_offset, float y1_offset) {
		return this.new Area(x1_offset, y1_offset);
	}

	public Area areaOf() {
		return this.new Area();
	}
}
