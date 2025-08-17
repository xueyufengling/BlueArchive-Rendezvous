package fw.terrain;

import java.util.ArrayList;

import fw.codec.annotation.CodecAutogen;
import fw.codec.derived.KeyDispatchDataCodecHolder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

/**
 * 高度图，根据x、z坐标计算出高度y，高于y则方块置空，低于y则密度保持不变
 */
public abstract class HeightMap implements DensityFunction.SimpleFunction, KeyDispatchDataCodecHolder<DensityFunction>, Cloneable {
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

	/**
	 * 获取初始项的系数
	 * 
	 * @param x
	 * @param z
	 * @param lastStepResult
	 * @param sampledValue
	 * @return
	 */
	protected double getCoefficient(double x, double z, double lastStepResult, Sampler2D sampler, double sampledValue) {
		return 1.0;
	}

	private ArrayList<Sampler2D.Entry> height_samplers = new ArrayList<>();

	public HeightMap() {
		KeyDispatchDataCodecHolder.super.construct(DensityFunction.class);
		height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.ASSIGN, (double x, double z, double lastStepResult, Sampler2D sampler, double sampledValue) -> this.getCoefficient(x, z, lastStepResult, sampler, sampledValue), (double x, double z) -> this.getHeightValue(x, z)));
	}

	public final double compute(double x, double z) {
		double result = Double.NaN;
		for (Sampler2D.Entry entry : height_samplers) {
			result = entry.calculate(result, x, z);
		}
		return result;
	}

	@Override
	public final double compute(DensityFunction.FunctionContext context) {
		return context.blockY() > compute(context.blockX(), context.blockZ()) ? minValue() : maxValue();
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

	/**
	 * 执行算术操作
	 * 
	 * @param op
	 * @param coefficient
	 * @param height
	 * @return
	 */

	public final HeightMap op(Sampler2D.Operation op, Sampler2D.Coefficient coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(op, coefficient, height));
		return hm;
	}

	public final HeightMap op(Sampler2D.Operation op, double coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(op, coefficient, height));
		return hm;
	}

	public final HeightMap op(Sampler2D.Operation op, Sampler2D.Coefficient coefficient, HeightMap height) {
		return op(op, coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap op(Sampler2D.Operation op, double coefficient, HeightMap height) {
		return op(op, coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap op(Sampler2D.Operation op, Sampler2D height) {
		return op(op, 1.0, height);
	}

	public final HeightMap op(Sampler2D.Operation op, HeightMap height) {
		return op(op, 1.0, height);
	}

	/**
	 * 加法
	 * 
	 * @param coefficient
	 * @param height
	 * @return
	 */
	public final HeightMap add(Sampler2D.Coefficient coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.ADD, coefficient, height));
		return hm;
	}

	public final HeightMap add(double coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.ADD, coefficient, height));
		return hm;
	}

	public final HeightMap add(Sampler2D.Coefficient coefficient, HeightMap height) {
		return add(coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap add(double coefficient, HeightMap height) {
		return add(coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap add(Sampler2D height) {
		return add(1.0, height);
	}

	public final HeightMap add(HeightMap height) {
		return add(1.0, height);
	}

	public final HeightMap sub(Sampler2D.Coefficient coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.SUB, coefficient, height));
		return this;
	}

	public final HeightMap sub(double coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.SUB, coefficient, height));
		return this;
	}

	public final HeightMap sub(Sampler2D.Coefficient coefficient, HeightMap height) {
		return sub(coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap sub(double coefficient, HeightMap height) {
		return sub(coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap sub(Sampler2D height) {
		return sub(1.0, height);
	}

	public final HeightMap sub(HeightMap height) {
		return sub(1.0, height);
	}

	public final HeightMap mul(double coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.MUL, coefficient, height));
		return this;
	}

	public final HeightMap mul(Sampler2D.Coefficient coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.MUL, coefficient, height));
		return this;
	}

	public final HeightMap mul(Sampler2D.Coefficient coefficient, HeightMap height) {
		return mul(coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap mul(double coefficient, HeightMap height) {
		return mul(coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap mul(Sampler2D height) {
		return mul(1.0, height);
	}

	public final HeightMap mul(HeightMap height) {
		return mul(1.0, height);
	}

	public final HeightMap div(Sampler2D.Coefficient coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.DIV, coefficient, height));
		return this;
	}

	public final HeightMap div(double coefficient, Sampler2D height) {
		HeightMap hm = this.clone();
		hm.height_samplers.add(Sampler2D.Entry.of(Sampler2D.Operation.DIV, coefficient, height));
		return this;
	}

	public final HeightMap div(Sampler2D.Coefficient coefficient, HeightMap height) {
		return div(coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap div(double coefficient, HeightMap height) {
		return div(coefficient, (double x, double z) -> height.clone().getHeightValue(x, z));
	}

	public final HeightMap div(Sampler2D height) {
		return div(1.0, height);
	}

	public final HeightMap div(HeightMap height) {
		return div(1.0, height);
	}

	@Override
	@SuppressWarnings("unchecked")
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KeyDispatchDataCodecHolder.super.codec();
	}
}
