package com.mongodb;

public class StopWatch {
	private final long startTime = System.currentTimeMillis();

	public long millisecondsSinceStart() {
		return System.currentTimeMillis() - startTime;
	}
}
