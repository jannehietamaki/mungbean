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

import java.util.Map;

import mungbean.protocol.bson.BSONCoder;
import mungbean.protocol.bson.BSONCoders;
import mungbean.protocol.bson.BSONMap;

public class MapDBCollection extends AbstractDBCollection<Map<String, Object>> {

	public MapDBCollection(DBOperationExecutor executor, String dbName, String collectionName) {
		super(executor, dbName, collectionName, new BSONCoders());
	}

	@Override
	public BSONCoder<Map<String, Object>> defaultEncoder() {
		return new BSONMap();
	}

	@Override
	protected Map<String, Object> injectId(Map<String, Object> doc) {
		if (!doc.containsKey("_id")) {
			doc.put("_id", new ObjectId());
		}
		return doc;
	}
}
