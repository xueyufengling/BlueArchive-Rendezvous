package ba.entries.dimension.kivotos;

import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecEntry;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class KivotosDf implements DensityFunction.SimpleFunction {

	static {
		CodecAutogen.CodecGenerator.forCodec(KivotosDf.class);
	}

	@CodecAutogen
	public static final KeyDispatchDataCodec<KivotosDf> CODEC = null;

	@CodecEntry
	public long seed;

	private final SimplexNoise baseNoise;

	public KivotosDf(long seed) {
		RandomSource randomsource = new LegacyRandomSource(seed);
		this.baseNoise = new SimplexNoise(randomsource);
	}

	private static float getHeightValue(SimplexNoise noise, int x, int z) {
		int i = x / 2;
		int j = z / 2;
		int k = x % 2;
		int l = z % 2;
		float f = 100.0F - Mth.sqrt((float) (x * x + z * z)) * 8.0F;
		f = Mth.clamp(f, -100.0F, 80.0F);

		for (int i1 = -12; i1 <= 12; i1++) {
			for (int j1 = -12; j1 <= 12; j1++) {
				long k1 = (long) (i + i1);
				long l1 = (long) (j + j1);
				if (k1 * k1 + l1 * l1 > 4096L && noise.getValue((double) k1, (double) l1) < -0.9F) {
					float f1 = (Mth.abs((float) k1) * 3439.0F + Mth.abs((float) l1) * 147.0F) % 13.0F + 9.0F;
					float f2 = (float) (k - i1 * 2);
					float f3 = (float) (l - j1 * 2);
					float f4 = 100.0F - Mth.sqrt(f2 * f2 + f3 * f3) * f1;
					f4 = Mth.clamp(f4, -100.0F, 80.0F);
					f = Math.max(f, f4);
				}
			}
		}

		return f;
	}

	@Override
	public double compute(DensityFunction.FunctionContext context) {
		return ((double) getHeightValue(this.baseNoise, context.blockX() / 8, context.blockZ() / 8) - 8.0) / 128.0;
	}

	@Override
	public double minValue() {
		return -0.84375;
	}

	@Override
	public double maxValue() {
		return 0.5625;
	}

	@Override
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return CODEC;
	}

}
