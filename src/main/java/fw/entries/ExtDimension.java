package fw.entries;

import fw.core.registry.DatagenHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class ExtDimension {
	/**
	 * DimensionType等价类
	 */
	public static class Type {
		public static final DatagenHolder<DimensionType> register(String name, DimensionType dimType) {
			return DatagenHolder.of(Registries.DIMENSION_TYPE, name, dimType);
		}
	}

	public static class Stem {
		public static final DatagenHolder<LevelStem> register(String name, DatagenHolder.ValueSource<LevelStem> levelStem) {
			return DatagenHolder.of(Registries.LEVEL_STEM, name, levelStem);
		}
	}
}
