package ba.world.terrain;

/**
 * 储存学生的各种地形优势程度
 */
public class TerrainAdvantage {
	/**
	 * 地形优势表
	 */
	private final TerrainEffect[] restraintMap;

	public TerrainAdvantage() {
		TerrainType[] terrainTypes = TerrainType.values();
		restraintMap = new TerrainEffect[terrainTypes.length];
		for (TerrainType terrainType : terrainTypes)
			restraintMap[terrainType.id] = TerrainEffect.Neutral;// 默认都是中性优势，无额外加成或减伤。
	}

	public TerrainAdvantage(TerrainEffect cityAdv, TerrainEffect desertAdv, TerrainEffect indoorAdv) {
		this();
		restraintMap[TerrainType.CityArea.id] = cityAdv;
		restraintMap[TerrainType.DesertArea.id] = desertAdv;
		restraintMap[TerrainType.IndoorArea.id] = indoorAdv;
	}

	/**
	 * 设置地形优势程度
	 * 
	 * @param terrain
	 * @param adv
	 * @return
	 */
	public TerrainAdvantage setTerrainEffect(TerrainType terrain, TerrainEffect adv) {
		restraintMap[terrain.id] = adv;
		return this;
	}

	/**
	 * 获取地形优势程度
	 * 
	 * @param terrain
	 * @return
	 */
	public TerrainEffect resolveTerrainEffect(TerrainType terrain) {
		return restraintMap[terrain.id];
	}

	/**
	 * 获取该地形下的伤害比例
	 * 
	 * @param terrain
	 * @return
	 */
	public float resolveDamageDealt(TerrainType terrain) {
		return resolveTerrainEffect(terrain).damageDealt;
	}

	/**
	 * 获取该地形下的掩护概率
	 * 
	 * @param terrain
	 * @return
	 */
	public float resolveShieldBlockRate(TerrainType terrain) {
		return resolveTerrainEffect(terrain).shieldBlockRate;
	}

	public static TerrainAdvantage from(TerrainEffect cityAdv, TerrainEffect desertAdv, TerrainEffect indoorAdv) {
		return new TerrainAdvantage(cityAdv, desertAdv, indoorAdv);
	}
}
