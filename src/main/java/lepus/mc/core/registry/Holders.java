package lepus.mc.core.registry;

import java.lang.reflect.Type;

import lyra.klass.GenericTypes;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Holder;

public class Holders {
	public static final Class<?> getHolderType(Type holderField) {
		return GenericTypes.getFirstGenericType(holderField);
	}

	public static final <T, H extends Holder<T>> H bindValue(H holder, T value) {
		ObjectManipulator.setDeclaredMemberObject(holder, "value", value);
		return holder;
	}

	@SuppressWarnings("unchecked")
	public static final <T> T getValue(Holder<T> holder) {
		return (T) ObjectManipulator.getDeclaredMemberObject(holder, "value");
	}
}
