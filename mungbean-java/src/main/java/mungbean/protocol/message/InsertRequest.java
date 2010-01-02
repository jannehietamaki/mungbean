package mungbean.protocol.message;

import java.util.ArrayList;
import java.util.List;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSON;
import mungbean.protocol.bson.BSONCoder;

public class InsertRequest<Type> extends CollectionRequest<NoResponseExpected> {
    private final List<BSON> documents = new ArrayList<BSON>();

    public InsertRequest(String collectionName, AbstractBSONCoders coders, Type... documents) {
        super(collectionName);
        for (Type doc : documents) {
            BSONCoder<Type> coder = coders.forValue(doc);
            this.documents.add(coder.write(coders, doc));
        }
    }

    @Override
    public void send(LittleEndianDataWriter writer) {
        writer.writeInt(0); // RESERVED
        writeCollectionName(writer);
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
    public NoResponseExpected readResponse(LittleEndianDataReader reader) {
        return new NoResponseExpected();
    }
}
