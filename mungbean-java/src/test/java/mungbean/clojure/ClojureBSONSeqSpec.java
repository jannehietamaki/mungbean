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

package mungbean.clojure;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

import clojure.lang.ISeq;

@RunWith(JDaveRunner.class)
public class ClojureBSONSeqSpec extends Specification<ClojureBSONSeq<Integer>> {
    public class WithEmptySeq {
        public ClojureBSONSeq<Integer> create() {
            return new ClojureBSONSeq<Integer>();
        }

        public void itemsCanBeAddedIntoSequence() {
            ISeq seq = context.newInstance();
            ISeq value = context.addValue(seq, 1);
            ISeq value2 = context.addValue(value, 2);
            specify(value2.count(), does.equal(2));
            specify(value2.first(), does.equal(2));
            specify(value2.next().first(), does.equal(1));
        }
    }

}
