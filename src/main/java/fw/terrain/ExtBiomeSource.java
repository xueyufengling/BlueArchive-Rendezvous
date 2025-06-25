package fw.terrain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;

import fw.codec.CodecWalker;
import fw.codec.annotation.CodecEntry;
import lyra.klass.GenericTypes;
import lyra.klass.KlassWalker;
import lyra.lang.Reflection;
import lyra.object.Placeholders;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

public abstract class ExtBiomeSource extends BiomeSource {

	/**
	 * 该派生类的CODEC，可以手动指定，没有手动指定则自动扫描PossibleBiomes
	 */
	protected MapCodec<? extends BiomeSource> derivedCodec = null;

	@CodecEntry
	protected List<Holder<Biome>> possible_biomes_list;

	/**
	 * 仅数据生成时序列化使用
	 * 
	 * @param context
	 * @param possibleBiomeKeys
	 */
	public ExtBiomeSource(BootstrapContext<?> context, List<String> possibleBiomeKeys) {
		resolvePossibleBiomes(context, possibleBiomeKeys);
	}

	public ExtBiomeSource(BootstrapContext<?> context) {
		resolvePossibleBiomes(context, null);
	}

	/**
	 * 实际运行时反序列化使用
	 * 
	 * @param possibleBiomesList
	 */
	public ExtBiomeSource(List<Holder<Biome>> possibleBiomesList) {
		this.possible_biomes_list = possibleBiomesList;
	}

	/**
	 * 在子类中搜寻MapCodec<br>
	 * 数据生成和运行时均会调用，需要保证这两个阶段均构建好CODEC。
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected MapCodec<? extends BiomeSource> codec() {
		if (derivedCodec == null) {
			CodecWalker.walkMapCodecs(this.getClass(), (Field f, MapCodec codec, Class<?> codecType) -> {
				if (codec != null && Reflection.is(codecType, BiomeSource.class)) {
					derivedCodec = codec;
					return false;
				}
				return true;
			});
		}
		if (derivedCodec == null)
			throw new NullPointerException("BiomeSource CODEC for " + this.getClass() + " is null, make sure you have specified or generated a correct MapCodec.");
		return derivedCodec;
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
				throw new RuntimeException("Resolve possible biomes failed indicates that BootstrapContext is null.");
			else {
				for (String key : wrapper.value) {
					possible_biomes_list.add(ExtBiome.datagenHolder(bootstrapContext, key));
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
