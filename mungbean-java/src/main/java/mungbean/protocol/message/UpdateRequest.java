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

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSON;
import mungbean.query.QueryBuilder;
import mungbean.query.UpdateBuilder;

public class UpdateRequest<Type> extends CollectionRequest<NoResponseExpected> {
    private final UpdateOptionsBuilder flags;
    private final BSON selector;
    private final BSON updates;

    public UpdateRequest(String collectionName, QueryBuilder selector, UpdateBuilder updates, AbstractBSONCoders coders, AbstractBSONCoders selectorCoders) {
        this(collectionName, selector, updates.options(), updates.build(), coders, selectorCoders);
    }

    public UpdateRequest(String collectionName, QueryBuilder selector, UpdateOptionsBuilder updateOptions, Object updates, AbstractBSONCoders coders, AbstractBSONCoders selectorCoders) {
        super(collectionName);
        this.flags = updateOptions;
        this.selector = selectorCoders.forValue(selector.build()).write(coders, selector.build());
        this.updates = coders.forValue(updates).write(coders, updates);
    }

    @Override
    public int length() {
        return 4 + collectionNameLength() + 4 + selector.length() + updates.length();
    }

    @Override
    public void send(LittleEndianDataWriter writer) {
        writer.writeInt(0); // RESERVED
        writeCollectionName(writer);
        writer.writeInt(flags.value());
        writer.write(selector.bytes());
        writer.write(updates.bytes());
    }

    @Override
    public RequestOpCode type() {
        return RequestOpCode.OP_UPDATE;
    }

    @Override
    public NoResponseExpected readResponse(LittleEndianDataReader reader) {
        return new NoResponseExpected();
    }
}
