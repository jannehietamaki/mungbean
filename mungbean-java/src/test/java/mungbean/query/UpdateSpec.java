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

import static mungbean.CollectionUtil.*;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class UpdateSpec extends Specification<Update> {
    public class WithFieldIncrement {
        public Update create() {
            Update update = new Update();
            update.field("foo").increment(5);
            return update;
        }

        public void generatesMapContainingMongoRules() {
            specify(context.build(), does.equal(map("$inc", map("foo", 5))));
        }
    }

    public class WithFieldSet {
        public Update create() {
            Update update = new Update();
            update.field("foo").set("5");
            return update;
        }

        public void generatesMapContainingMongoRules() {
            specify(context.build(), does.equal(map("$set", map("foo", "5"))));
        }
    }

    public class WithFieldUnset {
        public Update create() {
            Update update = new Update();
            update.field("foo").unset();
            return update;
        }

        public void generatesMapContainingMongoRules() {
            specify(context.build(), does.equal(map("$unset", map("foo", 1D))));
        }
    }

    public class WithPushItemToArray {
        public Update create() {
            Update update = new Update();
            update.field("foo").push(1);
            return update;
        }

        public void generatesMapContainingMongoRules() {
            specify(context.build(), does.equal(map("$push", map("foo", 1))));
        }
    }

    public class WithPushAllItemsToArray {
        public Update create() {
            Update update = new Update();
            update.field("foo").push(1, 2, 3, 4);
            return update;
        }

        public void generatesMapContainingMongoRules() {
            specify(context.build(), does.equal(map("$pushAll", map("foo", list(1, 2, 3, 4)))));
        }
    }

    public class PopLastElementFromArray {
        public Update create() {
            Update update = new Update();
            update.field("foo").popLast();
            return update;
        }

        public void generatesMapContainingMongoRules() {
            specify(context.build(), does.equal(map("$pop", map("foo", 1D))));
        }
    }

    public class PopFirstElementFromArray {
        public Update create() {
            Update update = new Update();
            update.field("foo").popFirst();
            return update;
        }

        public void generatesMapContainingMongoRules() {
            specify(context.build(), does.equal(map("$pop", map("foo", -1D))));
        }
    }

    public class PullElementsFromArray {
        public Update create() {
            Update update = new Update();
            update.field("foo").pull("bar");
            return update;
        }

        public void generatesMapContainingMongoRules() {
            specify(context.build(), does.equal(map("$pull", map("foo", "bar"))));
        }
    }

    public class PullAllElementsFromArray {
        public Update create() {
            Update update = new Update();
            update.field("foo").pull("bar", "zoo");
            return update;
        }

        public void generatesMapContainingMongoRules() {
            specify(context.build(), does.equal(map("$pullAll", map("foo", list("bar", "zoo")))));
        }
    }

}
