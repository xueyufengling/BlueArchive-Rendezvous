package fw.datagen;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.contents.TranslatableContents;

/**
 * 用于存放本地化字符串占位符，配合LangDatagen注解使用。
 */
public class LocalString implements Localizable {
	private String key;
	private String defaultValue;
	private TranslatableContents translatableContents;

	private LocalString(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
		translatableContents = new TranslatableContents(key, defaultValue, null);
	}

	private LocalString(String key) {
		this(key, key);
	}

	@Override
	public String localizationKey() {
		return key;
	}

	public String key() {
		return key;
	}

	public String defaultValue() {
		return defaultValue;
	}

	public String value() {
		return Language.getInstance().getOrDefault(key, defaultValue);
	}

	public TranslatableContents translatableContents() {
		return translatableContents;
	}

	@Override
	public String toString() {
		return "{key=" + key() + ", value=" + value() + "}";
	}

	/**
	 * 返回一个键为key的本地化字符串占位符
	 * 
	 * @param key
	 * @return
	 */
	public static final LocalString forKey(String key) {
		return new LocalString(key);
	}

	public static final LocalString forKey(String key, String defaultValue) {
		return new LocalString(key, defaultValue);
	}
}
