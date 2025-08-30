package lepus.mc.terrain;

import java.util.ArrayList;

import lepus.math.algebra.Operator;
import lepus.math.algebra.Term;
import lepus.math.algebra.UnaryOperator;
import lepus.math.field.ScalarField;
import lepus.mc.codec.annotation.CodecAutogen;
import lepus.mc.codec.derived.KeyDispatchDataCodecHolder;
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
			result.height_samplers = (ArrayList<Term<ScalarField, ?>>) this.height_samplers.clone();
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

	private ArrayList<Term<ScalarField, ?>> height_samplers = new ArrayList<>();

	public HeightMap() {
		KeyDispatchDataCodecHolder.super.construct(DensityFunction.class);
		height_samplers.add(Term.of((double x, double z) -> this.getHeightValue(x, z)));
	}

	public final int entryNum() {
		return height_samplers.size();
	}

	@Override
	public final double value(double x, double z) {
		return Term.resolve(this.height_samplers).value().value(x, z);
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

	/**
	 * 对两个标量场进行计算的算子
	 * 
	 * @param hm
	 * @param op
	 * @param height
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <O> HeightMap op(HeightMap hm, Operator<ScalarField, ?, O> op, O... oprands) {
		hm.height_samplers.add(Term.of(op, oprands));
		return hm;
	}

	/**
	 * 对单一标量场进行计算的算子
	 * 
	 * @param hm
	 * @param op
	 * @return
	 */
	public static final HeightMap op(HeightMap hm, UnaryOperator<ScalarField, ScalarField> op) {
		hm.height_samplers.add(Term.of(op));
		return hm;
	}

	@SuppressWarnings("unchecked")
	public final <O> HeightMap opThis(Operator<ScalarField, ?, O> op, O... oprands) {
		return op(this, op, oprands);
	}

	@SuppressWarnings("unchecked")
	public final <O> HeightMap op(Operator<ScalarField, ?, O> op, O... oprands) {
		return op(this.clone(), op, oprands);
	}

	public final <O> HeightMap opThis(UnaryOperator<ScalarField, ScalarField> op) {
		return op(this, op);
	}

	public final <O> HeightMap op(UnaryOperator<ScalarField, ScalarField> op) {
		return op(this.clone(), op);
	}

	/**
	 * 加法
	 * 
	 * @param hm
	 * @param height
	 * @return
	 */
	public static final HeightMap add(HeightMap hm, ScalarField height) {
		return op(hm, ScalarField.Operators.ADD, height);
	}

	public final HeightMap addThis(ScalarField height) {
		return add(this, height);
	}

	public final HeightMap add(ScalarField height) {
		return add(this.clone(), height);
	}

	public static final HeightMap sub(HeightMap hm, ScalarField height) {
		return op(hm, ScalarField.Operators.SUB, height);
	}

	public final HeightMap subThis(ScalarField height) {
		return sub(this, height);
	}

	public final HeightMap sub(HeightMap height) {
		return sub(this.clone(), height);
	}

	public static final HeightMap mul(HeightMap hm, ScalarField height) {
		return op(hm, ScalarField.Operators.MUL, height);
	}

	public final HeightMap mulThis(ScalarField height) {
		return mul(this, height);
	}

	public final HeightMap mul(HeightMap height) {
		return mul(this.clone(), height);
	}

	public static final HeightMap div(HeightMap hm, ScalarField height) {
		return op(hm, ScalarField.Operators.DIV, height);
	}

	public final HeightMap divThis(ScalarField height) {
		return div(this, height);
	}

	public final HeightMap div(HeightMap height) {
		return div(this.clone(), height);
	}

	public static final HeightMap blend(HeightMap hm, ScalarField blendFactor, ScalarField func) {
		return op(hm, ScalarField.Operators.BLEND, func, blendFactor);
	}

	public final HeightMap blendThis(ScalarField blendFactor, ScalarField func) {
		return blend(this, blendFactor, func);
	}

	public final HeightMap blend(ScalarField blendFactor, ScalarField func) {
		return blend(this.clone(), blendFactor, func);
	}

	@Override
	@SuppressWarnings("unchecked")
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KeyDispatchDataCodecHolder.super.codec();
	}
}
