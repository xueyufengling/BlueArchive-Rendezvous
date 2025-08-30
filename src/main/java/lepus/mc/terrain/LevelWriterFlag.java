package lepus.mc.terrain;

/**
 * 存档方块写入标志
 */
public class LevelWriterFlag {
	/**
	 * 写入时本方块更新
	 */
	public static final int BLOCK_UPDATE = 1;

	/**
	 * 写入时通知客户端有更新
	 */
	public static final int CLIENT_UPDATE = 2;

	/**
	 * 写入后无需重新渲染
	 */
	public static final int NO_RERENDERING = 4;

	/**
	 * 写入后强制重新渲染
	 */
	public static final int FORCE_RERENDERING = 8;

	/**
	 * 写入时临近方块不更新
	 */
	public static final int NO_NEAR_UPDATE = 16;

	/**
	 * 临近方块被移除时不再掉落
	 */
	public static final int NO_NEAR_DROP = 32;

	/**
	 * 向外通知本方块正在改动
	 */
	public static final int MOVING = 64;
}
