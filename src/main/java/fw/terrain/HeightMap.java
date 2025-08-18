package fw.terrain;

import java.util.ArrayList;

import fw.codec.annotation.CodecAutogen;
import fw.codec.derived.KeyDispatchDataCodecHolder;
import fw.math.ConvolutionKernel;
import fw.math.ScalarField;
import fw.math.SlidingWindowOperation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

/**
 * 高度图，根据x、z坐标计算出高度y，高于y则方块置空，低于y则密度保持不变
 */
public abstract class HeightMap implements DensityFunction.SimpleFunction, KeyDispatchDataCodecHolder<DensityFunction>, ScalarField, Cloneable {
	static {
		CodecAutogen.CodecGenerator.markDerivedAutoRegister();
	}

	@Override
	@SuppressWarnings("unchecked")
	public HeightMap clone() {
		try {
			HeightMap result = (HeightMap) super.clone();
			result.height_samplers = (ArrayList<ScalarField.Entry>) this.height_samplers.clone();
			return result;
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 高度值的系数
	 */
	@FunctionalInterface
	public static interface Coefficient {
		/**
		 * @param x
		 * @param z
		 * @param y 采样得到的原始高度值
		 * @return 变换后的最终高度值
		 */
		public double coefficient(double x, double z, double y);

		public static final Coefficient NONE = (double x, double z, double y) -> 1;
	}

	/**
	 * 初始项
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	protected abstract double getHeightValue(double x, double z);

	private ArrayList<ScalarField.Entry> height_samplers = new ArrayList<>();

	public HeightMap() {
		KeyDispatchDataCodecHolder.super.construct(DensityFunction.class);
		height_samplers.add(ScalarField.Entry.of(ScalarField.Operator.ASSIGN, (double x, double z) -> this.getHeightValue(x, z)));
	}

	public final int entryNum() {
		return height_samplers.size();
	}

	public final ScalarField wrap() {
		ScalarField finalCalc = height_samplers.get(0).term;
		for (int idx = 1; idx < height_samplers.size(); ++idx) {
			finalCalc = ScalarField.Entry.wrap(finalCalc, height_samplers.get(idx));
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

	public static final HeightMap op(HeightMap hm, ScalarField.Operator op, ScalarField height) {
		hm.height_samplers.add(ScalarField.Entry.of(op, height));
		return hm;
	}

	public final HeightMap opThis(ScalarField.Operator op, ScalarField height) {
		return op(this, op, height);
	}

	public final HeightMap op(ScalarField.Operator op, ScalarField height) {
		return op(this.clone(), op, height);
	}

	/**
	 * 加法
	 * 
	 * @param hm
	 * @param height
	 * @return
	 */
	public static final HeightMap add(HeightMap hm, ScalarField height) {
		return op(hm, ScalarField.Operator.ADD, height);
	}

	public final HeightMap addThis(ScalarField height) {
		return add(this, height);
	}

	public final HeightMap add(ScalarField height) {
		return add(this.clone(), height);
	}

	public static final HeightMap sub(HeightMap hm, ScalarField height) {
		return op(hm, ScalarField.Operator.SUB, height);
	}

	public final HeightMap subThis(ScalarField height) {
		return sub(this, height);
	}

	public final HeightMap sub(HeightMap height) {
		return sub(this.clone(), height);
	}

	public static final HeightMap mul(HeightMap hm, ScalarField height) {
		return op(hm, ScalarField.Operator.MUL, height);
	}

	public final HeightMap mulThis(ScalarField height) {
		return mul(this, height);
	}

	public final HeightMap mul(HeightMap height) {
		return mul(this.clone(), height);
	}

	public static final HeightMap div(HeightMap hm, ScalarField height) {
		return op(hm, ScalarField.Operator.DIV, height);
	}

	public final HeightMap divThis(ScalarField height) {
		return div(this, height);
	}

	public final HeightMap div(HeightMap height) {
		return div(this.clone(), height);
	}

	public static final HeightMap conv(HeightMap hm, ConvolutionKernel kernel) {
		hm.height_samplers.add(ScalarField.Entry.of(ScalarField.Operator.CONV(kernel)));
		return hm;
	}

	public final HeightMap convThis(ConvolutionKernel kernel) {
		return conv(this, kernel);
	}

	public final HeightMap conv(ConvolutionKernel kernel) {
		return conv(this.clone(), kernel);
	}

	public static final HeightMap blend(HeightMap hm, ScalarField blendFactor, ScalarField func) {
		hm.height_samplers.add(ScalarField.Entry.of(ScalarField.Operator.BLEND(blendFactor), func));
		return hm;
	}

	public final HeightMap blendThis(ScalarField blendFactor, ScalarField func) {
		return blend(this, blendFactor, func);
	}

	public final HeightMap blend(ScalarField blendFactor, ScalarField func) {
		return blend(this.clone(), blendFactor, func);
	}

	public static final HeightMap slidingWindow(HeightMap hm, SlidingWindowOperation op) {
		hm.height_samplers.add(ScalarField.Entry.of(ScalarField.Operator.SLIDING_WINDOW(op)));
		return hm;
	}

	public final HeightMap slidingWindowThis(SlidingWindowOperation op) {
		return slidingWindow(this, op);
	}

	public final HeightMap slidingWindow(SlidingWindowOperation op) {
		return slidingWindow(this.clone(), op);
	}

	@Override
	@SuppressWarnings("unchecked")
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KeyDispatchDataCodecHolder.super.codec();
	}
}
