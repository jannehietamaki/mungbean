package mungbean.protocol.message;

import java.util.Map;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.BSON;

public class QueryRequest extends CollectionRequest<QueryResponse> {

	private final QueryOptionsBuilder builder;
	private final int numberToSkip;
	private final int numberToReturn;
	private final BSON query;

	public QueryRequest(String collectionName, QueryOptionsBuilder builder, int numberToSkip, int numberToReturn, Map<String, Object> query) {
		super(collectionName);
		this.builder = builder;
		this.numberToSkip = numberToSkip;
		this.numberToReturn = numberToReturn;
		this.query = toBson(query);
	}

	@Override
	public int length() {
		return 4 + collectionNameLength() + 4 + 4 + query.length();
	}

	@Override
	public QueryResponse readResponse(LittleEndianDataReader reader) {
		return new QueryResponse(reader);
	}

	@Override
	public void send(LittleEndianDataWriter writer) {
		writer.writeInt(builder.build());
		writer.writeCString(collectionName());
		writer.writeInt(numberToSkip);
		writer.writeInt(numberToReturn);
		writer.write(query.bytes());
	}

	@Override
	public RequestOpCode type() {
		return RequestOpCode.OP_QUERY;
	}
}
