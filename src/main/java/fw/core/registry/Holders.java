package fw.core.registry;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import lyra.klass.GenericTypes;

public class Holders {
	public static final Class<?> getHolderType(Type holderField) {
		return GenericTypes.getFirstGenericType(holderField);
	}
}
