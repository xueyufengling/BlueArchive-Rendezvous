package lepus.mc.terrain.feature;

import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import lepus.mc.codec.annotation.AsDataField;
import lepus.mc.codec.annotation.CodecAutogen;
import lepus.mc.codec.annotation.CodecEntry;
import lepus.mc.codec.annotation.CodecTarget;
import lepus.mc.codec.derived.CodecHolder;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * 地物的生成参数，不同地物参数可不一样<br>
 * 不同的地物也可以共享相同的参数，即都注册到同一个FeatureConfiguration
 */
@AsDataField
public class FeatureGenerationParams implements FeatureConfiguration, CodecHolder<FeatureGenerationParams> {
	@CodecAutogen(null_if_empty = false)
	public static final Codec<FeatureGenerationParams> CODEC = null;

	/**
	 * 无参数
	 */
	public static final FeatureGenerationParams NONE = new FeatureGenerationParams(List.of());

	static {
		CodecAutogen.CodecGenerator.Codec(List.class);
	}

	/**
	 * 使用该参数的地物列表
	 */
	@CodecEntry
	protected List<ConfiguredFeature<?, ?>> feature_list;

	@CodecTarget
	public FeatureGenerationParams(List<ConfiguredFeature<?, ?>> feature_list) {
		CodecHolder.super.construct(FeatureGenerationParams.class);
		this.feature_list = feature_list;
	}

	public Stream<ConfiguredFeature<?, ?>> getFeatures() {
		return feature_list.parallelStream();
	}
}
