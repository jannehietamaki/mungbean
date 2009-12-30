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

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class Md5 {
	private Md5() {

	}

	public static String md5(String source) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(source.getBytes(Charset.forName("UTF-8")));
			byte messageDigest[] = algorithm.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
				hexString.append(Integer.toHexString(0xFF & b));
			}
			return hexString.toString().toUpperCase();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
