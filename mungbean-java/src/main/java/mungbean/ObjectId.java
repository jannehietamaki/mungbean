package mungbean;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectId {
	// http://www.mongodb.org/display/DOCS/Object+IDs

	private final byte[] id;
	private final static AtomicInteger incrementCounter = new AtomicInteger(0);

	public ObjectId(byte[] id) {
		Assert.isTrue(id.length == 12, "Id must be 12 bytes");
		this.id = id;
	}

	public ObjectId() {
		this.id = new byte[12];
		long time = (TimeSource.instance().currentTimeMillis() - 1200000000000L) >> 4;
		int a = 0;
		for (; a < 4; a++) {
			this.id[a] = (byte) ((time >> 8 * a) & 0xFF);
		}
		byte[] hwAddress = hardwareAddress();
		for (; a < 7; a++) {
			this.id[a] = hwAddress[a - 1];
		}
		long pid = TimeSource.instance().startTime();
		for (; a < 9; a++) {
			this.id[a] = (byte) (pid & 0xff);
			pid = pid >> 1;
		}
		int counter = incrementCounter.incrementAndGet();
		for (; a < 12; a++) {
			this.id[a] = (byte) (counter & 0xff);
			counter = counter >> 1;
		}
	}

	private byte[] hardwareAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				byte[] addr = networkInterface.getHardwareAddress();
				if (addr != null) {
					return addr;
				}
			}
			throw new RuntimeException("Unable to retrieve hardware address!");
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] toByteArray() {
		return id;
	}

	@Override
	public int hashCode() {
		int ret = 0;
		for (byte b : id) {
			ret = (31 * ret) ^ b;
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass().equals(getClass())) {
			byte[] otherBytes = ((ObjectId) obj).id;
			if (id.length == otherBytes.length) {
				for (int a = 0; a < otherBytes.length; a++) {
					if (otherBytes[a] != id[a]) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("ObjectId[");
		for (int i = 0; i < id.length; i++) {
			if (i > 0 && i % 3 == 0) {
				result.append(":");
			}
			result.append(Integer.toString((id[i] & 0xff) + 0x100, 16).substring(1));
		}
		result.append("]");
		return result.toString();
	}
}
