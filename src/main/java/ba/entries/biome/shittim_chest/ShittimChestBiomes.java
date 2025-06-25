package ba.entries.biome.shittim_chest;

import java.util.List;

import com.mojang.serialization.MapCodec;

import fw.codec.annotation.CodecAutogen;
import fw.datagen.DatagenHolder;
import fw.datagen.annotation.RegistryDatagen;
import fw.terrain.ExtBiome;
import fw.terrain.ExtBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class ShittimChestBiomes extends ExtBiomeSource {

	static {
		CodecAutogen.CodecGenerator.forCodec(ShittimChestBiomes.class);
		RegistryDatagen.RegistriesProvider.forDatagen(ShittimChestBiomes.class);
	}

	@CodecAutogen(null_if_empty = true)
	public static final MapCodec<? extends BiomeSource> CODEC = null;

	protected static final List<String> biomes = List.of("ba:shittim_chest");

	@RegistryDatagen
	public static final DatagenHolder<Biome> shittim_chest = ExtBiome.register("shittim_chest", ExtBiome.build(
			false,
			0,
			Biome.TemperatureModifier.NONE,
			0,
			ExtBiome.buildBiomeSpecialEffects(0, 0, 0, 0, null),
			MobSpawnSettings.EMPTY,
			BiomeGenerationSettings.EMPTY));

	/**
	 * 仅数据生成阶段使用
	 * 
	 * @param context
	 */
	public ShittimChestBiomes(BootstrapContext<?> context) {
		super(context);
	}

	public ShittimChestBiomes(List<Holder<Biome>> possibleBiomesList) {
		super(possibleBiomesList);
	}

	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Sampler sampler) {
		return biome("ba:shittim_chest");
	}
}
