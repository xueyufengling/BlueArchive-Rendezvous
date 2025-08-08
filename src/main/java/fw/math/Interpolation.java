package fw.math;

import java.util.ArrayList;

import net.minecraft.world.level.Level;

public class Interpolation<V> {
	public double minT = 0;
	public double maxT = Level.TICKS_PER_DAY;

	@SuppressWarnings("hiding")
	public class Point<V> {
		public double t;
		public V value;

		public Point(double t, V value) {
			if (t < Interpolation.this.minT)
				t = Interpolation.this.minT;
			else if (t > Interpolation.this.maxT)
				t = Interpolation.this.maxT;
			this.t = t;
			this.value = value;
		}
	}

	public static enum OutOfRangeStrategy {
		REPEAT {
			/**
			 * t越界后t从最低值开始重新循环<br>
			 * 对于Level.getDayTime()得到的t为世界创建到现在的tick总数，需要相对于一天的tick数求余得到当前一天内的时间<br>
			 */
			@Override
			public double resolveT(double minT, double maxT, double t) {
				return (t - minT) % (maxT - minT);
			}

		},
		TRUNC {
			/**
			 * t越界后截断，保持t最低值或最高值
			 */
			@Override
			public double resolveT(double minT, double maxT, double t) {
				if (t > maxT)
					return maxT;
				else if (t < minT)
					return minT;
				else
					return t;
			}

		};

		public abstract double resolveT(double minT, double maxT, double t);
	}

	public static enum BoundaryPointStrategy {
		REPEAT {

			@Override
			public int minBoundaryPoint(int minIdx, int maxIdx) {
				return maxIdx;
			}

			@Override
			public int maxBoundaryPoint(int minIdx, int maxIdx) {
				return minIdx;
			}

			@Override
			public double minBoundaryDt(double minT, double maxT, double beginT, double endT, double t) {
				return maxT - beginT + (t - minT);
			}

			@Override
			public double minBoundaryPointDistance(double minT, double maxT, double beginT, double endT) {
				return maxT - beginT + (endT - minT);
			}

			@Override
			public double maxBoundaryDt(double minT, double maxT, double beginT, double endT, double t) {
				return t - beginT;
			}

			@Override
			public double maxBoundaryPointDistance(double minT, double maxT, double beginT, double endT) {
				return maxT - beginT + (endT - minT);
			}

		},
		TRUNC {

			@Override
			public int minBoundaryPoint(int minIdx, int maxIdx) {
				return minIdx;
			}

			@Override
			public int maxBoundaryPoint(int minIdx, int maxIdx) {
				return maxIdx;
			}

			@Override
			public double minBoundaryDt(double minT, double maxT, double beginT, double endT, double t) {
				return t - minT;
			}

			@Override
			public double minBoundaryPointDistance(double minT, double maxT, double beginT, double endT) {
				return endT - minT;
			}

			@Override
			public double maxBoundaryDt(double minT, double maxT, double beginT, double endT, double t) {
				return t - beginT;
			}

			@Override
			public double maxBoundaryPointDistance(double minT, double maxT, double beginT, double endT) {
				return maxT - beginT;
			}

		};

		public abstract int minBoundaryPoint(int minIdx, int maxIdx);

		public abstract double minBoundaryDt(double minT, double maxT, double beginT, double endT, double t);

		public abstract double minBoundaryPointDistance(double minT, double maxT, double beginT, double endT);

		public abstract int maxBoundaryPoint(int minIdx, int maxIdx);

		public abstract double maxBoundaryDt(double minT, double maxT, double beginT, double endT, double t);

		public abstract double maxBoundaryPointDistance(double minT, double maxT, double beginT, double endT);
	}

	public OutOfRangeStrategy tOORStrategy = OutOfRangeStrategy.REPEAT;
	public BoundaryPointStrategy pointOORStrategy = BoundaryPointStrategy.REPEAT;

	private ArrayList<Point<V>> points = new ArrayList<>();

	/**
	 * 设置插值点
	 * 
	 * @param t
	 * @param point
	 * @return
	 */
	public Interpolation<V> append(double t, V point) {
		points.add(this.new Point<V>(t, point));
		points.sort((Point<V> p1, Point<V> p2) -> (int) (p1.t - p2.t));
		return this;
	}

	public static <V> Interpolation<V> begin(double t, V point) {
		return new Interpolation<V>().append(t, point);
	}

	public static class Result<V> {
		public final double dt;
		public final double pointDistance;
		public final V begin;
		public final V end;

		public Result(double dt, double pointDistance, V begin, V end) {
			this.dt = dt;
			this.pointDistance = pointDistance;
			this.begin = begin;
			this.end = end;
		}
	}

	public Result<V> interplote(double t) {
		switch (points.size()) {
		case 0:
			return null;
		case 1: {
			Point<V> single = points.get(0);
			return new Result<V>(t, maxT - minT, single.value, single.value);
		}
		default: {
			Point<V> begin = null;
			Point<V> end = null;
			t = tOORStrategy.resolveT(minT, maxT, t);
			double dt = 0;
			double pointDistance = 0;
			int size = points.size();
			for (int idx = 0; idx < size; ++idx) {
				Point<V> point = points.get(idx);
				if (t < point.t) {
					end = point;
					if (idx == 0) {// 仅当 minT < t < points[0].t 时触发
						begin = points.get(pointOORStrategy.minBoundaryPoint(0, size - 1));
						dt = pointOORStrategy.minBoundaryDt(minT, maxT, begin.t, end.t, t);
						pointDistance = pointOORStrategy.minBoundaryPointDistance(minT, maxT, begin.t, end.t);
					} else {
						dt = t - begin.t;
						pointDistance = end.t - begin.t;
					}
					break;
				} else {
					begin = point;
					if (idx == size - 1) {
						end = points.get(pointOORStrategy.maxBoundaryPoint(0, size - 1));
						dt = pointOORStrategy.maxBoundaryDt(minT, maxT, begin.t, end.t, t);
						pointDistance = pointOORStrategy.maxBoundaryPointDistance(minT, maxT, begin.t, end.t);
					}
					continue;
				}
			}
			return new Result<V>(dt, pointDistance, begin.value, end.value);
		}
		}
	}
}