package mini.entity;

/**
 * 音符类
 * 相当于一个字典
 * 用查表的方法得到各个音的音高和时值持续的时间
 */

public class Note {
    public class Pitch {
        public static final String LC = "262";
        public static final String LCS = "277";
        public static final String LD = "294";
        public static final String LDS = "311";
        public static final String LE = "330";
        public static final String LES = "349";
        public static final String LF = "349";
        public static final String LFS = "370";
        public static final String LG = "392";
        public static final String LGS = "415";
        public static final String LA = "440";
        public static final String LAS = "466";
        public static final String LB = "494";
        public static final String LBS = "523";

        public static final String C = "523";
        public static final String CS = "554";
        public static final String D = "587";
        public static final String DS = "622";
        public static final String E = "659";
        public static final String ES = "698";
        public static final String F = "698";
        public static final String FS = "740";
        public static final String G = "784";
        public static final String GS = "831";
        public static final String A = "880";
        public static final String AS = "932";
        public static final String B = "988";
        public static final String BS = "1047";

        public static final String HC = "1047";
        public static final String HCS = "1109";
        public static final String HD = "1175";
        public static final String HDS = "1245";
        public static final String HE = "1319";
        public static final String HES = "1397";
        public static final String HF = "1397";
        public static final String HFS = "1480";
        public static final String HG = "1568";
        public static final String HGS = "1661";
        public static final String HA = "1760";
        public static final String HAS = "1865";
        public static final String HB = "1976";
        public static final String HBS = "2093";
    }


    public class Duration{
        public static final String _1 = "16";
        public static final String _2 = "8";
        public static final String _4 = "4";
        public static final String _8 = "2";
        public static final String _g = "1";
    }
}
