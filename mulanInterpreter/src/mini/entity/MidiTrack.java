package mini.entity;

import mini.util.MidiUtil;

public class MidiTrack {

    private byte[] MidiTrackData;

    private byte[] MidiTrackContentData;

    private final static byte[] TRACK_HEADER = new byte[]{0x4D, 0x54, 0x72, 0x6B};

    private final static byte[] END_TRACK = new byte[]{0x00, (byte) 0xFF, 0x2F, 0x00};

    private int ByteCount;

    public MidiTrack() {
        MidiTrackData = TRACK_HEADER;
        MidiTrackContentData = new byte[0];
        ByteCount = 0;
    }

    public void setBpm(float bpm) {
        int microsecondPreNote = MidiUtil.bpmToMpt(bpm);
        byte[] tempo = new byte[]{0x00, (byte) 0xFF, 0x51, 0x03};
        tempo = MidiUtil.mergeByte(tempo, MidiUtil.intToBytes(microsecondPreNote, 3));

        ByteCount += 7;

        MidiTrackContentData = MidiUtil.mergeByte(MidiTrackContentData, tempo);
    }

    public void setDuration(Integer duration) {
        if(duration!=0) {
            byte[] note = new byte[]{duration.byteValue(), (byte) 0x90, 0x3C, 0x00};
            ByteCount += 4;
            MidiTrackContentData = MidiUtil.mergeByte(MidiTrackContentData, note);
        }
    }

    public void insertNote(Integer channel, Integer pitch, Integer ticks) {
        byte[] noteOn;
        byte[] noteOff;

        if (pitch != 0) {
            noteOn = new byte[]{0x00, ((Integer) (144 + channel)).byteValue(), pitch.byteValue(), 0x64};

            ByteCount += 4;

            MidiTrackContentData = MidiUtil.mergeByte(MidiTrackContentData, noteOn);

            noteOff = MidiUtil.buildBytes(ticks);

            ByteCount += noteOff.length;

            MidiTrackContentData = MidiUtil.mergeByte(MidiTrackContentData, noteOff);

            noteOff = new byte[]{((Integer) (128 + channel)).byteValue(),pitch.byteValue(), 0x00};

            ByteCount += 3;

            MidiTrackContentData = MidiUtil.mergeByte(MidiTrackContentData, noteOff);
        } else {
            noteOn = new byte[]{0x00, ((Integer) (144 + channel)).byteValue(), 0X00, 0x00};

            ByteCount += 4;

            MidiTrackContentData = MidiUtil.mergeByte(MidiTrackContentData, noteOn);

            noteOff = MidiUtil.buildBytes(ticks);

            ByteCount += noteOff.length;

            MidiTrackContentData = MidiUtil.mergeByte(MidiTrackContentData, noteOff);

            noteOff = new byte[]{((Integer) (128 + channel)).byteValue(),0X00, 0x00};

            ByteCount += 3;

            MidiTrackContentData = MidiUtil.mergeByte(MidiTrackContentData, noteOff);
        }
    }

    public void setEnd() {
        ByteCount += 4;
        MidiTrackData = MidiUtil.mergeByte(MidiTrackData, MidiUtil.intToBytes(ByteCount, 4));
        MidiTrackData = MidiUtil.mergeByte(MidiTrackData, MidiTrackContentData);
        MidiTrackData = MidiUtil.mergeByte(MidiTrackData, END_TRACK);
    }

    public byte[] getMidiTrackData() {
        return MidiTrackData;
    }
}