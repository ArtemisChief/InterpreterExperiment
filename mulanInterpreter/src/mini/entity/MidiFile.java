package mini.entity;

import java.io.File;
import java.util.List;

public class MidiFile {

    private byte[] MidiFileData;

    private final static byte[] MIDI_HEADER_A = new byte[]{0x4D, 0x54, 0x68, 0x64, 0x00, 0x00, 0x00, 0x06};

    private List<MidiTrack> Tracks;

    public MidiFile(List<MidiTrack> tracks) {
        Tracks = tracks;
    }

    public void writeToFile(File midiFile) {

    }

}