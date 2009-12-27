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

public abstract class CollectionRequest<ReturnType> extends MongoRequest<ReturnType> {
	private final String collectionName;

	public CollectionRequest(String collectionName) {
		this.collectionName = collectionName;
	}

	protected String collectionName() {
		return collectionName;
	}

	protected int collectionNameLength() {
		return 1 + collectionName.getBytes(UTF8).length;
	}
}
