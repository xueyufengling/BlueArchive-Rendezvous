package lepus.mc.core;

public interface Tickable {
	public default Object tick(Object... args) {
		return null;
	}
}
