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

import mungbean.Assert;
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
    private final BSONCoder<ResponseType> coder;
    private final AbstractBSONCoders coders;
    
    public QueryRequest(String collectionName, QueryOptionsBuilder builder, QueryBuilder query, AbstractBSONCoders coders, BSONCoder<ResponseType> coder) {
        this(collectionName, builder, query.build(), query.order(), coders, coder, query.skip(), query.limit());
    }

    public QueryRequest(String collectionName, QueryOptionsBuilder builder, Map<String, Object> params, Map<String, Object> orders, AbstractBSONCoders coders, BSONCoder<ResponseType> coder) {
        this(collectionName, builder, params, orders, coders, coder, 0, 1);
    }

    private QueryRequest(String collectionName, QueryOptionsBuilder builder, Map<String, Object> params, Map<String, Object> orders, AbstractBSONCoders coders, BSONCoder<ResponseType> coder, int numberToSkip, int numberToReturn) {
        super(collectionName);
        this.builder = builder;
        this.numberToSkip = numberToSkip;
        this.numberToReturn = Math.abs(numberToReturn);
        this.query = buildQuery(coders, params, orders);
        this.coder = coder;
        this.coders = coders;
    }

    private BSON buildQuery(AbstractBSONCoders coders, final Map<String, Object> query, final Map<String, Object> order) {
        Assert.notNull(query, "Query can not be null");
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
        return new QueryResponse<ResponseType>(reader, coder, coders);
    }

    @Override
    public void send(LittleEndianDataWriter writer) {
        writer.writeInt(builder.build());
        writeCollectionName(writer);
        writer.writeInt(numberToSkip);
        // if limit it set, fetch n number items and close cursor, otherwise
        // fetch 5000 items and leave cursor open
        if (numberToReturn > 0) {
            writer.writeInt(-numberToReturn);
        } else {
            writer.writeInt(5000);
        }
        writer.write(query.bytes());
    }

    @Override
    public RequestOpCode type() {
        return RequestOpCode.OP_QUERY;
    }
}
