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

import mungbean.protocol.bson.AbstractBSONCoders;
import mungbean.protocol.bson.BSONBoolean;
import mungbean.protocol.bson.BSONDate;
import mungbean.protocol.bson.BSONEndMarker;
import mungbean.protocol.bson.BSONInteger;
import mungbean.protocol.bson.BSONNull;
import mungbean.protocol.bson.BSONNumber;
import mungbean.protocol.bson.BSONOid;
import mungbean.protocol.bson.BSONPattern;
import mungbean.protocol.bson.BSONString;

public class ClojureBSONCoders extends AbstractBSONCoders {
	public ClojureBSONCoders() {
		addEncoder(new BSONEndMarker());
		addEncoder(new BSONNull());
		addEncoder(new ClojureBSONSeq<Object>());
		addEncoder(new BSONInteger());
		addEncoder(new BSONNumber());
		addEncoder(new BSONString());
		addEncoder(new ClojureBSONMap());
		addEncoder(new BSONOid());
		addEncoder(new BSONPattern());
		addEncoder(new BSONDate());
		addEncoder(new BSONBoolean());
	}
}
