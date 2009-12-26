package mungbean.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class ReflectionUtil {
	private ReflectionUtil() {
	}

	public static FieldDefinition[] fieldsOf(Class<?> typeClass) {
		ArrayList<FieldDefinition> ret = new ArrayList<FieldDefinition>();
		while (typeClass != null) {
			Field[] fields = typeClass.getDeclaredFields();
			for (Field field : fields) {
				if (isAccessible(field)) {
					ret.add(new FieldDefinition(field));
				}
			}
			typeClass = typeClass.getSuperclass();
		}
		return ret.toArray(new FieldDefinition[ret.size()]);
	}

	private static boolean isAccessible(Field field) {
		return !field.isSynthetic() && !Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers());
	}
}
