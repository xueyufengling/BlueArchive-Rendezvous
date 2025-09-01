package lepus.phys;

public interface PhysScalar {
	public float value(float t);

	public static PhysScalar fixed(float s) {
		return (float t) -> s;
	}

	/**
	 * 可变化数值的时间无关标量
	 */
	public class TimeIndependent implements PhysScalar {
		public float value;

		public TimeIndependent(float init) {
			this.value = init;
		}

		public float value(float t) {
			return value;
		}
	}
}
