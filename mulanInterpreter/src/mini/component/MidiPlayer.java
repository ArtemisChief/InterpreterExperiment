package mini.component;

import javax.sound.midi.*;
import java.io.File;

public class MidiPlayer {
    public Synthesizer synthesizer;
    public Sequencer sequencer;
    private Sequence sequence;
    private Soundbank soundbank;
    private long microsecondPosition;

    public MidiPlayer() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSoundBank(File soundFontFile) {
        try {
            synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
            soundbank = MidiSystem.getSoundbank(soundFontFile);
            synthesizer.loadAllInstruments(soundbank);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMidiFile(File midiFile) {
        try {
            sequence = MidiSystem.getSequence(midiFile);
            sequencer.setSequence(sequence);
            microsecondPosition = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        sequencer.setMicrosecondPosition(microsecondPosition);
        sequencer.start();
    }

    public void pause() {
        microsecondPosition = sequencer.getMicrosecondPosition();
        sequencer.stop();
    }

    public void stop() {
        microsecondPosition = 0;
        sequencer.stop();
    }

    public Sequencer getSequencer(){
        return sequencer;
    }
}
