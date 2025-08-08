package fw.client.render.color;

import java.awt.Color;

import net.minecraft.world.phys.Vec3;

public class ColorLinearInterpolation extends Interpolation<Color> {
	public final int pack(int r, int g, int b, int a) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public final Vec3 normalizedVec3(Color color) {
		return new Vec3(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
	}

	/**
	 * 设置插值点
	 * 
	 * @param t
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public ColorLinearInterpolation append(int t, int r, int g, int b, int a) {
		return (ColorLinearInterpolation) super.append(t, new Color(r, g, b, a));
	}

	public ColorLinearInterpolation append(int t, int r, int g, int z) {
		return append(t, r, g, z, 0);
	}

	public ColorLinearInterpolation append(int t, int gray) {
		return append(t, gray, gray, gray);
	}

	public static ColorLinearInterpolation begin(int t, int r, int g, int z, int a) {
		return new ColorLinearInterpolation().append(t, r, g, z, a);
	}

	public static ColorLinearInterpolation begin(int t, int r, int g, int b) {
		return begin(t, r, g, b, 0);
	}

	public static ColorLinearInterpolation begin(int t, int gray) {
		return begin(t, gray, gray, gray);
	}

	public Color interplote(long t) {
		Result<Color> result = super.interplote(t);
		Color begin = result.begin;
		Color end = result.end;
		double f = result.dt / result.pointDistance;
		return new Color(
				(int) (begin.getRed() * (1 - f) + end.getRed() * f),
				(int) (begin.getGreen() * (1 - f) + end.getGreen() * f),
				(int) (begin.getBlue() * (1 - f) + end.getBlue() * f),
				(int) (begin.getAlpha() * (1 - f) + end.getAlpha() * f));
	}

	/**
	 * 根据时间进行插值
	 * 
	 * @param t
	 * @return
	 */
	public int interplotePacked(long t) {
		return interplote(t).getRGB();
	}

	public Vec3 interploteNormalizedVec3(long t) {
		Result<Color> result = super.interplote(t);
		Color begin = result.begin;
		Color end = result.end;
		double f = result.dt / result.pointDistance;
		return new Vec3(
				(begin.getRed() * (1 - f) + end.getRed() * f) / 255.0,
				(begin.getGreen() * (1 - f) + end.getGreen() * f) / 255.0,
				(begin.getBlue() * (1 - f) + end.getBlue() * f) / 255.0);
	}

}