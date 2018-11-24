package mini.component;

import mini.entity.MidiFile;
import mini.entity.MidiTrack;
import mini.entity.Node;
import mini.entity.Paragraph;

import java.util.*;

public class SemanticAnalysisMidi {

    private Node AbstractSyntaxTree;

    private List<Integer> errorLines;
    private StringBuilder errorInfo;
    private int toneOffset;
    private int haftToneOffset;

    private List<Paragraph> paragraphs;

    private MidiFile midiFile;
    private List<MidiTrack> midiTracks;
    private int channel;

    public String ConvertToMidi(Node abstractSyntaxTree) {
        AbstractSyntaxTree = abstractSyntaxTree;

        errorLines = new ArrayList<>();
        errorInfo = new StringBuilder();

        toneOffset = 0;
        haftToneOffset = 0;
        channel=0;

        paragraphs = new ArrayList<>();
        midiTracks=new ArrayList<>();

        DFS_Midi(AbstractSyntaxTree, null);

        midiFile = new MidiFile(midiTracks);
        midiFile.construct();

        if (getIsError())
            return null;

        return midiFile.toString();
    }

    private void DFS_Midi(Node curNode, Paragraph para) {
        Paragraph paragraph = para;
        List<Integer> noteList = null;
        List<Integer> durationList = null;

        for (Node child : curNode.getChildren()) {
            switch (child.getType()) {
                case "score":
                    DFS_Midi(child, paragraph);
                    break;

                case "execution":
                    DFS_Midi(child, paragraph);
                    break;

                case "statement":
                    for (Paragraph p : paragraphs) {
                        if (p.getParagraphName() == child.getChild(0).getContent()) {
                            errorInfo.append("Line: " + child.getChild(0).getCount() + "\t重复声明的段落名" + child.getChild(0).getContent() + "\n");
                            errorLines.add(child.getChild(0).getCount());
                        }
                    }
                    paragraph = new Paragraph();
                    paragraph.setParagraphName(child.getChild(0).getContent());
                    paragraphs.add(paragraph);
                    break;

                case "speed":
                    paragraph.setSpeed(Float.parseFloat(child.getChild(0).getContent()));
                    break;

                case "tonality":
                    toneOffset=0;
                    for (Node tonality : child.getChildren()) {
                        switch (tonality.getContent()) {
                            case "#":
                                toneOffset += 1;
                                break;
                            case "b":
                                toneOffset -= 1;
                                break;
                            case "C":
                                break;
                            case "D":
                                toneOffset += 2;
                                break;
                            case "E":
                                toneOffset += 4;
                                break;
                            case "F":
                                toneOffset += 5;
                                break;
                            case "G":
                                toneOffset += 7;
                                break;
                            case "A":
                                toneOffset -= 3;
                                break;
                            case "B":
                                toneOffset -= 1;
                                break;
                        }
                    }
                    break;

                case "sentence":
                    DFS_Midi(child, paragraph);
                    break;

                case "end paragraph":
                    break;

                case "melody":
                    noteList = paragraph.getNoteList();
                    for (Node tone : child.getChildren()) {
                        switch (tone.getContent()) {
                            case "(":
                                toneOffset -= 12;
                                break;
                            case ")":
                                toneOffset += 12;
                                break;
                            case "[":
                                toneOffset += 12;
                                break;
                            case "]":
                                toneOffset -= 12;
                                break;
                            case "#":
                                haftToneOffset = 1;
                                break;
                            case "b":
                                haftToneOffset = -1;
                                break;
                            case "0":
                                noteList.add(0);
                                break;
                            case "1":
                                noteList.add(60 + haftToneOffset + toneOffset);
                                haftToneOffset = 0;
                                break;
                            case "2":
                                noteList.add(62 + haftToneOffset + toneOffset);
                                haftToneOffset = 0;
                                break;
                            case "3":
                                noteList.add(64 + haftToneOffset + toneOffset);
                                haftToneOffset = 0;
                                break;
                            case "4":
                                noteList.add(65 + haftToneOffset + toneOffset);
                                haftToneOffset = 0;
                                break;
                            case "5":
                                noteList.add(67 + haftToneOffset + toneOffset);
                                haftToneOffset = 0;
                                break;
                            case "6":
                                noteList.add(69 + haftToneOffset + toneOffset);
                                haftToneOffset = 0;
                                break;
                            case "7":
                                noteList.add(71 + haftToneOffset + toneOffset);
                                haftToneOffset = 0;
                                break;
                        }
                    }
                    break;

                case "rhythm":
                    durationList = paragraph.getDurationList();
                    Integer line = child.getChild(0).getCount();
                    for (Node rhythm : child.getChildren()) {
                        switch (rhythm.getContent()) {
                            case "{":
                                break;
                            case "}":
                                break;
                            case "1":
                                durationList.add(480);
                                break;
                            case "1*":
                                durationList.add(720);
                                break;
                            case "2":
                                durationList.add(240);
                                break;
                            case "2*":
                                durationList.add(360);
                                break;
                            case "4":
                                durationList.add(120);
                                break;
                            case "4*":
                                durationList.add(180);
                                break;
                            case "8":
                                durationList.add(60);
                                break;
                            case "8*":
                                durationList.add(90);
                                break;
                            case "g":
                                durationList.add(30);
                                break;
                            case "g*":
                                durationList.add(45);
                                break;
                        }
                    }

                    if (noteList.size() != durationList.size()) {
                        errorInfo.append("Line: " + line + "\t该句音符与时值数量不相同\n");
                        errorLines.add(line);
                    }

                    break;

                case "playlist":
                    String paraName = "";
                    boolean andOp = false;
                    boolean hasStatment = false;
                    int totalDuration = 0;
                    for (int j = 0; j < child.getChildren().size(); j++) {
                        switch (child.getChild(j).getContent()) {
                            case "&":
                                andOp = true;
                                break;

                            case ",":
                                Paragraph tempPara = null;
                                int tempDuration = 0;
                                for (Paragraph p : paragraphs) {
                                    if (p.getParagraphName().equals(child.getChild(j - 1).getContent())) {
                                        hasStatment = true;
                                        tempPara = p;
                                    }
                                }
                                for (int duration : tempPara.getDurationList()) {
                                    tempDuration += duration;
                                }

                                totalDuration += tempDuration;

                                if (!andOp) {
                                    Paragraph lowParagraph = new Paragraph();
                                    lowParagraph.setSpeed(tempPara.getSpeed());
                                    lowParagraph.setNoteList(tempPara.getNoteList());
                                    lowParagraph.setDurationList(tempPara.getDurationList());

                                    for (int i = 0; i < lowParagraph.getNoteList().size(); i++) {
                                        lowParagraph.getNoteList().set(i, lowParagraph.getNoteList().get(i) - 12);
                                    }

                                    constuctMidiTrack(lowParagraph, totalDuration - tempDuration);
                                }

                                andOp = false;
                                break;

                            default:
                                paraName = child.getChild(j).getContent();
                                Paragraph paraToConstruct = null;

                                for (Paragraph p : paragraphs) {
                                    if (p.getParagraphName().equals(paraName)) {
                                        hasStatment = true;
                                        paraToConstruct = p;
                                    }
                                }

                                if (!hasStatment) {
                                    errorInfo.append("Line: " + child.getChild(j).getCount() + "\t未声明的段落名" + paraName + "\n");
                                    errorLines.add(child.getChild(j).getCount());
                                }

                                if (paraToConstruct != null)
                                    constuctMidiTrack(paraToConstruct, totalDuration);

                                break;
                        }
                    }
                    if (!andOp) {
                        Paragraph tempPara = paragraphs.get(paragraphs.size() - 1);
                        int tempDuration = 0;

                        if (paragraphs.size() - 1 > 0)
                            for (int du : paragraphs.get(paragraphs.size() - 1).getDurationList())
                                tempDuration += du;


                        Paragraph lowParagraph = new Paragraph();
                        lowParagraph.setSpeed(tempPara.getSpeed());
                        lowParagraph.setNoteList(tempPara.getNoteList());
                        lowParagraph.setDurationList(tempPara.getDurationList());

                        for (int i = 0; i < lowParagraph.getNoteList().size(); i++) {
                            lowParagraph.getNoteList().set(i, lowParagraph.getNoteList().get(i) - 12);
                        }

                        constuctMidiTrack(lowParagraph, totalDuration - tempDuration);
                    }
                    break;
            }
        }
    }

    private void constuctMidiTrack(Paragraph paragraph, int duration) {
        MidiTrack midiTrack = new MidiTrack();
        midiTrack.setBpm(paragraph.getSpeed());

        if (duration != 0)
            midiTrack.setDuration(duration);

        List<Integer> noteList = paragraph.getNoteList();
        List<Integer> durationList = paragraph.getDurationList();

        for (int i = 0; i < noteList.size(); i++) {
            midiTrack.insertNote(channel, noteList.get(i), durationList.get(i));
        }

        midiTrack.setEnd();

        midiTracks.add(midiTrack);

        channel++;

        if (channel > 15)
            channel = 0;
    }

    public boolean getIsError() {
        return !errorLines.isEmpty();
    }

    public List<Integer> getErrorLines() {
        return errorLines;
    }

    public String getErrors() {
        return errorInfo.toString();
    }

    public MidiFile getMidiFile(){
        return midiFile;
    }
}
