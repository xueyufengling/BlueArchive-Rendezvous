package fw.terrain;

import fw.core.Core;
import fw.core.registry.DynamicRegistries;
import fw.datagen.DatagenHolder;
import fw.resources.ResourceKeyBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class ExtBiome {
	/**
	 * 获取指定的生物群系
	 * 
	 * @param key 带命名空间的key
	 * @return
	 */
	public static final Holder<Biome> getBiome(String key) {
		return DynamicRegistries.BIOME.getHolderOrThrow(ResourceKeyBuilder.build(Registries.BIOME, key));
	}

	/**
	 * 获取本模组的生物群系
	 * 
	 * @param key 不带命名空间的key
	 * @return
	 */
	public static final Holder<Biome> modBiome(String key) {
		return DynamicRegistries.BIOME.getHolderOrThrow(ResourceKeyBuilder.build(Registries.BIOME, Core.ModId, key));
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

	/**
	 * 生成指定的生物群系
	 * 
	 * @param precipitation
	 * @param temperature
	 * @param temperatureModifier
	 * @param downfall
	 * @param effects
	 * @param spawnSettings
	 * @param generationSettings
	 * @return
	 */
	public static Biome build(boolean precipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall, BiomeSpecialEffects effects, MobSpawnSettings spawnSettings, BiomeGenerationSettings generationSettings) {
		return new Biome.BiomeBuilder()
				.hasPrecipitation(precipitation)
				.temperature(temperature)
				.temperatureAdjustment(temperatureModifier)
				.downfall(downfall)
				.specialEffects(effects)
				.mobSpawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}

	/**
	 * 注册生物群系
	 * 
	 * @param name
	 * @param biome
	 * @return
	 */
	public static final DatagenHolder<Biome> register(String name, Biome biome) {
		return DatagenHolder.of(Registries.BIOME, name, biome);
	}

	public static BiomeSpecialEffects buildBiomeSpecialEffects(
			int fogColor,
			int waterColor,
			int waterFogColor,
			int skyColor,
			int foliageColorOverride,
			int grassColorOverride,
			BiomeSpecialEffects.GrassColorModifier grassColorModifier,
			AmbientParticleSettings ambientParticleSettings,
			Holder<SoundEvent> ambientLoopSoundEvent,
			AmbientMoodSettings ambientMoodSettings,
			AmbientAdditionsSettings ambientAdditionsSettings,
			Music backgroundMusic) {
		BiomeSpecialEffects.Builder builder = new BiomeSpecialEffects.Builder()
				.fogColor(fogColor)
				.waterColor(waterColor)
				.waterFogColor(waterFogColor)
				.skyColor(skyColor)
				.foliageColorOverride(foliageColorOverride)
				.grassColorOverride(grassColorOverride);
		if (ambientParticleSettings != null)
			builder = builder.grassColorModifier(grassColorModifier);
		if (ambientParticleSettings != null)
			builder = builder.ambientParticle(ambientParticleSettings);
		if (ambientLoopSoundEvent != null)
			builder = builder.ambientLoopSound(ambientLoopSoundEvent);
		if (ambientMoodSettings != null)
			builder = builder.ambientMoodSound(ambientMoodSettings);
		if (ambientAdditionsSettings != null)
			builder = builder.ambientAdditionsSound(ambientAdditionsSettings);
		if (backgroundMusic != null)
			builder = builder.backgroundMusic(backgroundMusic);
		return builder.build();
	}

	public static BiomeSpecialEffects buildBiomeSpecialEffects(
			int fogColor,
			int waterColor,
			int waterFogColor,
			int skyColor,
			int foliageColorOverride,
			int grassColorOverride,
			AmbientParticleSettings ambientParticleSettings,
			Holder<SoundEvent> ambientLoopSoundEvent,
			Music backgroundMusic) {
		return ExtBiome.buildBiomeSpecialEffects(
				fogColor,
				waterColor,
				waterFogColor,
				skyColor,
				foliageColorOverride,
				grassColorOverride,
				BiomeSpecialEffects.GrassColorModifier.NONE,
				ambientParticleSettings,
				ambientLoopSoundEvent,
				null,
				null,
				backgroundMusic);
	}

	public static BiomeSpecialEffects buildBiomeSpecialEffects(
			int fogColor,
			int waterColor,
			int waterFogColor,
			int skyColor,
			int foliageColorOverride,
			int grassColorOverride,
			Music backgroundMusic) {
		return ExtBiome.buildBiomeSpecialEffects(
				fogColor,
				waterColor,
				waterFogColor,
				skyColor,
				foliageColorOverride,
				grassColorOverride,
				null,
				null,
				backgroundMusic);
	}

	public static BiomeSpecialEffects buildBiomeSpecialEffects(
			int fogColor,
			int waterColor,
			int waterFogColor,
			int skyColor,
			Music backgroundMusic) {
		return ExtBiome.buildBiomeSpecialEffects(
				fogColor,
				waterColor,
				waterFogColor,
				skyColor,
				0,
				0,
				backgroundMusic);
	}
}
