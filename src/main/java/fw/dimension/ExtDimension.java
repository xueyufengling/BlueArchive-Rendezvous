package fw.dimension;

import com.mojang.serialization.MapCodec;

import fw.core.registry.RegistryFactory;
import fw.datagen.DatagenHolder;
import fw.resources.ResourceKeyBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.Level;
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

		/**
		 * 从LevelStem中获取Level的ResourceKey。<br>
		 * Level的ResourceKey是在MinecraftServer.createLevels()中根据LevelStem生成而来的。<br>
		 * 
		 * @param levelStemHolder
		 * @return
		 */
		public static final ResourceKey<Level> levelKey(DatagenHolder<LevelStem> levelStemHolder) {
			return levelStemHolder.resourceKey(Registries.DIMENSION);
		}

		/**
		 * 从LevelStem的ResourceKey中获取Level的ResourceKey
		 * 
		 * @param levelStem
		 * @return
		 */
		public static final ResourceKey<Level> levelKey(ResourceKey<LevelStem> levelStem) {
			return ResourceKeyBuilder.build(Registries.DIMENSION, levelStem.location());
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
	 * 密度函数DensityFunction注册相关。<br>
	 * DimensionFunctionType在使用CodecAutogen注解自动生成CODEC时已经自动注册，如果没有使用该注解生成，需要手动使用registerType()将CODEC注册。<br>
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

		/**
		 * DENSITY_FUNCTION注册表在数据生成阶段使用时，只能向BootstrapContext注册，不能向MappedRegistry注册，因为数据生成时不存在该注册表，只有运行时才有。<br>
		 * 运行时需要调用该注册项的值来进行计算，因此运行时也要注册该条目。
		 * 
		 * @param name
		 * @param func
		 * @return
		 */
		public static final DatagenHolder<DensityFunction> register(String name, DensityFunction func) {
			return DatagenHolder.of(Registries.DENSITY_FUNCTION, name, func);
		}

	}
}
