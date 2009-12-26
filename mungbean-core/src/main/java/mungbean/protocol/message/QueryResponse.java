package com.mongodb.protocol.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.bson.BSONCoders;
import com.mongodb.protocol.bson.BSONMap;

public class QueryResponse extends MongoResponse {
	private static final BSONCoders BSON = new BSONCoders();
	private final int responseFlag;
	private final long cursorId;
	private final int startingFrom;
	private final int numberReturned;
	private final List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

	public QueryResponse(LittleEndianDataReader reader) {
		super(reader);
		responseFlag = reader.readInt();
		cursorId = reader.readLong();
		startingFrom = reader.readInt();
		numberReturned = reader.readInt();
		for (int i = 0; i < numberReturned; i++) {
			values.add(new BSONMap().read(BSON, reader));
		}
	}

	public int responseFlag() {
		return responseFlag;
	}

	public long cursorId() {
		return cursorId;
	}

	public int startingFrom() {
		return startingFrom;
	}

	public int numberReturned() {
		return numberReturned;
	}

	public List<Map<String, Object>> values() {
		return values;
	}
}
