package com.mongodb.protocol.bson;

public class BSON {
	private final byte[] data;

	public BSON(byte[] data) {
		this.data = data;
	}

	public int length() {
		return data.length;
	}

	public byte[] bytes() {
		return data;
	}

}
