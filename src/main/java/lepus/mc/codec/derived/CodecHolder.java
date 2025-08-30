package lepus.mc.codec.derived;

import com.mojang.serialization.Codec;

@SuppressWarnings("rawtypes")
public interface CodecHolder<O> extends DerivedCodecHolder<Codec, O> {
	@Override
	default Class<Codec> codecClass() {
		return Codec.class;
	}
}
