package fw.terrain.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import fw.codec.derived.MapCodecHolder;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

@AsDataField
public abstract class ExtBiomeSource extends BiomeSource implements MapCodecHolder<BiomeSource> {
	@Override
	@SuppressWarnings({ "unchecked" })
	public MapCodec<? extends BiomeSource> codec() {
		return MapCodecHolder.super.codec();
	}

	@CodecEntry
	protected List<Holder<Biome>> possible_biomes_list;

	/**
	 * 仅数据生成时序列化使用
	 * 
	 * @param context
	 * @param possibleBiomeKeys
	 */
	public ExtBiomeSource(BootstrapContext<?> context, List<String> possibleBiomeKeys) {
		MapCodecHolder.super.construct(BiomeSource.class);
		resolvePossibleBiomes(context, possibleBiomeKeys);
	}

	public ExtBiomeSource(BootstrapContext<?> context, String... possibleBiomeKeys) {
		this(context, List.of(possibleBiomeKeys));
	}

	/**
	 * 实际运行时反序列化使用
	 * 
	 * @param possibleBiomesList
	 */
	@CodecTarget
	public ExtBiomeSource(List<Holder<Biome>> possibleBiomesList) {
		MapCodecHolder.super.construct(BiomeSource.class);
		this.possible_biomes_list = possibleBiomesList;
	}

	/**
	 * 从子类中读取possibleBiomeHolders列表，如果为null则根据possibleBiomeKeys创建。
	 * 
	 * @param bootstrapContext
	 * @param possibleBiomeKeys 可能的生物群系的key列表，如果为null则扫描目标对象的静态List<String>字段
	 */
	private void resolvePossibleBiomes(BootstrapContext<?> bootstrapContext, List<String> possibleBiomeKeys) {
		if (possible_biomes_list == null) {
			possible_biomes_list = new ArrayList<>();
			for (String key : possibleBiomeKeys) {
				possible_biomes_list.add(ExtBiome.datagenStageHolder(bootstrapContext, key));
			}
		}
	}

	/**
	 * 该方法返回的结果将在MC中缓存，理论上每次进入世界只调用一次，因此本类不再缓存Stream。<br>
	 * 仅运行时调用，且无法在运行时访问MappedRegistry，必须通过datagen就确定好所有可能的生物群系。
	 */
	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return this.possible_biomes_list.parallelStream();
	}

	/**
	 * 获取任意一个生物群系的Holder
	 * 
	 * @param key
	 * @return
	 */
	protected static final Holder<Biome> biome(String key) {
		return ExtBiome.getBiome(key);
	}

	/**
	 * 获取本mod的生物群系Holder
	 * 
	 * @param key
	 * @return
	 */
	protected static final Holder<Biome> modBiome(String key) {
		return ExtBiome.modBiome(key);
	}
}
