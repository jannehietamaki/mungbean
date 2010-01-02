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

package mungbean;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import mungbean.protocol.command.Command;
import mungbean.query.Query;

public class DatabaseAdmin {
    private final AbstractDatabase database;

    public DatabaseAdmin(AbstractDatabase database) {
        this.database = database;
    }

    public void dropDatabase() {
        database.mapCollection("$cmd").command(new Command("dropDatabase"));
    }

    public Collection<String> getCollectionNames() {
        List<Map<String, Object>> names = database.mapCollection("system.namespaces").query(new Query());
        HashSet<String> result = new HashSet<String>();
        for (Map<String, Object> name : names) {
            result.add(String.valueOf(name.get("name")).split("\\.")[1]);
        }
        result.remove("system");
        return result;
    }
}
