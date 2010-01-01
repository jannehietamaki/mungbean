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

package mungbean.protocol.command;

import java.util.Map;

import mungbean.DBCollection;
import mungbean.protocol.message.CommandResponse;
import mungbean.query.QueryBuilder;

public class AggregationCommand<ResponseType> extends AbstractCommand<ResponseType> {
	private final Aggregation<ResponseType> aggregation;
	private final QueryBuilder query;

	public AggregationCommand(Aggregation<ResponseType> aggregation, QueryBuilder query) {
		this.aggregation = aggregation;
		this.query = query;
	}

	@Override
	public ResponseType parseResponse(CommandResponse values) {
		return aggregation.parseResponse(values);
	}

	@Override
	public Map<String, Object> requestMap(DBCollection<?> collection) {
		return aggregation.requestMap(collection, query);
	}

}
