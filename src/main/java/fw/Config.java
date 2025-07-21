package fw;

import org.slf4j.Logger;

import ba.ModEntry;

public class Config {
	/**
	 * 模组的ID，亦即命名空间
	 */
	public static final String ModId = ModEntry.ModId;

	/**
	 * 日志记录器
	 */
	public static final Logger Logger = ModEntry.Logger;

	/**
	 * 是否输出日志消息
	 */
	public static final boolean logInfo = true;

	/**
	 * 是否自己加载依赖库，如果配置了Jar in jar则不需要自己加载
	 */
	public static final boolean loadLibBySelf = false;

	/**
	 * 依赖jar库Lyra的路径，使用jar包内路径，仅当loadLibBySelf为true时有效
	 */
	public static String LyraPath = "/libs/Lyra.jar";

	/**
	 * 依赖jar库Alpha-Lyr的路径，使用jar包内路径，仅当loadLibBySelf为true时有效
	 */
	public static String AlphaLyrPath = "/libs/Alpha-Lyr.jar";
}
