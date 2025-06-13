package ba.entries.biome.kivotos;

import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;

import fw.codec.annotation.CodecAutogen;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;

public class KivotosBiomes extends BiomeSource {

	static {
		CodecAutogen.CodecGenerator.forCodec(KivotosBiomes.class);
	}

	@CodecAutogen
	public static final MapCodec<? extends BiomeSource> CODEC = null;

	public KivotosBiomes() {

	}

	@Override
	protected MapCodec<? extends BiomeSource> codec() {
		return CODEC;
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Sampler sampler) {
		// TODO Auto-generated method stub
		return null;
	}

}
