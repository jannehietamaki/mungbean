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

package mungbean.clojure;

import java.util.ArrayList;
import java.util.List;

import mungbean.protocol.bson.AbstractBSONArray;
import mungbean.protocol.bson.KeyValuePair;
import clojure.lang.ISeq;
import clojure.lang.PersistentList;

public class ClojureBSONSeq<T> extends AbstractBSONArray<ISeq, T> {

    public ClojureBSONSeq() {
        super(ISeq.class);
    }

    @Override
    protected ISeq addValue(ISeq ret, T value) {
        return ret.cons(value);
    }

    @Override
    protected ISeq newInstance() {
        return PersistentList.EMPTY;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterable<KeyValuePair<Integer, T>> valuesOf(ISeq l) {
        List<KeyValuePair<Integer, T>> returnValues = new ArrayList<KeyValuePair<Integer, T>>();
        for (int i = 0; i < l.count(); i++) {
            returnValues.add(new KeyValuePair<Integer, T>(i, (T) l.first()));
            l = l.next();
        }
        return returnValues;
    }
}
