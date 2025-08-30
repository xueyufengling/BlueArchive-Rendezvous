package lepus.mc.terrain.algorithm;

import java.util.List;

import com.mojang.serialization.Codec;

import lepus.math.field.ScalarField;
import lepus.mc.codec.annotation.AsDataField;
import lepus.mc.codec.annotation.CodecAutogen;
import lepus.mc.codec.annotation.CodecEntry;
import lepus.mc.codec.annotation.CodecTarget;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

/**
 * 平滑的倍频SimplexNoise噪声
 */
@AsDataField
public class OctaveSimplexNoise implements ScalarField {

	static {
		CodecAutogen.CodecGenerator.Codec();
	}

	@CodecAutogen(null_if_empty = true)
	public static final Codec<OctaveSimplexNoise> CODEC = null;

	/**
	 * 地形最低点偏移y坐标
	 */
	@CodecEntry
	public final double bias;

	/**
	 * 地形振幅，最高点和最低点的落差值，方块为单位
	 */
	@CodecEntry
	public final double amplitude;

	/**
	 * 采样要使用x、z的数值同样为较小的小数，直接使用方块坐标采样则相邻方块参差不齐，无法得到连续性结果。<br>
	 * 实际采样x坐标为x*x_factor，该值越小则相邻两个方块采样结果越平滑。
	 */
	@CodecEntry
	public final double x_factor;

	@CodecEntry
	public final double z_factor;

	@CodecEntry
	public final long seed;

	@CodecEntry
	public final boolean use_noise_offsets;

	@CodecEntry
	public final List<Integer> octaves;

	/**
	 * 噪声取值-1.0 ~ 1.0
	 */
	private final PerlinSimplexNoise noise;

	/**
	 * 地形振幅，最终高度为bias+noise(x*x_factor, z*z_factor)*(amplitude)，其中noise为噪声函数，取值-1 ~ 1
	 *
	 * @param bias
	 * @param amplitude
	 * @param x_factor
	 * @param z_factor
	 * @param seed
	 * @param use_noise_offsets
	 * @param octaves
	 */
	@CodecTarget
	public OctaveSimplexNoise(double bias, double amplitude, double x_factor, double z_factor, long seed, boolean use_noise_offsets, List<Integer> octaves) {
		this.bias = bias;
		this.amplitude = amplitude;
		this.x_factor = x_factor;
		this.z_factor = z_factor;
		this.seed = seed;
		this.use_noise_offsets = use_noise_offsets;
		this.octaves = octaves;
		this.noise = new PerlinSimplexNoise(new LegacyRandomSource(seed), octaves);
	}

	public OctaveSimplexNoise(double bias, double amplitude, double x_factor, double z_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		this(bias, amplitude, x_factor, z_factor, seed, use_noise_offsets, List.of(octaves));
	}

	public OctaveSimplexNoise(double bias, double amplitude, double xz_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		this(bias, amplitude, xz_factor, xz_factor, seed, use_noise_offsets, octaves);
	}

	public OctaveSimplexNoise(double amplitude, double xz_factor, long seed, boolean use_noise_offsets, Integer... octaves) {
		this(0, amplitude, xz_factor, seed, use_noise_offsets, octaves);
	}

	/**
	 * 能生成的最大高度
	 * 
	 * @return
	 */
	public final double maxHeight() {
		return bias + amplitude;
	}

	/**
	 * 能生成的最小高度
	 * 
	 * @return
	 */
	public final double minHeight() {
		return bias - amplitude;
	}

	@Override
	public double value(double x, double z) {
		return bias + noise.getValue(x * x_factor, z * z_factor, use_noise_offsets) * amplitude;
	}

	/**
	 * 振幅比例归一化，等价于amplitudeRatio(this.bias, sampledValue)
	 * 
	 * @param sampledValue
	 * @return
	 */
	public final double amplitudeRatio(double sampledValue) {
		return (sampledValue - this.bias) / this.amplitude;
	}

	/**
	 * 相对于某个y值的偏移占总振幅的比例
	 * 
	 * @param yOffset      y参照值，距离此值越近则返回值越小，越远则返回值越大
	 * @param sampledValue 采样得到的值
	 * @return
	 */
	public final double amplitudeRatio(double yOffset, double sampledValue) {
		return (sampledValue - yOffset) / (this.maxHeight() - yOffset);
	}

	public final OctaveSimplexNoise biasOf(double bias) {
		return new OctaveSimplexNoise(bias, this.amplitude, this.x_factor, this.z_factor, this.seed, this.use_noise_offsets, this.octaves);
	}
}
