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
package mungbean.protocol.message;

import mungbean.QueryCallback;
import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSONCoder;
import mungbean.protocol.bson.MapBSONCoders;

public class QueryResponse<ResponseType> extends MongoResponse {
    private static final AbstractBSONCoders BSON = new MapBSONCoders();
    private final int responseFlag;
    private final long cursorId;
    private final int startingFrom;
    private final int numberReturned;
    private final BSONCoder<ResponseType> coder;
    private final LittleEndianDataReader reader;

    public QueryResponse(LittleEndianDataReader reader, BSONCoder<ResponseType> coder) {
        super(reader);
        this.reader = reader;
        this.coder = coder;
        responseFlag = reader.readInt();
        cursorId = reader.readLong();
        startingFrom = reader.readInt();
        numberReturned = reader.readInt();
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

    public void readResponse(QueryCallback<ResponseType> callback) {
        int numToFetch = numberReturned;
        if (responseFlag > 0) {
            numToFetch = 1;
        }
        for (int i = 0; i < numToFetch; i++) {
            callback.process(coder.read(BSON, reader));
        }
    }
}
