package fw.core.registry;

import java.lang.reflect.Type;

import lyra.klass.GenericTypes;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Holder;

public class Holders {
	public static final Class<?> getHolderType(Type holderField) {
		return GenericTypes.getFirstGenericType(holderField);
	}

	public static final <T> Holder.Reference<T> bindValue(Holder.Reference<T> holder, T value) {
		ObjectManipulator.setMemberObject(holder, "value", value);
		return holder;
	}
}
