package lepus.mc.codec.derived;

import com.mojang.serialization.MapCodec;

@SuppressWarnings("rawtypes")
public interface MapCodecHolder<O> extends DerivedCodecHolder<MapCodec, O> {
	@Override
	default Class<MapCodec> codecClass() {
		return MapCodec.class;
	}
}