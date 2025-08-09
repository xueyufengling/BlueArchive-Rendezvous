package fw.terrain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;

import fw.codec.CodecHolder;
import fw.codec.annotation.CodecEntry;
import lyra.klass.GenericTypes;
import lyra.klass.KlassWalker;
import lyra.object.Placeholders;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

public abstract class ExtBiomeSource extends BiomeSource implements CodecHolder<BiomeSource> {
	@Override
	public MapCodec<? extends BiomeSource> codec() {
		return CodecHolder.super.codec();
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
		CodecHolder.super.construct(BiomeSource.class);
		resolvePossibleBiomes(context, possibleBiomeKeys);
	}

	public ExtBiomeSource(BootstrapContext<?> context) {
		this(context, null);
	}

	/**
	 * 实际运行时反序列化使用
	 * 
	 * @param possibleBiomesList
	 */
	public ExtBiomeSource(List<Holder<Biome>> possibleBiomesList) {
		CodecHolder.super.construct(BiomeSource.class);
		this.possible_biomes_list = possibleBiomesList;
	}

	/**
	 * 从子类中读取possibleBiomeHolders列表，如果为null则根据possibleBiomeKeys创建。
	 * 
	 * @param bootstrapContext
	 * @param possibleBiomeKeys 可能的生物群系的key列表，如果为null则扫描目标对象的静态List<String>字段
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void resolvePossibleBiomes(BootstrapContext<?> bootstrapContext, List<String> possibleBiomeKeys) {
		if (possible_biomes_list == null) {
			Placeholders.TypeWrapper<List<String>> wrapper = Placeholders.TypeWrapper.wrap(possibleBiomeKeys);
			if (possibleBiomeKeys == null) {// 扫描生物群系的注册key
				KlassWalker.walkTypeFields(this.getClass(), List.class, (Field f, boolean isStatic, List biomes) -> {
					if (isStatic && GenericTypes.getFirstGenericType(f) == String.class) {
						wrapper.value = biomes;
						return false;
					}
					return true;
				});
			}
			possible_biomes_list = new ArrayList<>();
			if (bootstrapContext == null)
				throw new IllegalStateException("Resolve possible biomes failed indicates that BootstrapContext is null.");
			else {
				for (String key : wrapper.value) {
					possible_biomes_list.add(ExtBiome.datagenStageHolder(bootstrapContext, key));
				}
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
