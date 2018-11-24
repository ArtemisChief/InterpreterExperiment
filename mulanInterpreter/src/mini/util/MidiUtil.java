package mini.util;

public class MidiUtil {

    public static byte[] mergeByte(byte[] b1, byte[] b2) {
        byte[] result = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, result, 0, b1.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        return result;
    }

    public static int bpmToMpt(float bpm) {
        return (int) (60 / bpm * 1000000);
    }

    public static byte[] intToBytes(int val, int byteCount) {
        byte[] buffer = new byte[byteCount];

        int[] ints = new int[byteCount];

        for (int i = 0; i < byteCount; i++) {
            ints[i] = val & 0xFF;
            buffer[byteCount - i - 1] = (byte) ints[i];

            val = val >> 8;

            if (val == 0)
                break;
        }
        return buffer;
    }

    private static final String HEX = "0123456789ABCDEF";

    public static String byteToHex(byte b) {
        int high = (b & 0xF0) >> 4;
        int low = (b & 0x0F);

        return "" + HEX.charAt(high) + HEX.charAt(low);
    }

    public static String bytesToHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(byteToHex(b[i])).append(" ");
        }
        return sb.toString();
    }
}
