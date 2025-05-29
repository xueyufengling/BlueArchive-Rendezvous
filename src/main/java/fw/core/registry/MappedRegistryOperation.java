package fw.core.registry;

import net.minecraft.core.MappedRegistry;

@FunctionalInterface
public interface MappedRegistryOperation<T> {
	public void operate(MappedRegistry<T> registry);
}
