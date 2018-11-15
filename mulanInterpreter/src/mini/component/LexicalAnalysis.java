package mini.component;

import mini.entity.Token;

import java.util.ArrayList;

/**
 *     <错误,-1> 错误token
 *     <score,1> 乐谱
 *     <paragraph,2> 段落
 *     <speed=?,3> 速度
 *     <1=?,4> 调性
 *     <end,5>
 *     <play,6> 播放操作
 *     <(,7>
 *     <),8>
 *     <[,9>
 *     <],10>
 *     <{,11>
 *     <},12>
 *     <<,13>
 *     <>,14>
 *     <*,15>
 *     <,,16>
 *     <&,17>
 *     <\n,97>换行
 *     <音符，98>
 *     <时值,99>
 *     <标识符100 ，标识符指针>
 */

public class LexicalAnalysis {

    private boolean error;
    private ArrayList<Token> tokens;
    private int count;

    private int searchReserve(String s) {
        switch (s) {
            case "score":
                return 1;
            case "paragraph":
                return 2;
            case "speed=":
                return 3;
            case "1=":
                return 4;
            case "end":
                return 5;
            case "play":
                return 6;
            case "C":
            case "D":
            case "E":
            case "F":
            case "G":
            case "A":
            case "B":
                return 95;
            default:
                return -1;
        }
    }

    private boolean isLetter(char letter) {
        return letter >= 'a' && letter <= 'z' || letter >= 'A' && letter <= 'Z';
    }

    private boolean isNumber(char num) {
        return num >= '0' && num <= '9';
    }

    private boolean isTonality(char tonality) {
        return tonality == 'C' || tonality == 'D' || tonality == 'E' || tonality == 'F' || tonality == 'G' || tonality == 'A' || tonality == 'B';
    }

    private boolean isNote(char ch) {
        return (ch >= '0' && ch <= '7') || ch == '[' || ch == ']' || ch == '(' || ch == ')' || ch == '{' || ch == '}';
    }

    private boolean isTime(char ch) {
        return ch == '1' || ch == '2' || ch == '4' || ch == '8' || ch == 'g' || ch == '*';
    }


    //预处理
    public String filterResource(String input) {
        String temp = "";
        for (int i = 0; i < input.length(); i++) {
            if (i + 1 < input.length()) {
                if (input.charAt(i) == '/' && input.charAt(i + 1) == '/') {//若为单行注释“//”,则去除注释后面的东西，直至遇到回车换行
                    while (i < input.length() && input.charAt(i) != '\n') {
                        i++;//向后扫描
                        if (i == input.length() - 1)
                            return temp;
                    }

                }
                if (i + 1 < input.length() && input.charAt(i) == '/' && input.charAt(i + 1) == '*') {//若为多行注释“/* 。。。*/”则去除该内容
                    i += 2;
                    while (input.charAt(i) != '*' || input.charAt(i + 1) != '/') {
                        if (input.charAt(i) == '\n')
                            temp += String.valueOf(input.charAt(i));
                        i++;//继续扫描
                        if (i == input.length()) {
                            count = -1;
                            error = true;
                            return null;
                        }
                    }
                    i += 1;//跨过“*/”
                    continue;
                }
            }
            if (i < input.length() && input.charAt(i) != '\t' && input.charAt(i) != '\r') {//若出现无用字符，则过滤；否则加载
                temp += String.valueOf(input.charAt(i));
            }
        }

        //处理1=和speed=后的空格
        String output = "";
        for (int i = 0; i < temp.length(); i++) {
            //读取到=时将其后跟着的连续空格删去
            if (temp.charAt(i) == '=') {
                output += String.valueOf(temp.charAt(i));
                i++;
                while (i + 1 < temp.length() && temp.charAt(i) == ' ')
                    i++;
            }
            //处理<>间的空格
            if (i + 1 < temp.length() && temp.charAt(i) == '<') {
                //读取到<时，将到>为止其中的空格删去
                output += String.valueOf(temp.charAt(i));
                i++;
                while (i + 1 < temp.length() && temp.charAt(i) != '>') {
                    if (temp.charAt(i) == ' ')
                        i++;
                    else {
                        output += String.valueOf(temp.charAt(i));
                        i++;
                    }
                }
            }

            if (i < temp.length())
                output += String.valueOf(temp.charAt(i));
        }
        return output;
    }


