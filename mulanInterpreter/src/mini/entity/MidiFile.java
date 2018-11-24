package mini.entity;

import mini.util.MidiUtil;

import java.io.*;
import java.util.List;

public class MidiFile {

    private byte[] MidiFileData;

    private final static byte[] MIDI_HEADER = new byte[]{0x4D, 0x54, 0x68, 0x64, 0x00, 0x00, 0x00, 0x06, 0x00, 0x01};

    private List<MidiTrack> Tracks;

    public MidiFile(List<MidiTrack> tracks) {
        MidiFileData = MIDI_HEADER;
        Tracks = tracks;
    }

    public void construct() {
        int trackCount = Tracks.size();
        MidiFileData = MidiUtil.mergeByte(MidiFileData, MidiUtil.intToBytes(trackCount, 2));
        MidiFileData = MidiUtil.mergeByte(MidiFileData, new byte[]{0x00, 0x78});
        for (MidiTrack track : Tracks) {
            MidiFileData = MidiUtil.mergeByte(MidiFileData, track.getMidiTrackData());
        }
    }

    public void writeToFile(File midiFile) {
        if (midiFile.exists())
            midiFile.delete();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(midiFile);
            fileOutputStream.write(MidiFileData);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(MidiUtil.bytesToHex(MidiFileData));
        for (int i = 48; i < stringBuilder.length(); i += 49) {
            if (i < stringBuilder.length())
                stringBuilder.replace(i, i, "\n");
        }
        return stringBuilder.toString();
    }

}