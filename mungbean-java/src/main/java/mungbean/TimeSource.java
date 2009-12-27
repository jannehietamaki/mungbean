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

public class TimeSource {
	private static TimeSource instance = new TimeSource(System
			.currentTimeMillis());
	private final long startTime;

	private TimeSource(long startTime) {
		this.startTime = startTime;
	}

	public static TimeSource instance() {
		return instance;
	}

	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static void setCurrentTimeSource(TimeSource source) {
		instance = source;
	}

	public long startTime() {
		return startTime;
	}
}
