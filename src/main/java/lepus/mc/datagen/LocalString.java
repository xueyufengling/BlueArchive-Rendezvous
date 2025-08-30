package lepus.mc.datagen;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.contents.TranslatableContents;

/**
 * 用于存放本地化字符串占位符，配合LangDatagen注解使用。
 */
public class LocalString implements Localizable {
	private String path;
	private String defaultValue;
	private TranslatableContents translatableContents;

	private LocalString(String path, String defaultValue) {
		this.path = path;
		this.defaultValue = defaultValue;
		translatableContents = new TranslatableContents(path, defaultValue, null);
	}

	private LocalString(String path) {
		this(path, path);
	}

	@Override
	public String localizationPath() {
		return path;
	}

	public String path() {
		return path;
	}

	public String defaultValue() {
		return defaultValue;
	}

	public String value() {
		return Language.getInstance().getOrDefault(path, defaultValue);
	}

	public TranslatableContents translatableContents() {
		return translatableContents;
	}

	@Override
	public String toString() {
		return "{path=" + path() + ", value=" + value() + "}";
	}

	/**
	 * 返回一个键为path的本地化字符串占位符
	 * 
	 * @param path
	 * @return
	 */
	public static final LocalString forKey(String path) {
		return new LocalString(path);
	}

	public static final LocalString forKey(String path, String defaultValue) {
		return new LocalString(path, defaultValue);
	}
}
