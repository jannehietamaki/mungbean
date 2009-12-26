package mungbean.util;

import java.lang.reflect.Field;

public class FieldDefinition {
	private final Field field;

	public FieldDefinition(Field field) {
		this.field = field;
		field.setAccessible(true);
	}

	public String name() {
		return field.getName();
	}

	public void set(Object target, Object value) {
		try {
			field.set(target, value);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public Object get(Object source) {
		try {
			return field.get(source);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
