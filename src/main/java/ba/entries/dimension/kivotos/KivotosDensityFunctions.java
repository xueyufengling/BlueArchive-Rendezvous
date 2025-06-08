package ba.entries.dimension.kivotos;

import com.mojang.serialization.MapCodec;

import fw.datagen.DatagenHolder;
import fw.datagen.annotation.RegistryDatagen;
import fw.terrain.ExtDimension;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public final class KivotosDensityFunctions {
	static {
		RegistryDatagen.RegistriesProvider.forDatagen(KivotosDensityFunctions.class);
	}

	@RegistryDatagen
	public static final DatagenHolder<DensityFunction> KIVOTOS_BASE_3D_NOISE = ExtDimension.Df.register(Kivotos.ID + "/base_3d_noise", BlendedNoise.createUnseeded(
			0.25, // xz_scale
			0.25, // y_scale
			80.0, // xz_factor
			160.0, // y_factor
			8.0// smear_scale_multiplier
	));

	//@RegistryDatagen
	public static final DatagenHolder<DensityFunction> KIVOTOS_CONTINENTS = ExtDimension.Df.register(Kivotos.ID + "/continents", new KivotosDensityFunction(0));

	public static final class KivotosDensityFunction implements DensityFunction.SimpleFunction {

		public static final KeyDispatchDataCodec<KivotosDensityFunction> CODEC = KeyDispatchDataCodec.of(
				MapCodec.unit(new KivotosDensityFunction(0L)));

		private final SimplexNoise baseNoise;

		public KivotosDensityFunction(long seed) {
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
}