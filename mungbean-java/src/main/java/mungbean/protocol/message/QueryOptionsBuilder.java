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

public class QueryOptionsBuilder {
    private int value = 0;

    public QueryOptionsBuilder tailableCursor() {
        value = value | 2;
        return this;
    }

    public QueryOptionsBuilder slaveOk() {
        value = value | 4;
        return this;
    }

    public QueryOptionsBuilder oplogReplay() {
        value = value | 8;
        return this;
    }

    public QueryOptionsBuilder noCursorTimeout() {
        value = value | 16;
        return this;
    }

    public int build() {
        return value;
    }
}
