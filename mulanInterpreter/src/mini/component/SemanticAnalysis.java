package mini.component;

import mini.entity.Node;
import mini.entity.Note;

import java.util.Map;

public class SemanticAnalysis {
    private String code;

    public SemanticAnalysis() {

    }

    public String ConvertToArduino(Node abstractSyntaxTree) {
        code = "";

        //TODO 分析语法树转换成Arduino代码

        //查表得到频率与时值，如
        // String frequency = Note.Pitch.C;
        // String duration = Note.Duration._g;

        return code;
    }
}
