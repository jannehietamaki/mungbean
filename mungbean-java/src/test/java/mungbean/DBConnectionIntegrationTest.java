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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import mungbean.protocol.DBConnection;
import mungbean.protocol.message.DeleteRequest;
import mungbean.protocol.message.InsertRequest;
import mungbean.protocol.message.QueryOptionsBuilder;
import mungbean.protocol.message.QueryRequest;
import mungbean.protocol.message.QueryResponse;
import mungbean.protocol.message.UpdateOptionsBuilder;
import mungbean.protocol.message.UpdateRequest;

@RunWith(JDaveRunner.class)
public class DBConnectionIntegrationTest extends Specification<DBConnection> {
	public class WithServer {

		public DBConnection create() {
			return new DBConnection("localhost", 27017);
		}

		public void destroy() {
			context.close();
		}

		@SuppressWarnings("unchecked")
		public void itemCanBeInsertedQueriedAndRemoved() {
			final ObjectId id = new ObjectId();
			context.execute(new InsertRequest("foozbar.foo", new HashMap<String, Object>() {
				{
					put("foo", "bar");
					put("_id", id);
				}
			}));
			HashMap<String, Object> idQuery = new HashMap<String, Object>() {
				{
					put("_id", id);
				}
			};
			QueryResponse response = context.execute(new QueryRequest("foozbar.foo", new QueryOptionsBuilder(), 0, 0, true, idQuery));
			List<Map<String, Object>> values = response.values();
			specify(((HashMap<String, Object>) values.get(0)).get("_id"), does.equal(id));
			context.execute(new UpdateRequest("foozbar.foo", new UpdateOptionsBuilder(), idQuery, new HashMap<String, Object>() {
				{
					put("zoo", 5);
				}
			}));
			specify(((HashMap<String, Object>) context.execute(new QueryRequest("foozbar.foo", new QueryOptionsBuilder(), 0, 0, true, idQuery)).values().get(0)).get("zoo"), does.equal(5));
			context.execute(new DeleteRequest("foozbar.foo", idQuery));
			specify(context.execute(new QueryRequest("foozbar.foo", new QueryOptionsBuilder(), 0, 0, true, idQuery)).values().size(), does.equal(0));
		}
	}
}
