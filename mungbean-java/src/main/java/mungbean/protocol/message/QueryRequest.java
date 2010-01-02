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

import java.util.LinkedHashMap;
import java.util.Map;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSON;
import mungbean.protocol.bson.BSONCoder;
import mungbean.protocol.bson.BSONMap;
import mungbean.query.QueryBuilder;

public class QueryRequest<ResponseType> extends CollectionRequest<QueryResponse<ResponseType>> {

    private final QueryOptionsBuilder builder;
    private final int numberToSkip;
    private final int numberToReturn;
    private final BSON query;
    private final boolean closeCursor;
    private final BSONCoder<ResponseType> coder;

    public QueryRequest(String collectionName, QueryOptionsBuilder builder, QueryBuilder query, boolean closeCursor, AbstractBSONCoders coders, BSONCoder<ResponseType> coder) {
        super(collectionName);
        this.builder = builder;
        this.numberToSkip = query.skip();
        this.numberToReturn = query.limit();
        this.closeCursor = closeCursor;
        this.query = buildQuery(coders, query.build(), query.order());
        this.coder = coder;
    }

    public QueryRequest(String collectionName, QueryOptionsBuilder builder, Map<String, Object> params, Map<String, Object> orders, AbstractBSONCoders coders, BSONCoder<ResponseType> coder) {
        super(collectionName);
        this.builder = builder;
        this.numberToSkip = 0;
        this.numberToReturn = 1;
        this.closeCursor = true;
        this.query = buildQuery(coders, params, orders);
        this.coder = coder;
    }

    private BSON buildQuery(AbstractBSONCoders coders, final Map<String, Object> query, final Map<String, Object> order) {
        if (order == null || order.isEmpty()) {
            return coders.forValue(query).write(coders, query);
        }
        return new BSONMap().write(coders, new LinkedHashMap<String, Object>() {
            {
                put("query", query);
                put("orderby", order);
            }
        });
    }

    @Override
    public int length() {
        return 4 + collectionNameLength() + 4 + 4 + query.length();
    }

    @Override
    public QueryResponse<ResponseType> readResponse(LittleEndianDataReader reader) {
        return new QueryResponse<ResponseType>(reader, coder);
    }

    @Override
    public void send(LittleEndianDataWriter writer) {
        writer.writeInt(builder.build());
        writeCollectionName(writer);
        writer.writeInt(numberToSkip);
        if (closeCursor) {
            if (numberToReturn > 0) {
                writer.writeInt(-numberToReturn);
            } else {
                writer.writeInt(Integer.MIN_VALUE);
            }
        } else {
            writer.writeInt(numberToReturn);
        }
        writer.write(query.bytes());
    }

    @Override
    public RequestOpCode type() {
        return RequestOpCode.OP_QUERY;
    }
}
