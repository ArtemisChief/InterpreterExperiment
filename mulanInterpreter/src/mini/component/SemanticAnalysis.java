package mini.component;

import mini.entity.Node;
import mini.entity.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SemanticAnalysis {

    private Node AbstractSyntaxTree;

    private boolean isError;
    private StringBuilder code;
    private StringBuilder errorInfo;
    private ArrayList<String> paragraphs;
    private int count;
    private int scoreLength;

    public SemanticAnalysis() {

    }

    public String ConvertToArduino(Node abstractSyntaxTree) {
        AbstractSyntaxTree = abstractSyntaxTree;

        isError=false;

        code = new StringBuilder();
        errorInfo = new StringBuilder();

        paragraphs = new ArrayList<>();

        count = 0;

        //TODO 分析语法树转换成Arduino代码

        code.append("#include <Tone.h>\n" +
                "#include <SCoop.h>\n\n" +
                "int tonePin1=2;\n" +
                "int tonePin2=3;\n\n" +
                "Tone tone1;\n" +
                "Tone tone2;\n\n\n");

        DFS(AbstractSyntaxTree);

        if(isError)
            return errorInfo.toString();

        return code.toString();
    }

    private void DFS(Node curNode) {
        double speedFactor;
        int noteCount = 0;
        int rhythmCount = 0;
        String notes="";
        String rhythms="";
        for (Node child : curNode.getChildren()) {
            switch (child.getType()) {
                case "score":
                    count++;
                    scoreLength = 0;
                    code.append("const int length" + count + ";\n\n" +
                            "double speedFactor" + count + ";\n\n" +
                            "double tonalityFactor" + count + ";\n\n");
                    DFS(child);
                    break;

                case "execution":
                    code.append("void play(int *paragragh, int *duration, int paragraphLength, Tone tonePlayer, double tonalityFactor, double speedFactor){\n" +
                            "  for(int i=0;i<paragraphLength;i++){\n" +
                            "    if(paragragh[i]!=0)\n" +
                            "      tonePlayer.play(paragragh[i] * tonalityFactor);\n" +
                            "    if(duration[i]>0){\n" +
                            "      delay(duration[i] * speedFactor);\n" +
                            "      tonePlayer.stop();\n" +
                            "      delay(10);\n" +
                            "    }else{\n" +
                            "      delay(-duration[i] * speedFactor);\n" +
                            "      delay(10);\n" +
                            "      tonePlayer.stop();\n" +
                            "    }\n" +
                            "  }\n" +
                            "}\n\n" +
                            "defineTaskLoop(Task1){};//task1\n\n" +
                            "defineTaskLoop(Task2){};//task2\n\n" + "" +
                            "void setup() {\n" +
                            "  tone1.begin(tonePin1);\n" +
                            "  tone2.begin(tonePin2);\n" +
                            "  mySCoop.start();\n" +
                            "}\n\n" +
                            "void loop() {\n" +
                            "  yield();\n" +
                            "}");
                    DFS(child);
                    break;

                case "statement":
                    paragraphs.add(child.getChild(0).getContent());
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
                    code.insert(code.indexOf("length" + count) + ("length" + count).length(), "=" + scoreLength);
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
                        notes+=tone.getContent();
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
                        rhythms+=rhythm.getContent();
                        switch (rhythm.getContent()) {
                            case "{":
                                legato = -1;
                                break;
                            case "}":
                                code.deleteCharAt(code.lastIndexOf("-"));
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
                                code.insert(code.indexOf("};//Duration"), "Error: 不支持32分音符，即g*");
                                errorInfo.append("Error: 不支持32分音符，即g*\n");
                                isError=true;
                                break;
                        }
                    }
                    code.insert(code.indexOf("};//Duration"), "\n");

                    if (noteCount != rhythmCount) {
                        code.insert(code.indexOf("};//Duration"), "Error: 该句音符与时值数量不相同:\"+notes+\" \"+rhythms+\"\\n\"");
                        errorInfo.append("Error: 该句音符与时值数量不相同:"+notes+" "+rhythms+"\n");
                        isError=true;
                    }
                    scoreLength += noteCount;
                    break;

                case "playlist":
                    String paraName = "";
                    int tonePlayed = 0;         //要使用的蜂鸣器编号
                    int playCount = 0;          //一个段落名对应一个Count值
                    Map<String, Integer> countMap = new HashMap<>();
                    boolean andOp = false;
                    for (Node playList : child.getChildren()) {
                        switch (playList.getContent()) {
                            case "&":
                                if (tonePlayed > 1) {
                                    code.append("Error: 不支持两个以上蜂鸣器同时播放，请减少play中同时播放的数量\n");
                                    errorInfo.append("Error: 不支持两个以上蜂鸣器同时播放，请减少play中同时播放的数量\n");
                                    isError=true;
                                }
                                andOp = true;
                                break;
                            case ",":
                                if (tonePlayed == 1) {
                                    code.insert(code.indexOf("};//task2"), "\n  play(" + paraName + ", " + paraName + "Duration, length" + countMap.get(paraName) + ",tone2, tonalityFactor" + countMap.get(paraName) + "*0.5, speedFactor" + countMap.get(paraName) + ");\n");
                                }
                                tonePlayed = 0;
                                andOp = false;
                                break;
                            default:
                                paraName = playList.getContent();
                                if (!paragraphs.contains(paraName)) {
                                    code.insert(code.indexOf("};//task1"), "Error: 未声明的段落名" + paraName);
                                    errorInfo.append("Error: 未声明的段落名" + paraName + "\n");
                                    isError=true;
                                }
                                tonePlayed++;
                                if (!countMap.containsKey(paraName)) {
                                    playCount++;
                                    countMap.put(paraName, playCount);
                                }
                                if (!andOp) {
                                    code.insert(code.indexOf("};//task1"), "\n  play(" + paraName + ", " + paraName + "Duration, length" + countMap.get(paraName) + ",tone1, tonalityFactor" + countMap.get(paraName) + ", speedFactor" + countMap.get(paraName) + ");\n");
                                } else {
                                    code.insert(code.indexOf("};//task2"), "\n  play(" + paraName + ", " + paraName + "Duration, length" + countMap.get(paraName) + ",tone2, tonalityFactor" + countMap.get(paraName) + ", speedFactor" + countMap.get(paraName) + ");\n");
                                }
                                break;
                        }
                    }
                    if (tonePlayed == 1) {
                        code.insert(code.indexOf("};//task2"), "\n  play(" + paraName + ", " + paraName + "Duration, length" + countMap.get(paraName) + ",tone2, tonalityFactor*0.5" + countMap.get(paraName) + ", speedFactor" + countMap.get(paraName) + ");\n");
                    }
                    code.delete(code.indexOf("//task1"), code.indexOf("//task1") + 7);
                    code.delete(code.indexOf("//task2"), code.indexOf("//task2") + 7);
                    break;
            }
        }
    }
}
