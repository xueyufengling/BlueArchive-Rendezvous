package fw.terrain;

import fw.core.Core;
import fw.core.registry.MappedRegistries;
import fw.resources.ResourceKeyBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;

public class ExtBiome {
	/**
	 * 获取指定的生物群系
	 * 
	 * @param key 带命名空间的key
	 * @return
	 */
	public static final Holder<Biome> getBiome(String key) {
		return MappedRegistries.BIOME.getHolderOrThrow(ResourceKeyBuilder.build(Registries.BIOME, key));
	}

	/**
	 * 获取本模组的生物群系
	 * 
	 * @param key 不带命名空间的key
	 * @return
	 */
	public static final Holder<Biome> modBiome(String key) {
		return MappedRegistries.BIOME.getHolderOrThrow(ResourceKeyBuilder.build(Registries.BIOME, Core.ModId, key));
	}

	/**
	 * 数据生成时使用,用于获取目标key的Holder.<br>
	 * 数据包注册表的Holder要进入服务器才能获取，这里是获取启动时的Holder，即BootstrapContext的Holder。<br>
	 */
	private static HolderGetter<Biome> DATAGEN_BIOME_GETTER = null;

	/**
	 * 数据注册阶段获取生物群系的Holder
	 * 
	 * @param context
	 * @param key     带命名空间的key
	 * @return
	 */
	public static final Holder<Biome> datagenHolder(BootstrapContext<?> context, String key) {
		if (DATAGEN_BIOME_GETTER == null)
			DATAGEN_BIOME_GETTER = context.lookup(Registries.BIOME);
		return DATAGEN_BIOME_GETTER.getOrThrow(ResourceKeyBuilder.build(Registries.BIOME, key));
	}

	/**
	 * 数据注册阶段获取生物群系的Holder
	 * 
	 * @param context
	 * @param key     不带命名空间的key
	 * @return
	 */
	public static final Holder<Biome> datagenModHolder(BootstrapContext<?> context, String key) {
		if (DATAGEN_BIOME_GETTER == null)
			DATAGEN_BIOME_GETTER = context.lookup(Registries.BIOME);
		return DATAGEN_BIOME_GETTER.getOrThrow(ResourceKeyBuilder.build(Registries.BIOME, Core.ModId, key));
	}
}