    //分析单个词 isIdentifier表示传入字符串当前是否可能为标识符
    public void Scanner(String inputWord, boolean isIdentifier) {
        //如果字符串为空，直接返回
        if (inputWord.length() == 0)
            return;
        int syn;
        //输入字符串字母开头时
        //标识符 保留字 速度
        if (isLetter(inputWord.charAt(0))) {

            //如果输入为字符串单个调性字母，添加到tokens中
            if (inputWord.length() == 1 && isTonality(inputWord.charAt(0))) {
                tokens.add(new Token(95, inputWord, count));
                return;
            }

            //如果输入字符串以b开头，且此时不可能为表示符，则当作降号处理
            //tokens中添加降号并将降号b以后的字符串分析
            if (!isIdentifier && inputWord.charAt(0) == 'b') {
                tokens.add(new Token(19, "b", count));
                if (inputWord.length() > 1)
                    Scanner(inputWord.substring(1), false);
                return;
            }

            //判断是否为播放操作
            if (inputWord.length() >= 5) {
                if (inputWord.substring(0, 5).equals("play(")) {
                    //如果以play(但不以)结尾
                    if (!inputWord.endsWith(")") || inputWord.length() == 5) {
                        error = true;
                        tokens.add(new Token(-1, "播放语句格式有误", count));
                        return;
                    }
                    syn = 6;
                    //将play和(加入tokens中
                    tokens.add(new Token(syn, "play", count));
                    tokens.add(new Token(7, "(", count));
                    int start = 5;
                    //将()内的标识符扫描分析
                    for (int i = 5; i < inputWord.length() - 1; i++) {
                        //遇到,时分割
                        if (inputWord.charAt(i) == ',') {
                            String temp = inputWord.substring(start, i);
                            if (temp.length() != 0)
                                Scanner(temp, true);
                            start = i + 1;
                            tokens.add(new Token(16, ",", count));
                        }
                        //遇到&时分割
                        else if (inputWord.charAt(i) == '&') {
                            String temp = inputWord.substring(start, i);
                            if (temp.length() != 0)
                                Scanner(temp, true);
                            start = i + 1;
                            tokens.add(new Token(17, "&", count));
                        }
                        //扫描到)时，将当前字符串分析
                        if (i == inputWord.length() - 2) {
                            String temp = inputWord.substring(start, i + 1);
                            if (temp.length() != 0)
                                Scanner(temp, true);
                            start = i;
                        }
                    }
                    //没有出现错误的话在tokens中添加)
                    if (!error)
                        tokens.add(new Token(8, ")", count));
                    return;
                }
            }

            //判断是否是速度标识
            if (inputWord.length() >= 6) {
                //当输入字符串以speed=开头时
                if (inputWord.substring(0, 6).equals("speed=")) {
                    //若speed后未加速度常数，报错
                    if (inputWord.length() == 6) {
                        error = true;
                        tokens.add(new Token(-1, "\"speed=\"后缺少对应速度", count));
                        return;
                    }
                    //扫描speed=后的字符，出现非数字则报错
                    for (int i = 6; i < inputWord.length(); i++) {
                        if (!isNumber(inputWord.charAt(i))) {
                            error = true;
                            tokens.add(new Token(-1, "速度中出现非法字符：" + inputWord.charAt(i), count));
                            return;
                        }
                    }
                    syn = 3;
                    //将速度及速度常数加入tokens中
                    tokens.add(new Token(syn, "speed=", count));
                    tokens.add(new Token(96, inputWord.substring(6), count));
                    return;
                }
            }
            //判断是否为标识符或保留字

            //如果出现非法字符，报错
            for (int i = 1; i < inputWord.length(); i++) {
                if (!isLetter(inputWord.charAt(i)) && !isNumber(inputWord.charAt(i))) {
                    error = true;
                    tokens.add(new Token(-1, "标识符中出现非法字符：" + inputWord.charAt(i), count));
                    return;
                }
            }
            //查找保留字
            syn = searchReserve(inputWord);
            //若字符串为单独一个play，报错
            if (syn == 6) {
                error = true;
                tokens.add(new Token(-1, "play播放操作格式不正确", count));
                return;
            }
            //若搜索不到对应保留字，则视为标识符
            if (syn == -1)
                syn = 100;
            tokens.add(new Token(syn, inputWord, count));
        }
        //如果以数字开头
        //可能为调性或旋律
        else if (isNumber(inputWord.charAt(0))) {
            //当该处应为标识符时，以数字开头则报错
            if (isIdentifier) {
                error = true;
                tokens.add(new Token(-1, "标识符不能以数字开头", count));
                return;
            }

            //输入字符串以1=开头时，严格要求只能为  1=调性  1=升号/降号+调性  其他情况报错
            if (inputWord.length() >= 2 && inputWord.charAt(0) == '1' && inputWord.charAt(1) == '=') {
                syn = 4;
                tokens.add(new Token(syn, "1=", count));
                if (inputWord.length() == 3) {
                    if (!isTonality(inputWord.charAt(2))) {
                        error = true;
                        tokens.add(new Token(-1, "调性格式错误！", count));
                        return;
                    } else {
                        tokens.add(new Token(95, String.valueOf(inputWord.charAt(2)), count));
                        return;
                    }
                }
                if (inputWord.length() == 4) {
                    if (inputWord.charAt(2) != 'b' && inputWord.charAt(2) != '#') {
                        error = true;
                        tokens.add(new Token(-1, "调性格式错误！", count));
                        return;
                    } else
                        Scanner(String.valueOf(inputWord.charAt(2)), false);

                    if (!isTonality(inputWord.charAt(3))) {
                        error = true;
                        tokens.add(new Token(-1, "调性格式错误！", count));
                        return;
                    } else {
                        tokens.add(new Token(95, String.valueOf(inputWord.charAt(3)), count));
                        return;
                    }
                } else {
                    error = true;
                    tokens.add(new Token(-1, "调性格式错误！", count));
                    return;
                }
            }

            //不以1=开头时，数字开头视为旋律
            else {
                for (int i = 0; i < inputWord.length(); i++) {
                    //如果出现 1234<gggg>这种旋律和时长间无空格紧挨着的情况时，将旋律和时长分开分别分析
                    if (inputWord.charAt(i) == '<') {
                        Scanner(inputWord.substring(0, i), false);
                        Scanner(inputWord.substring(i), false);
                        return;
                    }

                    //检查旋律中是否出现非法字符
                    if (!isNote(inputWord.charAt(i)) && inputWord.charAt(i) != '(' && inputWord.charAt(i) != ')' && inputWord.charAt(i) != 'b' && inputWord.charAt(i) != '#') {
                        //出现非法字符时，将非法字符之前的字符分析
                        for (int j = 0; j < i; j++) {
                            Scanner(String.valueOf(inputWord.charAt(j)), false);
                        }
                        error = true;
                        tokens.add(new Token(-1, "旋律中出现非法字符：" + inputWord.charAt(i), count));
                        return;
                    }
                }
                //如果输入不属于01234567，则报错
                if (!isNote(inputWord.charAt(0))) {
                    error = true;
                    tokens.add(new Token(-1, "旋律中出现非法字符：" + inputWord.charAt(0), count));
                    return;
                }
                //将最前面的单个旋律音符加入tokens中，然后去掉最前面的音符继续分析（以将旋律句子分为单个音符token）
                syn = 98;
                tokens.add(new Token(syn, String.valueOf(inputWord.charAt(0)), count));
                if (inputWord.length() > 1)
                    Scanner(inputWord.substring(1), false);
            }
        }

        //如果输入字符串以各种括号开头，则将括号加入tokens中，继续分析剩余字符串
        else if (inputWord.charAt(0) == '(') {
            syn = 7;
            tokens.add(new Token(syn, String.valueOf(inputWord.charAt(0)), count));
            if (inputWord.length() > 1)
                Scanner(inputWord.substring(1), false);
        } else if (inputWord.charAt(0) == ')') {
            syn = 8;
            tokens.add(new Token(syn, String.valueOf(inputWord.charAt(0)), count));
            if (inputWord.length() > 1)
                Scanner(inputWord.substring(1), false);
        } else if (inputWord.charAt(0) == '[') {
            syn = 9;
            tokens.add(new Token(syn, String.valueOf(inputWord.charAt(0)), count));
            if (inputWord.length() > 1)
                Scanner(inputWord.substring(1), false);
        } else if (inputWord.charAt(0) == ']') {
            syn = 10;
            tokens.add(new Token(syn, String.valueOf(inputWord.charAt(0)), count));
            if (inputWord.length() > 1)
                Scanner(inputWord.substring(1), false);
        } else if (inputWord.charAt(0) == '{') {
            syn = 11;
            tokens.add(new Token(syn, String.valueOf(inputWord.charAt(0)), count));
            if (inputWord.length() > 1)
                Scanner(inputWord.substring(1), false);
        } else if (inputWord.charAt(0) == '}') {
            syn = 12;
            tokens.add(new Token(syn, String.valueOf(inputWord.charAt(0)), count));
            if (inputWord.length() > 1)
                Scanner(inputWord.substring(1), false);
        }

        //以<开头时，将输入字符串视为时长
        else if (inputWord.charAt(0) == '<') {
            if (inputWord.charAt(0) == '<' && inputWord.charAt(inputWord.length() - 1) == '>') {
                //若输入仅为<>，报错
                if (inputWord.length() == 2) {
                    error = true;
                    tokens.add(new Token(-1, "<>间缺少时长", count));
                    return;
                }
                //若<后以附点开头，报错
                if (inputWord.charAt(1) == '*') {
                    error = true;
                    tokens.add(new Token(-1, "附点*必须跟在其他音符时长之后", count));
                    return;
                }
                //将<加入tokens中
                tokens.add(new Token(13, "<", count));
                //将其他部分一个一个加入tokens中
                for (int i = 1; i < inputWord.length() - 1; i++) {
                    if (inputWord.charAt(i) == '{') {
                        tokens.add(new Token(11, "{", count));
                        continue;
                    }
                    if (inputWord.charAt(i) == '}') {
                        tokens.add(new Token(12, "}", count));
                        continue;
                    }
                    if (!isTime(inputWord.charAt(i))) {
                        if (inputWord.charAt(i) == ' ')
                            continue;
                        error = true;
                        tokens.add(new Token(-1, "时长中出现非法字符：" + inputWord.charAt(i), count));
                        return;
                    }
                    tokens.add(new Token(99, String.valueOf(inputWord.charAt(i)), count));
                }
                tokens.add(new Token(14, ">", count));
            } else {
                error = true;
                tokens.add(new Token(-1, "时长句子格式错误", count));
                return;
            }

        }
        //以#开头时，将升号加入tokens中，剩余字符串继续分析
        else if (inputWord.charAt(0) == '#') {
            syn = 18;
            tokens.add(new Token(syn, String.valueOf(inputWord.charAt(0)), count));
            if (inputWord.length() > 1)
                Scanner(inputWord.substring(1), false);
        }
        //输入为\n时，直接返回（已在Lex函数中添加换行token）
        else if (inputWord.equals("\n"))
            return;
            //其他非法字符开头则报错
        else {
            error = true;
            tokens.add(new Token(-1, "出现非法字符：" + inputWord.charAt(0), count));
        }
    }


