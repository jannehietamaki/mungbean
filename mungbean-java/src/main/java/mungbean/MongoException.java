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

import mungbean.protocol.message.CommandResponse;
import mungbean.protocol.message.MongoRequest;

public class MongoException extends RuntimeException {
    public MongoException(String message) {
        super(message);
    }

    private MongoException(String message, Throwable reason) {
        super(message, reason);
    }

    public MongoException(String message, Throwable reason, MongoRequest<?> request) {
        this(message + request.debugInfo(), reason);
    }

    public MongoException(String message, Map<String, Object> response) {
        this(message + ": " + response.toString());
    }

    public MongoException(String message, CommandResponse response) {
        this(message + ": " + response.toString());
    }

    public static MongoException forResponse(Map<String, Object> message) {
        if ("unauthorized".equals(message.get("err"))) {
            throw new MongoException("Not authorized", message);
        }
        throw new MongoException("Operation failed", message);
    }
}
