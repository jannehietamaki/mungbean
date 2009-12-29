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

import mungbean.DBOperationExecutor;

public class ClojureDatabase {
	private final String dbName;
	private final DBOperationExecutor executor;

	public ClojureDatabase(DBOperationExecutor executor, String name) {
		this.dbName = name;
		this.executor = executor;
	}

	public ClojureDBCollection openCollection(String name) {
		return new ClojureDBCollection(executor, dbName, name);
	}
}
