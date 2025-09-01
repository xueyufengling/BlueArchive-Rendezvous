package lepus.phys;

import java.util.List;

import org.joml.Vector3f;

/**
 * 矢量表达式(数值计算器)，时间相关
 */
@FunctionalInterface
public interface PhysVector {
	/**
	 * 计算矢量在指定时时刻的值，如果与其他矢量相关，需要使用lambda表达式捕获
	 * 
	 * @param value 矢量函数
	 * @param t
	 * @return
	 */
	public Vector3f value(float t);

	public static PhysVector fixed(Vector3f v) {
		return (float t) -> v;
	};

	public static PhysVector fixed(float x, float y, float z) {
		return (float t) -> new Vector3f(x, y, z);
	};

	public static PhysVector zero() {
		return PhysVector.fixed(new Vector3f());
	};

	public default PhysScalar length() {
		return (float t) -> value(t).length();
	}

	/**
	 * 差分
	 * 
	 * @param dt 时间间隔
	 * @return
	 */
	public default PhysVector difference(float dt) {
		return (float t) -> this.value(t + dt).sub(this.value(t));
	}

	/**
	 * 线性差分求微分
	 * 
	 * @param dt
	 * @return
	 */
	public default PhysVector differential(float dt) {
		return (float t) -> this.value(t + dt).sub(this.value(t)).div(dt);
	}

	/**
	 * 动力系统式Riemann积分，给定t时刻的积分值，计算dt后积分的新值，计算结果将储存在lastStepResult
	 * 
	 * @param lastStepResult 上一步的积分结果
	 * @param dt             积分区间分段长度
	 * @return 新的矢量副本
	 */
	public default PhysVector integral(final Vector3f lastStepResult, float dt) {
		return new PhysVector() {
			private float lastT = Float.NaN;

			@Override
			public Vector3f value(float t) {
				if (t != lastT) {// 当连续对同一个t求值时只计算一次并将结果缓存返回，防止多次调用本函数时lastStepResult.add()重复叠加积分值
					this.lastT = t;
					lastStepResult.add(PhysVector.this.value(t).add(PhysVector.this.value(t + dt)).mul(0.5f * dt));
				}
				return lastStepResult;
			}
		};
	}

	/**
	 * 计算Riemann积分微元，即区间[t, t+dt]内的线性近似积分，区间长度为dt
	 * 
	 * @param dt
	 * @return
	 */
	public default PhysVector dIntegral(float dt) {
		return (float t) -> this.value(t + dt).add(this.value(t)).mul(0.5f * dt);
	}

	public default PhysVector add(PhysVector v) {
		return (float t) -> this.value(t).add(v.value(t));
	}

	public default PhysVector sub(PhysVector v) {
		return (float t) -> this.value(t).sub(v.value(t));
	}

	/**
	 * 反向
	 * 
	 * @return
	 */
	public default PhysVector inv() {
		return (float t) -> this.value(t).mul(-1.0f);
	}

	/**
	 * 点乘
	 * 
	 * @param v
	 * @return
	 */
	public default PhysScalar dot(PhysVector v) {
		return (float t) -> this.value(t).dot(v.value(t));
	}

	/**
	 * 矢量和，返回结果矢量将与vecs数组本身实时变化
	 * 
	 * @param vecs
	 * @return
	 */
	public static PhysVector resultant(List<PhysVector> vecs) {
		return (float t) -> {
			Vector3f result = new Vector3f();
			for (PhysVector v : vecs) {
				result.add(v.value(t));
			}
			return result;
		};
	}

	public static PhysVector resultant(List<PhysVector> vecs, Vector3f result) {
		return (float t) -> {
			result.set(0, 0, 0);
			for (PhysVector v : vecs) {
				result.add(v.value(t));
			}
			return result;
		};
	}
}
