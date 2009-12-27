/*
   Copyright 2009 Janne Hietamäki

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package mungbean.protocol.bson;

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
		encoders.add(new BSONPattern());
		encoders.add(new BSONDate());
		encoders.add(new BSONBoolean());
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
