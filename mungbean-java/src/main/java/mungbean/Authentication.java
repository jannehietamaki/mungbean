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

import static mungbean.Md5.md5;

import java.util.LinkedHashMap;

import mungbean.protocol.DBConnection;
import mungbean.protocol.message.CommandRequest;
import mungbean.protocol.message.CommandResponse;

public class Authentication {
	private final String user;
	private final String password;
	private final String database;

	public Authentication(String database, String user, String password) {
		this.user = user;
		this.password = password;
		this.database = database;
	}

	public String database() {
		return database;
	}

	public String user() {
		return user;
	}

	public String password() {
		return password;
	}

	public void authenticate(DBConnection connection) {
		final String nonce = (String) connection.execute(new CommandRequest(database(), "getnonce")).get("nonce");
		LinkedHashMap<String, Object> authenticationParameters = authenticationRequest(nonce);
		CommandResponse value = connection.execute(new CommandRequest(database(), authenticationParameters, CommandRequest.DEFAULT_CODERS));
		if (!value.get("ok").equals(1D)) {
			throw new MongoException("Authentication failed for database " + database(), value);
		}
	}

	LinkedHashMap<String, Object> authenticationRequest(final String nonce) {
		final String passwordHash = md5(user() + ":mongo:" + password());
		final String source = nonce + user() + passwordHash;
		LinkedHashMap<String, Object> authenticationParameters = new LinkedHashMap<String, Object>() {
			{
				put("authenticate", 1D);
				put("user", user());
				put("nonce", nonce);
				put("key", md5(source));
			}
		};
		return authenticationParameters;
	}
}
