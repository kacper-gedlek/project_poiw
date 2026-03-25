package poiw.project.CAS;

public class UID {
	public static String b2hStr(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			System.err.println("Błędne UID");
			return "";
		}
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
	public static int b2int(byte[] bytes) {
		if(bytes.length != 4) {
			System.err.println("Błędne UID");
			return 0;
		}
		return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | ((bytes[3] & 0xFF));
	}
}
