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