/*
   Copyright 2009-2010 Janne Hietamäki

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

package mungbean.clojure.command;

import clojure.lang.IPersistentList;
import mungbean.protocol.command.AbstractDistinct;
import mungbean.protocol.message.CommandResponse;

public class ClojureDistinct extends AbstractDistinct<IPersistentList> {

    public ClojureDistinct(String field) {
        super(field);
    }

    @Override
    public IPersistentList parseResponse(CommandResponse values) {
        return (IPersistentList) values.get("values");
    }

}