    public void Lex(String input) {
        count=1;
        error=false;
        tokens = new ArrayList();
        //初始化行号
        count = 1;
        //预处理注释、空格
        input = filterResource(input);
        //初始化isIdentifier
        boolean isIdentifier = false;
        //预处理后若为空，则报错
        if (input == null) {
            error = true;
            tokens.add(new Token(-1, "词法分析检测到错误，停止分析", count));
            return;
        }
        //start为当前读取字符串的开头位置
        int start = 0;
        for (int i = 0; i < input.length() && !error; i++) {
            //start==i且当前字符为空格时继续以略过连续空格
            if (input.charAt(i) == ' ' && start == i) {
                i++;
                start = i;
                continue;
            }
            //出现空格时，将start到空格处的字符串分析
            if (input.charAt(i) == ' ') {
                String temp = input.substring(start, i);
                Scanner(temp, isIdentifier);
                //若该字符串为paragraph或score，则当前行之后跟着的字符串应为标识符
                if (temp.equals("paragraph") || temp.equals("score"))
                    isIdentifier = true;
                else
                    isIdentifier = false;
                start = i + 1;
                continue;
            }
            //扫描到换行符时，将start到换行处的字符串分析，分析完后不出错则添加换行token
            if (input.charAt(i) == '\n') {
                String temp = input.substring(start, i);
                if (!temp.equals(""))
                    Scanner(temp, isIdentifier);
                if (!error)
                    count++;
                isIdentifier = false;
                if (!error)
                    tokens.add(new Token(97, "\\n", count));
                start = i + 1;
                continue;
            }
            //扫描到输入的末尾时，将start到末尾处的字符串分析
            if (i == input.length() - 1) {
                String temp = input.substring(start, i + 1);
                Scanner(temp, isIdentifier);
                start = i;
                continue;
            }
        }
    }

    public ArrayList<Token> getTokens() {
        return this.tokens;
    }

    public boolean getError() {
        return error;
    }

    public int getCount() {
        return count;
    }
}