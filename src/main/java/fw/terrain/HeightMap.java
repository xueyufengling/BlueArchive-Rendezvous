package fw.terrain;

import java.util.ArrayList;

import fw.codec.annotation.CodecAutogen;
import fw.codec.derived.KeyDispatchDataCodecHolder;
import fw.math.ConvolutionKernel;
import fw.math.Sampler2D;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

/**
 * 高度图，根据x、z坐标计算出高度y，高于y则方块置空，低于y则密度保持不变
 */
public abstract class HeightMap implements DensityFunction.SimpleFunction, KeyDispatchDataCodecHolder<DensityFunction>, Sampler2D, Cloneable {
	static {
		CodecAutogen.CodecGenerator.markDerivedAutoRegister();
	}

	@Override
	@SuppressWarnings("unchecked")
	public HeightMap clone() {
		try {
			HeightMap result = (HeightMap) super.clone();
			result.height_samplers = (ArrayList<Sampler2D.Entry>) this.height_samplers.clone();
			return result;
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 初始项
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	protected abstract double getHeightValue(double x, double z);

	private ArrayList<Sampler2D.Entry> height_samplers = new ArrayList<>();

	public HeightMap() {
		KeyDispatchDataCodecHolder.super.construct(DensityFunction.class);
		height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.ASSIGN, (double x, double z) -> this.getHeightValue(x, z)));
	}

	public final int entryNum() {
		return height_samplers.size();
	}

	public final Sampler2D wrap() {
		Sampler2D finalCalc = height_samplers.get(0).term;
		for (int idx = 1; idx < height_samplers.size(); ++idx) {
			finalCalc = Sampler2D.Entry.wrap(finalCalc, height_samplers.get(idx));
		}
		return finalCalc;
	}

	@Override
	public final double value(double x, double z) {
		return wrap().value(x, z);
	}

	@Override
	public final double compute(DensityFunction.FunctionContext context) {
		return context.blockY() > value(context.blockX(), context.blockZ()) ? minValue() : maxValue();
	}

	@Override
	public final double minValue() {
		return 0.0;
	}

	@Override
	public final double maxValue() {
		return 1.0;
	}

	/**
	 * 掩码操作，位于高度值以下的密度保持不变，以上的密度全部变成0
	 * 
	 * @param func
	 * @return
	 */
	public final DensityFunction mask(DensityFunction func) {
		return DensityFunctions.mul(func, this);
	}

	public static final HeightMap op(HeightMap hm, Sampler2D.Operation op, Sampler2D height) {
		hm.height_samplers.add(Sampler2D.Entry.of(op, height));
		return hm;
	}

	public final HeightMap opThis(Sampler2D.Operation op, Sampler2D height) {
		return op(this, op, height);
	}

	public final HeightMap op(Sampler2D.Operation op, Sampler2D height) {
		return op(this.clone(), op, height);
	}

	/**
	 * 加法
	 * 
	 * @param hm
	 * @param height
	 * @return
	 */
	public static final HeightMap add(HeightMap hm, Sampler2D height) {
		return op(hm, Sampler2D.Operation.ADD, height);
	}

	public final HeightMap addThis(Sampler2D height) {
		return add(this, height);
	}

	public final HeightMap add(Sampler2D height) {
		return add(this.clone(), height);
	}

	public static final HeightMap sub(HeightMap hm, Sampler2D height) {
		return op(hm, Sampler2D.Operation.SUB, height);
	}

	public final HeightMap subThis(Sampler2D height) {
		return sub(this, height);
	}

	public final HeightMap sub(HeightMap height) {
		return sub(this.clone(), height);
	}

	public static final HeightMap mul(HeightMap hm, Sampler2D height) {
		return op(hm, Sampler2D.Operation.MUL, height);
	}

	public final HeightMap mulThis(Sampler2D height) {
		return mul(this, height);
	}

	public final HeightMap mul(HeightMap height) {
		return mul(this.clone(), height);
	}

	public static final HeightMap div(HeightMap hm, Sampler2D height) {
		return op(hm, Sampler2D.Operation.DIV, height);
	}

	public final HeightMap divThis(Sampler2D height) {
		return div(this, height);
	}

	public final HeightMap div(HeightMap height) {
		return div(this.clone(), height);
	}

	public static final HeightMap conv(HeightMap hm, ConvolutionKernel kernel) {
		hm.height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.CONV(kernel)));
		return hm;
	}

	public final HeightMap convThis(ConvolutionKernel kernel) {
		return conv(this, kernel);
	}

	public final HeightMap conv(ConvolutionKernel kernel) {
		return conv(this.clone(), kernel);
	}

	@Override
	@SuppressWarnings("unchecked")
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KeyDispatchDataCodecHolder.super.codec();
	}
}
