package ba.entries.biome.kivotos;

import java.util.List;

import com.mojang.serialization.MapCodec;

import fw.codec.CodecHolder;
import fw.codec.annotation.CodecAutogen;
import fw.terrain.biome.ExtBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;

public class KivotosBiomes extends ExtBiomeSource {

	static {
		CodecHolder.CODEC();
	}

	@CodecAutogen(null_if_empty = true)
	public static final MapCodec<? extends BiomeSource> CODEC = null;

	protected static final List<String> biomes = List.of("minecraft:forest", "minecraft:jungle", "minecraft:desert");

	/**
	 * 仅数据生成阶段使用
	 * 
	 * @param contexts
	 */
	public KivotosBiomes(BootstrapContext<?> context) {
		super(context);
	}

	public KivotosBiomes(List<Holder<Biome>> possibleBiomesList) {
		super(possibleBiomesList);
	}

	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Sampler sampler) {
		return biome("minecraft:forest");
	}
}
