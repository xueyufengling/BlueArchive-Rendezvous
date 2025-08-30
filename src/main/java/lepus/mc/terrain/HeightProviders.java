package lepus.mc.terrain;

import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.heightproviders.VeryBiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.WeightedListHeight;

/**
 * 提供高度的工具类。<br>
 * ExtStructure中generateBeginPos()获取结构生成高度时使用。<br>
 */
public class HeightProviders {
	public static final BiasedToBottomHeight ofBiasedToBottomHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int inner) {
		return BiasedToBottomHeight.of(minInclusive, maxInclusive, inner);
	}

	public static final ConstantHeight ofConstantHeight(VerticalAnchor value) {
		return ConstantHeight.of(value);
	}

	public static final TrapezoidHeight ofTrapezoidHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int plateau) {
		return TrapezoidHeight.of(minInclusive, maxInclusive, plateau);
	}

	public static final UniformHeight ofUniformHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
		return UniformHeight.of(minInclusive, maxInclusive);
	}

	public static final VeryBiasedToBottomHeight ofVeryBiasedToBottomHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int inner) {
		return VeryBiasedToBottomHeight.of(minInclusive, maxInclusive, inner);
	}

	/**
	 * 具有权重的HeightProvider，用于创建WeightedListHeight
	 */
	public static class WeightedHeightProvider {
		public final HeightProvider provider;
		public final int weight;

		private WeightedHeightProvider(HeightProvider provider, int weight) {
			this.provider = provider;
			this.weight = weight;
		}

		public static final WeightedHeightProvider of(HeightProvider provider, int weight) {
			return new WeightedHeightProvider(provider, weight);
		}
	}

	public static final WeightedListHeight ofWeightedListHeight(SimpleWeightedRandomList<HeightProvider> distribution) {
		return new WeightedListHeight(distribution);
	}

	public static final WeightedListHeight ofWeightedListHeight(WeightedHeightProvider... entries) {
		SimpleWeightedRandomList.Builder<HeightProvider> builder = SimpleWeightedRandomList.builder();
		for (WeightedHeightProvider provider_entry : entries)
			builder.add(provider_entry.provider, provider_entry.weight);
		return ofWeightedListHeight(builder.build());
	}
}
