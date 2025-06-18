package ba.entries.dimension.kivotos;

import fw.datagen.DatagenHolder;
import fw.datagen.annotation.RegistryDatagen;
import fw.dimension.ExtDimension;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;

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

}