package ba.entries.biome.shittim_chest;

import java.util.List;

import com.mojang.serialization.MapCodec;

import fw.client.render.level.MutableBiomeSpecialEffects;
import fw.codec.CodecHolder;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecTarget;
import fw.core.ExecuteIn;
import fw.datagen.DatagenHolder;
import fw.datagen.annotation.RegistryDatagen;
import fw.math.ColorLinearInterpolation;
import fw.terrain.biome.ExtBiome;
import fw.terrain.biome.ExtBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class ShittimChestBiomes extends ExtBiomeSource {

	private static MutableBiomeSpecialEffects effects;

	static {
		CodecHolder.CODEC();
		RegistryDatagen.RegistriesProvider.forDatagen();
		ExecuteIn.Client(() -> {
			effects = MutableBiomeSpecialEffects.from("ba:shittim_chest");
			effects.tick(ColorLinearInterpolation
					.begin(0, 120, 153, 197)// 6 h 灰蓝
					.append(3000, 131, 210, 250)// 9 h 浅天蓝
					.append(6000, 92, 194, 253)// 12 h 深天蓝
					.append(9000, 131, 210, 250)// 15 h 浅天蓝
					.append(12000, 215, 184, 223)// 18 h 紫
					.append(15000, 146, 215, 232)// 21 h 浅黑蓝
					.append(18000, 48, 87, 160)// 0 h 深黑蓝
					.append(21000, 146, 215, 232)// 3 h 浅黑蓝
			);
		});
	}

	@CodecAutogen(null_if_empty = true, warn_if_register_failed = true)
	public static final MapCodec<? extends BiomeSource> CODEC = null;

	protected static final List<String> biomes = List.of("ba:shittim_chest");

	@RegistryDatagen
	public static final DatagenHolder<Biome> shittim_chest = ExtBiome.register("shittim_chest", ExtBiome.build(
			false,
			0,
			Biome.TemperatureModifier.NONE,
			0,
			ExtBiome.buildBiomeSpecialEffects(0xD2EFFD, 0x47C6FA, 0x62DEFB, 0xA4E2FC, null),
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

	@CodecTarget
	public ShittimChestBiomes(List<Holder<Biome>> possibleBiomesList) {
		super(possibleBiomesList);
	}

	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Sampler sampler) {
		return biome("ba:shittim_chest");
	}
}
