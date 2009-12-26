package com.mongodb;

public class Assert {
	public static void isTrue(boolean value, String message) {
		if (!value) {
			throw new RuntimeException(message);
		}
	}
}
