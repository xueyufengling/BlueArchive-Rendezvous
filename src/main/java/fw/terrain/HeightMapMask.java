package fw.terrain;

import fw.codec.derived.KeyDispatchDataCodecHolder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

/**
 * 高度图，根据x、z坐标计算出高度y，高于y则方块置空，低于y则密度保持不变
 */
public abstract class HeightMapMask implements DensityFunction.SimpleFunction, KeyDispatchDataCodecHolder<DensityFunction> {

	protected abstract double getHeightValue(int x, int z);

	public HeightMapMask() {
		KeyDispatchDataCodecHolder.super.construct(DensityFunction.class);
	}

	@Override
	public final double compute(DensityFunction.FunctionContext context) {
		return context.blockY() > getHeightValue(context.blockX(), context.blockZ()) ? minValue() : maxValue();
	}

	@Override
	public final double minValue() {
		return 0.0;
	}

	@Override
	public final double maxValue() {
		return 1.0;
	}

	public final DensityFunction apply(DensityFunction func) {
		return DensityFunctions.mul(func, this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return KeyDispatchDataCodecHolder.super.codec();
	}
}
