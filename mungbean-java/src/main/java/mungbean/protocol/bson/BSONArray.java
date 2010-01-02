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
package mungbean.protocol.bson;

import java.util.ArrayList;
import java.util.List;

public class BSONArray<T> extends AbstractBSONArray<List<T>, T> {
    private final static Class<?> typeClass = List.class;

    @SuppressWarnings("unchecked")
    public BSONArray() {
        super((Class<List<T>>) typeClass);
    }

    @Override
    protected List<T> newInstance() {
        return new ArrayList<T>();
    }

    @Override
    protected List<T> addValue(List<T> ret, T value) {
        ret.add(value);
        return ret;
    }

    @Override
    protected Iterable<KeyValuePair<Integer, T>> valuesOf(List<T> l) {
        List<KeyValuePair<Integer, T>> returnValues = new ArrayList<KeyValuePair<Integer, T>>();
        for (int a = 0; a < l.size(); a++) {
            returnValues.add(new KeyValuePair<Integer, T>(a, l.get(a)));
        }
        return returnValues;
    }
}
