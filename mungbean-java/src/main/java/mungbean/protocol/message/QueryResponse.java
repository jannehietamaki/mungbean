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

public class QueryResponse<ResponseType> extends MongoResponse {
    public final int CURSOR_NOT_FOUND = 1;
    public final int QUERY_FAILURE = 2;
    public final int SHARD_CONFIG_STALE = 4;
    public final int AWAIT_CAPABLE = 8;

    private final AbstractBSONCoders coders;
    private final int responseFlag;
    private final long cursorId;
    private final int startingFrom;
    private final int numberReturned;
    private final BSONCoder<ResponseType> coder;
    private final LittleEndianDataReader reader;

    public QueryResponse(LittleEndianDataReader reader, BSONCoder<ResponseType> coder, AbstractBSONCoders coders) {
        super(reader);
        this.reader = reader;
        this.coder = coder;
        responseFlag = reader.readInt();
        cursorId = reader.readLong();
        startingFrom = reader.readInt();
        numberReturned = reader.readInt();
        this.coders = coders;
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

    public boolean readResponse(QueryCallback<ResponseType> callback) {
        int numToFetch = numberReturned;
        boolean readMore = true;
        for (int i = 0; i < numToFetch; i++) {
            ResponseType item = coder.read(coders, reader);
            if (readMore) {
                readMore = callback.process(item);
            }
        }
        return readMore;
    }
}
