package mini.entity;

import java.util.ArrayList;
import java.util.List;

public class Paragraph {

    private String ParagraphName;
    private Float Speed;
    private List<Integer> NoteList;
    private List<Integer> DurationList;

    public Paragraph() {
        ParagraphName = "";
        Speed = 0.0F;
        NoteList = new ArrayList<>();
        DurationList = new ArrayList<>();
    }

    public String getParagraphName() {
        return ParagraphName;
    }

    public void setParagraphName(String paragraphName) {
        ParagraphName = paragraphName;
    }

    public Float getSpeed() {
        return Speed;
    }

    public void setSpeed(Float speed) {
        Speed = speed;
    }

    public List<Integer> getNoteList() {
        return NoteList;
    }

    public void setNoteList(List<Integer> noteList) {
        NoteList = noteList;
    }

    public List<Integer> getDurationList() {
        return DurationList;
    }

    public void setDurationList(List<Integer> durationList) {
        DurationList = durationList;
    }
}