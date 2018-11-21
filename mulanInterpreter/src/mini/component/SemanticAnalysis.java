package mini.component;

import mini.entity.Node;
import mini.entity.Note;

import java.util.Map;

public class SemanticAnalysis {

    private Node AbstractSyntaxTree;

    private StringBuilder code;
    private StringBuilder errorInfo;
    private int count;
    private int scoreLength;

    public SemanticAnalysis() {

    }

    public String ConvertToArduino(Node abstractSyntaxTree) {
        AbstractSyntaxTree = abstractSyntaxTree;

        code = new StringBuilder();
        errorInfo = new StringBuilder();

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
        int noteCount = 0;
        int rhythmCount = 0;
        for (Node child : curNode.getChildren()) {
            switch (child.getType()) {
                case "score":
                    count++;
                    scoreLength=0;
                    code.append("const int length" + count + ";\n\n" +
                            "double speedFactor" + count + ";\n\n" +
                            "double tonalityFactor" + count + ";\n\n");
                    DFS(child);
                    break;

                case "execution":
//                    code.append("defineTaskLoop(Task1){}");
                    DFS(child);
                    break;

                case "statement":
                    code.append("int *" + child.getChild(0).getContent() + "=new int[len" + count + "]\n{};//Notes\n\n");
                    code.append("int *" + child.getChild(0).getContent() + "Duration=new int[len" + count + "]\n{};//Duration\n\n");
                    break;

                case "speed":
                    speedFactor = 60 / Double.parseDouble(child.getChild(0).getContent()) / 4 * 1000;
                    code.insert(code.indexOf("speedFactor" + count) + ("speedFactor" + count).length(), "=" + speedFactor);
                    break;

                case "tonality":
                    for (Node tonality : child.getChildren()) {
                        double halfTone = 1;
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
                    code.insert(code.indexOf("length"+count)+("length"+count).length(),"="+scoreLength);
                    code.delete(code.indexOf("};//Notes") - 3, code.indexOf("};//Notes"));
                    code.delete(code.indexOf("};//Duration") - 3, code.indexOf("};//Duration"));
                    code.delete(code.indexOf("//Notes"), code.indexOf("//Notes") + 7);
                    code.delete(code.indexOf("//Duration"), code.indexOf("//Duration") + 10);
                    break;

                case "melody":
                    double pitchFactor = 1;
                    double halfTone = 1;
                    Integer pitch;

                    for (Node tone : child.getChildren()) {
                        switch (tone.getContent()) {
                            case "(":
                                pitchFactor *= 0.5;
                                break;
                            case ")":
                                pitchFactor *= 2;
                                break;
                            case "[":
                                pitchFactor *= 2;
                                break;
                            case "]":
                                pitchFactor *= 0.5;
                                break;
                            case "#":
                                halfTone *= 1.059463;
                                break;
                            case "b":
                                halfTone *= 0.943874;
                                break;
                            case "0":
                                noteCount++;
                                pitch = 0;
                                code.insert(code.indexOf("};//Notes"), pitch + ", ");
                                break;
                            case "1":
                                noteCount++;
                                pitch = (int) ((Double.parseDouble(Note.Pitch.C) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"), pitch + ", ");
                                halfTone = 1;
                                break;
                            case "2":
                                noteCount++;
                                pitch = (int) ((Double.parseDouble(Note.Pitch.D) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"), pitch + ", ");
                                halfTone = 1;
                                break;
                            case "3":
                                noteCount++;
                                pitch = (int) ((Double.parseDouble(Note.Pitch.E) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"), pitch + ", ");
                                halfTone = 1;
                                break;
                            case "4":
                                noteCount++;
                                pitch = (int) ((Double.parseDouble(Note.Pitch.F) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"), pitch + ", ");
                                halfTone = 1;
                                break;
                            case "5":
                                noteCount++;
                                pitch = (int) ((Double.parseDouble(Note.Pitch.G) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"), pitch + ", ");
                                halfTone = 1;
                                break;
                            case "6":
                                noteCount++;
                                pitch = (int) ((Double.parseDouble(Note.Pitch.A) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"), pitch + ", ");
                                halfTone = 1;
                                break;
                            case "7":
                                noteCount++;
                                pitch = (int) ((Double.parseDouble(Note.Pitch.B) * pitchFactor * halfTone));
                                code.insert(code.indexOf("};//Notes"), pitch + ", ");
                                halfTone = 1;
                                break;
                        }
                    }
                    code.insert(code.indexOf("};//Notes"), "\n");
                    break;

                case "rhythm":
                    Integer legato = 1;

                    for (Node rhythm : child.getChildren()) {
                        switch (rhythm.getContent()) {
                            case "{":
                                legato = -1;
                                break;
                            case "}":
                                //todo 连音处理
                                legato = 1;
                                break;
                            case "1":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), 16 * legato + ", ");
                                break;
                            case "1*":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), 24 * legato + ", ");
                                break;
                            case "2":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), 8 * legato + ", ");
                                break;
                            case "2*":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), 12 * legato + ", ");
                                break;
                            case "4":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), 4 * legato + ", ");
                                break;
                            case "4*":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), 6 * legato + ", ");
                                break;
                            case "8":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), 2 * legato + ", ");
                                break;
                            case "8*":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), 3 * legato + ", ");
                                break;
                            case "g":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), 1 * legato + ", ");
                                break;
                            case "g*":
                                rhythmCount++;
                                code.insert(code.indexOf("};//Duration"), "Error: 不支持32分音符");
                                errorInfo.append("Error: 不支持32分音符\n");
                                break;
                        }
                    }
                    code.insert(code.indexOf("};//Duration"), "\n");

                    if(noteCount!=rhythmCount) {
                        code.insert(code.indexOf("};//Duration"),"Error: 上一行中音符与时值数量不相同\n");
                        errorInfo.append("Error: 该句音符与时值数量不相同\n");
                    }
                    scoreLength+=noteCount;
                    break;

                case "playlist":
                    break;
            }
        }
    }
}
