package mini.component;

import mini.entity.Node;
import mini.entity.Note;

import java.util.Map;

public class SemanticAnalysis {

    private Node AbstractSyntaxTree;

    private StringBuilder code;
    private String errorInfo;
    private int count;

    public SemanticAnalysis() {

    }

    public String ConvertToArduino(Node abstractSyntaxTree) {
        AbstractSyntaxTree = abstractSyntaxTree;

        code = new StringBuilder();
        errorInfo = "";

        count = 0;

        //TODO 分析语法树转换成Arduino代码

        code.append("#include <Tone.h>\n" +
                "#include <SCoop.h>\n\n" +
                "int tonePin1=2;\n" +
                "int tonePin2=3;\n\n" +
                "Tone tone1;\n" +
                "Tone tone2;\n\n");

        DFS(AbstractSyntaxTree);

        /**
         * 关于速度
         * 如果 speed=120，即每分钟120拍，即每拍持续0.5秒
         * 默认四分音符为一拍，而四分音符时值为4，
         * 则可以计算出Arduino中的时值因子为
         * speedFactor=60/120/4*1000=125
         * 其中只有120是变量，60为每分钟60秒，4为四分音符的时值，1000为秒转换成毫秒
         */

        /**
         * 查表得到频率与时值，如
         * String frequency = Note.Pitch.C;     (值为523)
         * String duration = Note.Duration._g;  (值为1)
         */

        return code.toString();
    }

    private void DFS(Node curNode) {
        double speedFactor;
        int noteCount=0;
        int rhythmCount=0;
        for (Node child : curNode.getChildren()) {
            switch (child.getType()) {
                case "score":
                    count++;
                    code.append("const int len" + count + ";\n\n" +
                            "double speedFactor" + count + ";\n\n" +
                            "double tonalityFactor" + count + ";\n\n");
                    DFS(child);
                    break;

                case "execution":
                    DFS(child);
                    break;

                case "statement":
                    code.append("int *" + child.getChild(0).getContent() + "=new int[len" + count + "]{};//Notes\n\n");
                    code.append("int *" + child.getChild(0).getContent() + "Duration=new int[len" + count + "]{};//Duration\n\n");
                    break;

                case "speed":
                    speedFactor = 60 / Double.parseDouble(child.getChild(0).getContent()) / 4 * 1000;
                    code.insert(code.indexOf("speedFactor" + count) + ("speedFactor" + count).length(), "=" + speedFactor);
                    break;

                case "tonality":
                    for(Node tonality:child.getChildren()) {
                        double halfTone=1;
                        switch (tonality.getContent()) {
                            case "#":
                                halfTone = 1.059463;
                                break;
                            case "b":
                                halfTone = 0.943874;
                                break;
                            case "C":
                                code.insert(code.indexOf("tonalityFactor" + count) + ("tonalityFactor" + count).length(), "=" + Double.parseDouble(Note.Tonality.C) * halfTone);
                                break;
                            case "D":
                                code.insert(code.indexOf("tonalityFactor" + count) + ("tonalityFactor" + count).length(), "=" + Double.parseDouble(Note.Tonality.D) * halfTone);
                                break;
                            case "E":
                                code.insert(code.indexOf("tonalityFactor" + count) + ("tonalityFactor" + count).length(), "=" + Double.parseDouble(Note.Tonality.E) * halfTone);
                                break;
                            case "F":
                                code.insert(code.indexOf("tonalityFactor" + count) + ("tonalityFactor" + count).length(), "=" + Double.parseDouble(Note.Tonality.F) * halfTone);
                                break;
                            case "G":
                                code.insert(code.indexOf("tonalityFactor" + count) + ("tonalityFactor" + count).length(), "=" + Double.parseDouble(Note.Tonality.G) * halfTone);
                                break;
                            case "A":
                                code.insert(code.indexOf("tonalityFactor" + count) + ("tonalityFactor" + count).length(), "=" + Double.parseDouble(Note.Tonality.A) * halfTone);
                                break;
                            case "B":
                                code.insert(code.indexOf("tonalityFactor" + count) + ("tonalityFactor" + count).length(), "=" + Double.parseDouble(Note.Tonality.B) * halfTone);
                                break;
                        }
                    }
                    break;

                case "sentence":
                    DFS(child);
                    break;

                case "end paragraph":
                    code.delete(code.indexOf("};//Notes")-2,code.indexOf("};//Notes"));
                    code.delete(code.indexOf("};//Duration")-2,code.indexOf("};//Duration"));
                    code.delete(code.indexOf("//Notes"),code.indexOf("//Notes")+7);
                    code.delete(code.indexOf("//Duration"),code.indexOf("//Duration")+10);
                    break;

                case "melody":
                    double pitchFactor=1;
                    double halfTone=1;
                    Integer pitch;

                    for(Node tone: child.getChildren()){
                        noteCount++;
                        switch (tone.getContent()) {
                            case "(":
                                pitchFactor = 0.5;
                                break;
                            case ")":
                                pitchFactor = 1;
                                break;
                            case "[":
                                pitchFactor = 2;
                                break;
                            case "]":
                                pitchFactor = 1;
                                break;
                            case "#":
                                halfTone=1.059463;
                                break;
                            case "b":
                                halfTone=0.943874;
                                break;
                            case "0":
                                pitch= 0;
                                code.insert(code.indexOf("};//Notes"),pitch+", ");
                                break;
                            case "1":
                                pitch = (int)((Double.parseDouble(Note.Pitch.C) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"),pitch+", ");
                                halfTone = 1;
                                break;
                            case "2":
                                pitch = (int)((Double.parseDouble(Note.Pitch.D) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"),pitch+", ");
                                halfTone=1;
                                break;
                            case "3":
                                pitch = (int)((Double.parseDouble(Note.Pitch.E) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"),pitch+", ");
                                halfTone=1;
                                break;
                            case "4":
                                pitch = (int)((Double.parseDouble(Note.Pitch.F) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"),pitch+", ");
                                halfTone=1;
                                break;
                            case "5":
                                pitch = (int)((Double.parseDouble(Note.Pitch.G) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"),pitch+", ");
                                halfTone=1;
                                break;
                            case "6":
                                pitch = (int)((Double.parseDouble(Note.Pitch.A) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"),pitch+", ");
                                halfTone=1;
                                break;
                            case "7":
                                pitch = (int)((Double.parseDouble(Note.Pitch.B) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"),pitch+", ");
                                halfTone=1;
                                break;
                        }
                    }
                    code.insert(code.indexOf("};//Notes"),"\n");
                    break;

                case "rhythm":
                    break;

                case "playlist":
                    break;
            }
        }
    }
}
