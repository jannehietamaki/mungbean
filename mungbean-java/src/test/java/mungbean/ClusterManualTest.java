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

import java.util.Map;

import mungbean.query.Query;

import org.junit.runner.RunWith;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import static mungbean.CollectionUtil.*;

@RunWith(JDaveRunner.class)
public class ClusterManualTest extends Specification<Void> {
	public class WithCluster {
		public void writeOperationCanBeDone() throws InterruptedException {
			Mungbean mung = new Mungbean(new Server("localhost", 10000), new Server("localhost", 10001));
			DBCollection<Map<String, Object>> collection = mung.openDatabase("foo").openCollection("bar");
			while (true) {
				try {
					collection.save(map("foo", "bar"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(collection.query(new Query().field("foo").is("bar")));
				Thread.sleep(100);
			}
		}
	}
}
