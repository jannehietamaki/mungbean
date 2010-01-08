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
package mungbean.pojo;

import java.lang.reflect.Field;

import mungbean.AbstractDBCollection;
import mungbean.DBOperationExecutor;
import mungbean.ObjectId;
import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSONCoder;
import mungbean.protocol.bson.MapBSONCoders;

public class PojoDBCollection<T> extends AbstractDBCollection<T> {
    private final Class<T> typeClass;

    public PojoDBCollection(DBOperationExecutor executor, String dbName, String collectionName, final Class<T> typeClass) {
        this(executor, dbName, collectionName, typeClass, new PojoBSONCoders<T>(typeClass), new MapBSONCoders());
    }

    public PojoDBCollection(DBOperationExecutor executor, String dbName, String collectionName, final Class<T> typeClass, AbstractBSONCoders documentCoders, AbstractBSONCoders queryCoders) {
        super(executor, dbName, collectionName, documentCoders, queryCoders);
        this.typeClass = typeClass;
    }

    @Override
    public BSONCoder<T> defaultEncoder() {
        return new PojoEncoder<T>(typeClass);
    }

    @Override
    protected T injectId(T doc) {
        try {
            Field field = doc.getClass().getField("_id");
            field.setAccessible(true);
            if (field.get(doc) == null) {
                field.set(doc, new ObjectId());
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            // Ignore
        }
        return doc;
    }

}
