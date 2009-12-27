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

public class Mungbean {
	private final DbOperationExecutor executor;

	public Mungbean(String host, int port) {
		executor = new DbOperationExecutor(host, port);
	}

	public Database openDatabase(String name) {
		return new Database(executor, name);
	}

	public void close() {
		executor.close();
	}
}
