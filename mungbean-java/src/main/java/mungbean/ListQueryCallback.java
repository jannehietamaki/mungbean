/*
   Copyright 2009-2010 Janne Hietamäki

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

package mungbean;

import java.util.ArrayList;
import java.util.List;

public class ListQueryCallback<T> implements QueryCallback<T> {
    private final List<T> values = new ArrayList<T>();

    public List<T> values() {
        return values;
    }

    @Override
    public boolean process(T item) {
        values.add(item);
        return true;
    }
}
