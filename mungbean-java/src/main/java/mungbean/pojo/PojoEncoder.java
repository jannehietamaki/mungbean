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

import java.util.HashMap;
import java.util.Map;

import mungbean.protocol.LittleEndianDataReader;
import mungbean.protocol.LittleEndianDataWriter;
import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSONCoder;
import mungbean.protocol.bson.BSONMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class PojoEncoder<T> extends BSONCoder<T> {
    private final BSONMap mapCoder = new BSONMap();
    private final static Objenesis objenesis = new ObjenesisStd();
    private final Class<T> typeClass;

    public PojoEncoder(Class<T> typeClass) {
        super(3, Object.class);
        this.typeClass = typeClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T decode(AbstractBSONCoders bson, LittleEndianDataReader reader) {
        Map<String, Object> map = mapCoder.read(bson, reader);
        T ret = (T) objenesis.newInstance(typeClass);
        FieldDefinition[] fields = ReflectionUtil.fieldsOf(typeClass);
        for (FieldDefinition field : fields) {
            field.set(ret, map.get(field.name()));
        }
        return ret;
    }

    @Override
    protected void encode(AbstractBSONCoders bson, T o, LittleEndianDataWriter writer) {
        Map<String, Object> ret = new HashMap<String, Object>();
        FieldDefinition[] fields = ReflectionUtil.fieldsOf(typeClass);
        for (FieldDefinition field : fields) {
            ret.put(field.name(), field.get(o));
        }
        mapCoder.encode(bson, ret, writer);
    }

}
