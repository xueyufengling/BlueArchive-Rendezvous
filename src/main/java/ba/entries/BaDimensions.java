package ba.entries;

import java.util.OptionalLong;

import fw.core.registry.DatagenHolder;
import fw.datagen.annotation.RegistryDatagen;
import fw.entries.ExtDimension;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class BaDimensions {
	static {
		RegistryDatagen.RegistriesProvider.forDatagen(BaDimensions.class);
	}

	public static final String KIVOTOS_ID = "kivotos";

	@RegistryDatagen
	public static final DatagenHolder<DimensionType> KIVOTOS_TYPE = ExtDimension.Type.register(KIVOTOS_ID, new DimensionType(
			OptionalLong.empty(),
			true,
			false,
			false,
			true,
			1.0,
			true,
			true,
			-64,
			384,
			384,
			BlockTags.INFINIBURN_OVERWORLD,
			BuiltinDimensionTypes.OVERWORLD_EFFECTS,
			0.0F,
			new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0)));

	@RegistryDatagen
	public static final DatagenHolder<LevelStem> KIVOTOS_STEM = ExtDimension.Stem.register(KIVOTOS_ID, (BootstrapContext<LevelStem> context) -> {
		HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
		Holder<DimensionType> dimType = dimensionTypes.getOrThrow(KIVOTOS_TYPE.resourceKey);
		Holder<NoiseGeneratorSettings> noise = noiseSettings.getOrThrow(NoiseGeneratorSettings.NETHER);


		
	    MultiNoiseBiomeSource biomeSource = preset.getBiomeSource(biomes);
		
		return new LevelStem(dimType, new NoiseBasedChunkGenerator(
				new FixedBiomeSource(context.lookup(Registries.BIOME).getOrThrow(Biomes.PLAINS)),
				noise));
	});
}
