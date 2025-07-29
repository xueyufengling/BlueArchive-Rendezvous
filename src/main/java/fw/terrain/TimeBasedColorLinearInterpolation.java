package fw.terrain;

import java.util.ArrayList;

import net.minecraft.world.level.Level;

public class TimeBasedColorLinearInterpolation {
	private static class ColorPoint {
		int time;
		int r, g, b, a;

		ColorPoint(int t, int r, int g, int b, int a) {
			this.time = t;
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}
	}

	private ArrayList<ColorPoint> points = new ArrayList<>();

	/**
	 * 设置插值点
	 * 
	 * @param time
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public TimeBasedColorLinearInterpolation append(int time, int r, int g, int b, int a) {
		if (time < 0)
			time = 0;
		else if (time > Level.TICKS_PER_DAY)
			time = Level.TICKS_PER_DAY;
		if (r < 0)
			r = 0;
		else if (r > 255)
			r = 255;
		if (g < 0)
			g = 0;
		else if (g > 255)
			g = 255;
		if (b < 0)
			b = 0;
		else if (b > 255)
			b = 255;
		if (a < 0)
			a = 0;
		else if (a > 255)
			a = 255;
		points.add(new ColorPoint(time, r, g, b, a));
		points.sort((ColorPoint p1, ColorPoint p2) -> p1.time - p2.time);
		return this;
	}

	public static TimeBasedColorLinearInterpolation begin(int time, int r, int g, int b, int a) {
		return new TimeBasedColorLinearInterpolation().append(time, r, g, b, a);
	}

	/**
	 * 根据时间进行插值
	 * 
	 * @param time
	 * @return
	 */
	public int interplote(long time) {
		System.err.println(time);
		switch (points.size()) {
		case 0:
			return 0;
		case 1: {
			ColorPoint single = points.get(0);
			return single.a << 24 | single.r << 16 | single.g << 8 | single.b;
		}
		default: {
			ColorPoint start = null;
			ColorPoint end = null;
			if (time < 0)
				time = 0;
			else if (time > Level.TICKS_PER_DAY)
				time = Level.TICKS_PER_DAY;
			for (int idx = 0; idx < points.size(); ++idx) {
				ColorPoint current = points.get(idx);
				if (current.time <= time) {
					start = current;
					continue;
				} else {
					end = current;
					break;
				}
			}
			float f = 0;
			if (end == null) {
				end = points.get(0);// 从头开始循环
				float d = Level.TICKS_PER_DAY - start.time + end.time;
				if (time > end.time)
					f = (time - start.time) / d;
				else
					f = (Level.TICKS_PER_DAY - start.time + time) / d;
			} else
				f = ((float) (time - start.time)) / (end.time - start.time);
			int r = (int) (start.r + (end.r - start.r) * f);
			int g = (int) (start.g + (end.g - start.g) * f);
			int b = (int) (start.b + (end.b - start.b) * f);
			int a = (int) (start.a + (end.a - start.a) * f);
			return a << 24 | r << 16 | g << 8 | b;
		}
		}
	}
}