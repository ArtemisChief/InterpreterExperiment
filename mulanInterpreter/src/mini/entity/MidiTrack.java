package mini.entity;

public class MidiTrack {

    private byte[] MidiTrackData;

    private final static byte[] TRACK_HEADER_A = new byte[]{0x4D, 0x54, 0x72, 0x6B};

    private final static byte[] END_TRACK = new byte[]{0x00, (byte) 0xFF, 0x2F, 0x00};

    private int ByteCount;

    private int channel;

    public MidiTrack() {

    }

    public void setBpm(int bpm) {

    }

    public void insertNote(int channel, int pitch, int duration) {

    }

    public void setEnd() {

    }

}