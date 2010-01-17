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

package mungbean;

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class UtilsSpec extends Specification<Utils> {
    public class WithAny {
        public void hexStringCanBeParsed() {
            specify(Utils.hexStringToBytes("010203040506"), does.containExactly(new byte[] { 1, 2, 3, 4, 5, 6 }));
        }

        public void exceptionIsThrownIfInputContainsInvalidCharacters() {
            specify(new Block() {

                @Override
                public void run() throws Throwable {
                    specify(Utils.hexStringToBytes("hv312123123123"), does.equal(new byte[] {}));
                }

            }, does.raise(RuntimeException.class, "Input contains invalid characters: hv312123123123"));
        }

        public void exceptionIsTrhownIfInputHasOddNumberOfCharacters() {
            specify(new Block() {

                @Override
                public void run() throws Throwable {
                    specify(Utils.hexStringToBytes("123123123123123"), does.equal(new byte[] {}));
                }

            }, does.raise(RuntimeException.class, "hex string must contain even number of characters"));
        }
    }
}
