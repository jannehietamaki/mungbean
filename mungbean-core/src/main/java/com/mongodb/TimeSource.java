package com.mongodb;

public class TimeSource {
	private static TimeSource instance = new TimeSource(System.currentTimeMillis());
	private final long startTime;

	private TimeSource(long startTime) {
		this.startTime = startTime;
	}

	public static TimeSource instance() {
		return instance;
	}

	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static void setCurrentTimeSource(TimeSource source) {
		instance = source;
	}

	public long startTime() {
		return startTime;
	}
}
