package fw.terrain;

import com.mojang.serialization.MapCodec;

import fw.core.registry.DatagenHolder;
import fw.core.registry.RegistryFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ExtDimension {
	/**
	 * DimensionType等价类
	 */
	public static class Type {
		public static final DatagenHolder<DimensionType> register(String name, DimensionType dimType) {
			return DatagenHolder.of(Registries.DIMENSION_TYPE, name, dimType);
		}
	}

	/**
	 * 地形生成器
	 */
	public static class Stem {
		public static final DatagenHolder<LevelStem> register(String name, DatagenHolder.ValueSource<LevelStem> levelStem) {
			return DatagenHolder.of(Registries.LEVEL_STEM, name, levelStem);
		}
	}

	/**
	 * 地形生成噪声
	 */
	public static class Noise {
		public static final DatagenHolder<NoiseGeneratorSettings> register(String name, DatagenHolder.ValueSource<NoiseGeneratorSettings> noiseSettings) {
			return DatagenHolder.of(Registries.NOISE_SETTINGS, name, noiseSettings);
		}
	}

	/**
	 * 密度函数
	 */
	public static class Df {
		/**
		 * 注册自定义密度函数MapCodec，例如末地岛屿密度函数。见DensityFunctions::bootstrap
		 * 
		 * @param <T>
		 * @param name
		 * @param dfTypeCodec
		 * @return
		 */
		public static final <T extends DensityFunction> DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<T>> registerType(String name, MapCodec<T> dfTypeCodec) {
			return RegistryFactory.DENSITY_FUNCTION_TYPE.register(name, () -> dfTypeCodec);
		}

		/**
		 * 注册自定义密度函数KeyDispatchDataCodec，工作原理是从KeyDispatchDataCodec中获取MapCodec并注册，这两个实际上是等价的。
		 * 
		 * @param <T>
		 * @param name
		 * @param dfKeyCodec
		 * @return
		 */
		public static final <T extends DensityFunction> DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<T>> registerType(String name, KeyDispatchDataCodec<T> dfKeyCodec) {
			return registerType(name, dfKeyCodec.codec());
		}

		public static final DeferredHolder<DensityFunction, ? extends DensityFunction> register(String name, DensityFunction func) {
			return RegistryFactory.DENSITY_FUNCTION.register(name, () -> func);
		}

	}
}
