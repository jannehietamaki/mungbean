package com.mongodb.protocol.bson;

import java.util.ArrayList;
import java.util.List;

public class BSONCoders {
	private final List<BSONCoder<?>> encoders = new ArrayList<BSONCoder<?>>();
	private final static BSONCoders instance = new BSONCoders();

	public static BSONCoders instance() {
		return instance;
	}

	public BSONCoders() {
		encoders.add(new BSONEndMarker());
		encoders.add(new BSONNull());
		encoders.add(new BSONArray());
		encoders.add(new BSONInteger());
		encoders.add(new BSONNumber());
		encoders.add(new BSONString());
		encoders.add(new BSONArray());
		encoders.add(new BSONMap());
		encoders.add(new BSONOid());
	}

	public BSONCoder<?> forType(byte type) {
		for (BSONCoder<?> bson : encoders) {
			if (bson.type() == type) {
				return bson;
			}
		}
		throw new IllegalArgumentException("Unsupported BSON type " + type);
	}

	@SuppressWarnings("unchecked")
	public <T> BSONCoder<T> forValue(T val) {
		for (BSONCoder<?> bson : encoders) {
			if (bson.canEncode(val)) {
				return (BSONCoder<T>) bson;
			}
		}
		throw new IllegalArgumentException("Unable to find encoder for object " + val);
	}

}
