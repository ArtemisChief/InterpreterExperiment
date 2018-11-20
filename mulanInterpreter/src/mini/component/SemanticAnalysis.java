package mini.component;

import mini.entity.Node;
import mini.entity.Note;

import java.util.Map;

public class SemanticAnalysis {

    private Node AbstractSyntaxTree;

    private String code;
    private String errorInfo;

    public SemanticAnalysis() {

    }

    public String ConvertToArduino(Node abstractSyntaxTree) {
        AbstractSyntaxTree = abstractSyntaxTree;

        code = "";
        errorInfo = "";

        //TODO 分析语法树转换成Arduino代码

        code += "#include <Tone.h>\n" +
                "#include <SCoop.h>\n\n" +
                "int tonePin1=2;\n" +
                "int tonePin2=3;\n\n" +
                "Tone tone1;\n" +
                "Tone tone2;\n\n" +
                "const int len1;\n" +
                "const int len2;\n\n" +
                "double speedFactor1;\n" +
                "double speedFactor2;\n\n" +
                "double tonalityFactor1\n" +
                "double tonalityFactor2;\n\n";


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

        return code;
    }

    private void DFS(Node curNode) {
        double speedFactor;
        int count = 1;

        for (Node child : curNode.getChildren()) {
            switch (child.getType()) {
                case "score":
                    DFS(child);
                    break;

                case "execution":
                    DFS(child);
                    break;

                case "statement":
                    if (count > 2) {
                        errorInfo += "语义分析出现错误，Arduino暂不支持2个以上蜂鸣器，请减少声部\n";
                        return;
                    }
                    code += "int *" + child.getChild(0).getContent() + "=new int[len" + count + "]";
                    break;

                case "speed":
                    speedFactor = 60 / Double.parseDouble(child.getChild(0).getContent()) / 4 * 1000;
                    code = code.replace("speedFactor" + count, "speedFactor" + count + "=" + speedFactor);
                    break;

                case "tonality":
                    switch (child.getChild(0).getContent()) {
                        case "bC":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.CF);
                            break;
                        case "C":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.C);
                            break;
                        case "#C":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.CS);
                            break;
                        case "bD":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.DF);
                            break;
                        case "D":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.D);
                            break;
                        case "#D":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.DS);
                            break;
                        case "bE":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.EF);
                            break;
                        case "E":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.E);
                            break;
                        case "#E":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.ES);
                            break;
                        case "bF":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.FF);
                            break;
                        case "F":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.F);
                            break;
                        case "#F":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.FS);
                            break;
                        case "bG":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.GF);
                            break;
                        case "G":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.G);
                            break;
                        case "#G":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.GS);
                            break;
                        case "bA":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.AF);
                            break;
                        case "A":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.A);
                            break;
                        case "#A":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.AS);
                            break;
                        case "bB":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.BF);
                            break;
                        case "B":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.B);
                            break;
                        case "#B":
                            code = code.replace("tonalityFactor" + count, "tonalityFactor" + count + "=" + Note.Tonality.BS);
                            break;
                    }
                    break;

                case "sentence":
                    DFS(curNode);
                    break;

                case "end":
                    count++;
                    break;

                case "melody":
                    break;

                case "rhythm":
                    break;

                case "playlist":
                    break;
            }
        }
    }
}
