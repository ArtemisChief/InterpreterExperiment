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
}
