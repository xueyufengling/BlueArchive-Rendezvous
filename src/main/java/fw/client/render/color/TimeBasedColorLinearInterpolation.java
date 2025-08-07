package fw.client.render.color;

import java.util.ArrayList;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TimeBasedColorLinearInterpolation {
	public static class ColorPoint {
		public int time;
		public int r, g, b, a;

		public static final ColorPoint BLACK = new ColorPoint(0, 0, 0, 0, 0);

		ColorPoint(int t, int r, int g, int b, int a) {
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
			this.time = t;
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}

		ColorPoint(long t, int r, int g, int b, int a) {
			this((int) t, r, g, b, a);
		}

		public final int pack() {
			return (a << 24) | (r << 16) | (g << 8) | b;
		}

		public final Vec3 vec3i() {
			return new Vec3(r, g, b);
		}

		public final Vec3 vec3f() {
			return new Vec3(r / 255.0, g / 255.0, b / 255.0);
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
		points.add(new ColorPoint(time, r, g, b, a));
		points.sort((ColorPoint p1, ColorPoint p2) -> p1.time - p2.time);
		return this;
	}

	public TimeBasedColorLinearInterpolation append(int time, int r, int g, int b) {
		return append(time, r, g, b, 0);
	}

	public TimeBasedColorLinearInterpolation append(int time, int gray) {
		return append(time, gray, gray, gray);
	}

	public static TimeBasedColorLinearInterpolation begin(int time, int r, int g, int b, int a) {
		return new TimeBasedColorLinearInterpolation().append(time, r, g, b, a);
	}

	public static TimeBasedColorLinearInterpolation begin(int time, int r, int g, int b) {
		return begin(time, r, g, b, 0);
	}

	public static TimeBasedColorLinearInterpolation begin(int time, int gray) {
		return begin(time, gray, gray, gray);
	}

	public ColorPoint interplote(long time) {
		switch (points.size()) {
		case 0:
			return ColorPoint.BLACK;
		case 1: {
			ColorPoint single = points.get(0);
			return new ColorPoint(time, single.r, single.g, single.b, single.a);
		}
		default: {
			ColorPoint start = points.get(0);
			ColorPoint end = null;
			if (time < 0)
				time = 0;
			else
				time %= Level.TICKS_PER_DAY;// time为世界创建到现在的tick总数，需要相对于一天的tick数求余得到当前一天内的时间
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
			return new ColorPoint(time, r, g, b, a);
		}
		}
	}

	/**
	 * 根据时间进行插值
	 * 
	 * @param time
	 * @return
	 */
	public int interplotePacked(long time) {
		return interplote(time).pack();
	}

	public Vec3 interploteVec3i(long time) {
		return interplote(time).vec3i();
	}

	public Vec3 interploteVec3f(long time) {
		return interplote(time).vec3f();
	}
}