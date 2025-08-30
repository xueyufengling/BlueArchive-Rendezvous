package lepus.phys.astronomy;

/**
 * 天球坐标系
 */
public class EquatorialCoordinateSystem {
	/**
	 * 自转倾角，也是黄赤交角ε<br>
	 * 真地平为起始，向上为正向下为负
	 */
	private double obliguity;

	/**
	 * 北天极
	 */
	private double north_celestial_pole;

	/**
	 * 南天极
	 */
	private double south_celestial_pole;

	/**
	 * 真天顶
	 */
	public static final double true_zenith = 90;

	private EquatorialCoordinateSystem(double obliguity, double a) {
		this.obliguity = obliguity;
		this.north_celestial_pole = true_zenith + obliguity;
		this.south_celestial_pole = obliguity - true_zenith;
	}

	// public double zenith() {

	// }
}
