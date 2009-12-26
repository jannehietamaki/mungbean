package mungbean.protocol.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.BSON;

public class InsertRequest extends CollectionRequest<Void> {
	private final List<BSON> documents = new ArrayList<BSON>();

	public InsertRequest(String collectionName, Map<String, Object>... documents) {
		super(collectionName);
		for (Map<String, Object> doc : documents) {
			this.documents.add(toBson(doc));
		}
	}

	@Override
	public void send(LittleEndianDataWriter writer) {
		writer.writeInt(0); // RESERVED
		writer.writeCString(collectionName());
		for (BSON doc : documents) {
			writer.write(doc.bytes());
		}
	}

	@Override
	public int length() {
		int objectSizes = 0;
		for (BSON bson : documents) {
			objectSizes += bson.length();
		}
		return 4 + collectionNameLength() + objectSizes;
	}

	@Override
	public RequestOpCode type() {
		return RequestOpCode.OP_INSERT;
	}

	@Override
	public Void readResponse(LittleEndianDataReader reader) {
		return null;
	}
}
