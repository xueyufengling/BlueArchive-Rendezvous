package fw.terrain;

import fw.resources.ResourceKeyBuilder;
import lyra.klass.ObjectManipulator;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

/**
 * 简化对地形生成时DensityFunction（原版密度函数定义于NoiseRouterData）和NormalNoise.NoiseParameters的使用
 */
public class Df {
	private HolderGetter<DensityFunction> densityFunctions;
	private HolderGetter<NormalNoise.NoiseParameters> noiseParameters;
	public Class<?> staticDensityFunctionsClass = NoiseRouterData.class;
	public Class<?> staticNoiseParametersClass = Noises.class;

	private Df(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
		this.densityFunctions = densityFunctions;
		this.noiseParameters = noiseParameters;
	}

	private Df(BootstrapContext<?> context) {
		this(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE));
	}

	public Holder<NormalNoise.NoiseParameters> param(ResourceKey<NormalNoise.NoiseParameters> noiseParametersKey) {
		return noiseParameters.getOrThrow(noiseParametersKey);
	}

	public Holder<NoiseParameters> param(String noiseParametersKey) {
		return param(ResourceKeyBuilder.build(Registries.NOISE, noiseParametersKey));
	}

	/**
	 * 访问某个类的私有静态字段作为noiseParametersKey
	 * 
	 * @param noiseParametersKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Holder<NormalNoise.NoiseParameters> staticParam(Class<?> noiseParameters, String noiseParametersKey) {
		return param((ResourceKey<NormalNoise.NoiseParameters>) ObjectManipulator.access(noiseParameters, noiseParametersKey));
	}

	public DensityFunction staticParam(String noiseParametersKey) {
		return staticFunc(this.staticNoiseParametersClass, noiseParametersKey);
	}

	public DensityFunction func(ResourceKey<DensityFunction> densityFunctionsKey) {
		return new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(densityFunctionsKey));
	}

	public DensityFunction func(String densityFunctionsKey) {
		return func(ResourceKeyBuilder.build(Registries.DENSITY_FUNCTION, densityFunctionsKey));
	}

	/**
	 * 访问某个类的私有静态字段作为densityFunctionsKey
	 * 
	 * @param densityFunctionsKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DensityFunction staticFunc(Class<?> staticDensityFunctionsClass, String densityFunctionsKey) {
		return func((ResourceKey<DensityFunction>) ObjectManipulator.access(staticDensityFunctionsClass, densityFunctionsKey));
	}

	public DensityFunction staticFunc(String densityFunctionsKey) {
		return staticFunc(this.staticDensityFunctionsClass, densityFunctionsKey);
	}

	public static Df of(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParameters) {
		if (densityFunctions != null && noiseParameters != null)
			return new Df(densityFunctions, noiseParameters);
		return null;
	}

	public static Df of(BootstrapContext<?> context) {
		if (context != null)
			return new Df(context);
		return null;
	}
}
