package lepus.mc.terrain.biome;

import java.util.ArrayList;
import java.util.Set;

import lepus.mc.core.registry.registries.server.DynamicRegistries;
import lepus.mc.datagen.EntryHolder;
import lepus.mc.resources.ResourceKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;

public class ExtBiome {
	/**
	 * 获取指定的生物群系
	 * 
	 * @param key 带命名空间的key
	 * @return
	 */
	public static final Holder<Biome> getBiome(String key) {
		return DynamicRegistries.BIOME.getHolderOrThrow(ResourceKeys.build(Registries.BIOME, key));
	}

	/**
	 * 数据注册阶段获取生物群系的Holder
	 * 数据生成时使用,用于获取目标key的Holder.<br>
	 * 数据包注册表的Holder要进入服务器才能获取，这里是获取启动时的Holder，即BootstrapContext的Holder。<br>
	 * 
	 * @param context
	 * @param key     带命名空间的key
	 * @return
	 */
	public static final Holder<Biome> datagenStageHolder(BootstrapContext<?> context, String key) {
		return context.lookup(Registries.BIOME).getOrThrow(ResourceKeys.build(Registries.BIOME, key));
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
	public static final EntryHolder<Biome> register(String name, Biome biome) {
		return EntryHolder.of(Registries.BIOME, name, biome);
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

	@SuppressWarnings("unchecked")
	public static Holder<Biome>[] possibleBiomeHolders(BiomeSource source) {
		Set<Holder<Biome>> biomeSet = source.possibleBiomes();
		Holder<Biome>[] biomes = new Holder[biomeSet.size()];
		return biomeSet.toArray(biomes);
	}

	public static Biome[] possibleBiomes(BiomeSource source) {
		ArrayList<Biome> biomeList = new ArrayList<>();
		for (Holder<Biome> biomeHolder : source.possibleBiomes())
			biomeList.add(biomeHolder.value());
		Biome[] biomes = new Biome[biomeList.size()];
		return biomeList.toArray(biomes);
	}

	public static Biome[] possibleBiomes(ChunkGenerator gen) {
		return possibleBiomes(gen.getBiomeSource());
	}

	public static Biome[] possibleBiomes(LevelStem stem) {
		return possibleBiomes(stem.generator());
	}
}
