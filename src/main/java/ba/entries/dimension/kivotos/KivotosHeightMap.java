package ba.entries.dimension.kivotos;

import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecEntry;
import fw.terrain.HeightMapMask;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class KivotosHeightMap extends HeightMapMask {

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	@CodecAutogen(null_if_empty = true, warn_if_register_failed = true)
	public static final KeyDispatchDataCodec<KivotosHeightMap> CODEC = null;

	@CodecEntry
	public long seed;

	private final SimplexNoise baseNoise;

	public KivotosHeightMap(long seed) {
		RandomSource randomsource = new LegacyRandomSource(seed);
		this.baseNoise = new SimplexNoise(randomsource);
	}

	@Override
	protected double getHeightValue(int x, int z) {
		return -40;
	}
}
