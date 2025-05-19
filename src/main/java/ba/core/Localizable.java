package ba.core;

import net.minecraft.network.chat.Component;

public interface Localizable {

	public abstract String localizationKey();

	public default Component localizedComponent() {
		return Component.translatable(localizationKey());
	}
}
