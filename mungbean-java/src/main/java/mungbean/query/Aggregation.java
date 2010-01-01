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

package mungbean.query;

import java.util.List;

import mungbean.protocol.command.AbstractCommand;
import mungbean.protocol.command.Count;
import mungbean.protocol.command.Distinct;

public class Aggregation<ReturnType> implements AggregationBuilder<ReturnType> {
	private final AbstractCommand<ReturnType> command;

	private Aggregation(AbstractCommand<ReturnType> command) {
		this.command = command;
	}

	public static AggregationBuilder<List<Object>> distinct(String field) {
		return new Aggregation<List<Object>>(new Distinct(field));
	}

	public static AggregationBuilder<List<Object>> distinct(String field, QueryBuilder query) {
		return new Aggregation<List<Object>>(new Distinct(field, query));
	}

	public static AggregationBuilder<Long> count() {
		return new Aggregation<Long>(new Count());
	}

	public static AggregationBuilder<Long> count(QueryBuilder query) {
		return new Aggregation<Long>(new Count(query));
	}

	public AbstractCommand<ReturnType> build() {
		return command;
	}
}
