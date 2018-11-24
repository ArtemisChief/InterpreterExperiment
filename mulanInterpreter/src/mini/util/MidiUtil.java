package mini.util;

public class MidiUtil {
    private byte[] byteMerger(byte[] b1,byte[] b2) {
        byte[] result = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, result, 0, b1.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        return result;
    }
}
