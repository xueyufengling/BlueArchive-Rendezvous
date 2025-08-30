package lepus.graphics;

import java.awt.Color;

import net.minecraft.world.phys.Vec3;

public class ColorRGBA {
	public float r, g, b, a;

	public ColorRGBA(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public ColorRGBA(float[] rgba) {
		this.r = rgba[0];
		this.g = rgba[1];
		this.b = rgba[2];
		if (rgba.length > 3)
			this.a = rgba[3];
	}

	public ColorRGBA(int r, int g, int b, int a) {
		this.r = r / 255.0f;
		this.g = g / 255.0f;
		this.b = b / 255.0f;
		this.a = a / 255.0f;
	}

	public ColorRGBA(int[] rgba) {
		this.r = rgba[0] / 255.0f;
		this.g = rgba[1] / 255.0f;
		this.b = rgba[2] / 255.0f;
		if (rgba.length > 3)
			this.a = rgba[3] / 255.0f;
	}

	public ColorRGBA(Vec3 color) {
		this.r = (float) color.x;
		this.g = (float) color.y;
		this.b = (float) color.z;
		this.a = 1.0f;
	}

	public ColorRGBA(int pixel) {
		this.r = ((pixel >> 16) & 0xff) / 255.0f;
		this.g = ((pixel >> 8) & 0xff) / 255.0f;
		this.b = (pixel & 0xff) / 255.0f;
		this.a = ((pixel >> 24) & 0xff) / 255.0f;
	}

	public static ColorRGBA of(float r, float g, float b, float a) {
		return new ColorRGBA(r, g, b, a);
	}

	public static ColorRGBA of(float r, float g, float b) {
		return new ColorRGBA(r, g, b, 1.0f);
	}

	public static ColorRGBA of(int r, int g, int b, int a) {
		return new ColorRGBA(r, g, b, a);
	}

	public static ColorRGBA of(int r, int g, int b) {
		return new ColorRGBA(r, g, b, 255);
	}

	public static ColorRGBA of(Vec3 color) {
		return new ColorRGBA(color);
	}

	public static ColorRGBA of(int packed) {
		return new ColorRGBA(packed);
	}

	public static ColorRGBA of(float[] rgba) {
		return new ColorRGBA(rgba);
	}

	public static ColorRGBA of(int[] rgba) {
		return new ColorRGBA(rgba);
	}

	@Override
	public String toString() {
		return "{r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + "}";
	}

	/**
	 * 包装为int值
	 * 
	 * @return
	 */
	public int pack() {
		return pack(r, g, b, a);
	}

	/**
	 * OpenGL的RGBA8数据格式
	 * 
	 * @return
	 */
	public int packBGRA() {
		return pack(b, g, r, a);
	}

	public Vec3 vec3() {
		return new Vec3(r, g, b);
	}

	/**
	 * 灰度化
	 * 
	 * @return
	 */
	public float gray() {
		return 0.299f * r + 0.587f * g + 0.114f * b;
	}

	public int packRGB() {
		return pack(r, g, b);
	}

	public ColorRGBA add(ColorRGBA c) {
		return ColorRGBA.of(this.r + c.r, this.g + c.g, this.b + c.b, this.a + c.a);
	}

	public ColorRGBA add(Vec3 v) {
		return ColorRGBA.of((float) (this.r + v.x), (float) (this.g + v.y), (float) (this.b + v.z), this.a);
	}

	public ColorRGBA mul(ColorRGBA c) {
		return ColorRGBA.of(this.r * c.r, this.g * c.g, this.b * c.b, this.a * c.a);
	}

	public ColorRGBA mul(Vec3 v) {
		return ColorRGBA.of((float) (this.r * v.x), (float) (this.g * v.y), (float) (this.b * v.z), this.a);
	}

	public ColorRGBA mul(float f) {
		return ColorRGBA.of(this.r * f, this.g * f, this.b * f, this.a * f);
	}

	public ColorRGBA mul(float r, float g, float b, float a) {
		return ColorRGBA.of(this.r * r, this.g * g, this.b * b, this.a * a);
	}

	public ColorRGBA mulRGB(float f) {
		return ColorRGBA.of(this.r * f, this.g * f, this.b * f, this.a);
	}

	public ColorRGBA mulA(float f) {
		return ColorRGBA.of(this.r, this.g, this.b, this.a * f);
	}

	/**
	 * 反相
	 * 
	 * @return
	 */
	public ColorRGBA inv() {
		return ColorRGBA.of(1.0f - r, 1.0f - g, 1.0f - b, 1.0f - a);
	}

	public ColorRGBA correct() {
		if (r > 1)
			r = 1;
		if (r < 0)
			r = 0;
		if (g > 1)
			g = 1;
		if (g < 0)
			g = 0;
		if (b > 1)
			b = 1;
		if (b < 0)
			b = 0;
		if (a > 1)
			a = 1;
		if (a < 0)
			a = 0;
		return this;
	}

	/**
	 * 颜色插值
	 * 
	 * @param normalized 目标颜色比重
	 * @param dest       目标颜色
	 * @return
	 */
	public ColorRGBA interplote(float normalized, ColorRGBA dest) {
		return this.mul(1 - normalized).add(dest.mul(normalized));
	}

	public static final int[] parseAsInt(int pixel) {
		return new int[] {
				(pixel >> 16) & 0xff, // r
				(pixel >> 8) & 0xff, // g
				pixel & 0xff, // b
				(pixel >> 24) & 0xff// a
		};
	}

	public static final float[] parseAsFloat(int pixel) {
		return new float[] {
				((pixel >> 16) & 0xff) / 255.0f, // r
				((pixel >> 8) & 0xff) / 255.0f, // g
				(pixel & 0xff) / 255.0f, // b
				((pixel >> 24) & 0xff) / 255.0f// a
		};
	}

	public static final Vec3 parseAsVec3(int pixel) {
		return new Vec3(
				((pixel >> 16) & 0xff) / 255.0, // r
				((pixel >> 8) & 0xff) / 255.0, // g
				(pixel & 0xff) / 255.0 // b
		);
	}

	public static final int pack(int r, int g, int b, int a) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static final int pack(float r, float g, float b, float a) {
		return pack((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));
	}

	public static final int pack(int r, int g, int b) {
		return (r << 16) | (g << 8) | b;
	}

	public static final int pack(float r, float g, float b) {
		return pack((int) (r * 255), (int) (g * 255), (int) (b * 255));
	}

	public static final int pack(Vec3 rgb) {
		return ((int) (rgb.x * 255) << 16) | ((int) (rgb.y * 255) << 8) | (int) (rgb.z * 255);
	}

	public static final Vec3 normalizedVec3(Color color) {
		return new Vec3(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
	}

	public static final ColorRGBA WHITE = ColorRGBA.of(1.0f, 1.0f, 1.0f, 1.0f);

	public static final ColorRGBA BLACK = ColorRGBA.of(0.0f, 0.0f, 0.0f, 1.0f);

	public static final ColorRGBA ORANGE = ColorRGBA.of(1.0f, 0.647f, 0.0f, 1.0f);

	public static final ColorRGBA TRANSPARENT = ColorRGBA.of(0.0f, 0.0f, 0.0f, 0.0f);
}
