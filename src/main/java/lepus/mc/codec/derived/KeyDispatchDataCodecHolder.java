package lepus.mc.codec.derived;

import net.minecraft.util.KeyDispatchDataCodec;

@SuppressWarnings("rawtypes")
public interface KeyDispatchDataCodecHolder<O> extends DerivedCodecHolder<KeyDispatchDataCodec, O> {
	@Override
	default Class<KeyDispatchDataCodec> codecClass() {
		return KeyDispatchDataCodec.class;
	}
}