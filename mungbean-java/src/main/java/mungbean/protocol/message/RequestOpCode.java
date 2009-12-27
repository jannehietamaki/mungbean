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
package mungbean.protocol.message;

public enum RequestOpCode {
	OP_REPLY(1), OP_MSG(1000), OP_UPDATE(2001), OP_INSERT(2002), OP_GET_BY_OID(2003), OP_QUERY(2004), OP_GET_MORE(2005), OP_DELETE(2006), OP_KILL_CURSORS(2007);
	private final int opCode;

	RequestOpCode(int opCode) {
		this.opCode = opCode;
	}

	public int opCode() {
		return opCode;
	}

	public static RequestOpCode forId(int id) {
		for (RequestOpCode opCode : values()) {
			if (opCode.opCode() == id) {
				return opCode;
			}
		}
		throw new IllegalArgumentException("Invalid opcode " + id);
	}

}