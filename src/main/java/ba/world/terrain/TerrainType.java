package ba.world.terrain;

public enum TerrainType {
	/**
	 * 市街地
	 */
	CityArea(0),

	/**
	 * 屋外
	 */
	DesertArea(1),

	/**
	 * 屋内
	 */
	IndoorArea(2);

	public final int id;

	private TerrainType(int id) {
		this.id = id;
	}
}
