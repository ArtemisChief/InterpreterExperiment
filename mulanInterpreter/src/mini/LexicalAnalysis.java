package mini;

import java.util.ArrayList;

public class LexicalAnalysis {

    private boolean error=false;
    private ArrayList<Token> tokens=new ArrayList();

    /*
    <错误,-1> 错误token
    <score,1> 乐谱
    <paragraph,2> 段落
    <speed=?,3> 速度
    <1=?,4> 调性
    <end,5>
    <play,6> 播放操作
    <(,7>
    <),8>
    <[,9>
    <],10>
    <{,11>
    <},12>
    <<,13>
    <>,14>
    <*,15>
    <,,16>
    <&,17>
    <\n,97>换行
    <音符，98>
    <时值,99>
    <标识符100 ，标识符指针>
    */



    private  int searchReserve(String s){
        switch (s){
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

    private boolean isLetter(char letter) { return letter >= 'a' && letter <= 'z' || letter >= 'A' && letter <= 'Z'; }

    private boolean isNumber(char num) {
        return num >= '0' && num <= '9';
    }

    private boolean isTonality(char tonality){return tonality=='C'||tonality=='D'||tonality=='E'||tonality=='F'||tonality=='G'||tonality=='A'||tonality=='B';}

    private boolean isNote(char ch) {
        return (ch >= '0' && ch <= '7')||ch=='['||ch==']'||ch=='('||ch==')'||ch=='{'||ch=='}';
    }

    private boolean isTime(char ch) {
        return ch == '1' || ch == '2'|| ch == '4'|| ch == '8'|| ch == 'g'|| ch == '*';
    }


    public String filterResource(String input){
        String temp="";
        for(int i=0;i<input.length();i++){
            if(i+1<input.length()){
                if (input.charAt(i) == '/' && input.charAt(i + 1) == '/') {//若为单行注释“//”,则去除注释后面的东西，直至遇到回车换行
                    while (input.charAt(i) != '\n') {
                        i++;//向后扫描
                    }
                }
                if (input.charAt(i) == '/' && input.charAt(i + 1) == '*') {//若为多行注释“/* 。。。*/”则去除该内容
                    i += 2;
                    while (input.charAt(i) != '*' || input.charAt(i + 1) != '/') {
                        i++;//继续扫描
                        if (i == input.length()) {
                            System.out.println("注释出错，没有找到 */，程序结束！！！\n");
                            return null;
                        }
                    }
                    i += 2;//跨过“*/”
                }
                if(input.charAt(i) == '\n' && input.charAt(i + 1) == '\n')
                    i++;
            }
            if (input.charAt(i) != '\t'&&input.charAt(i) != '\r'){//若出现无用字符，则过滤；否则加载
                temp+=String.valueOf(input.charAt(i));
            }
        }
        String output="";
        for(int i=0;i<temp.length();i++){
            if(temp.charAt(i)=='='){
                output+=String.valueOf(temp.charAt(i));
                i++;
                while(i+1<temp.length()&&temp.charAt(i)==' ')
                    i++;
            }
            if(i+1<temp.length()&&temp.charAt(i)=='<'){
                output+=String.valueOf(temp.charAt(i));
                i++;
                while (i+1<temp.length()&&temp.charAt(i)!='>'){
                    if(temp.charAt(i)==' ')
                        i++;
                    else{
                        output+=String.valueOf(temp.charAt(i));
                        i++;
                    }
                }
            }

            if(i<temp.length())
                output+=String.valueOf(temp.charAt(i));
        }
        return output;
    }


    public void Scanner(String inputWord,boolean isIdentifier){
        int syn;
        //标识符 保留字 速度
        if(isLetter(inputWord.charAt(0))){
            if(inputWord.length()==1&&isTonality(inputWord.charAt(0))){
                tokens.add(new Token(95,inputWord));
                return;
            }
            if(!isIdentifier&&inputWord.charAt(0)=='b'){
                    tokens.add(new Token(19,"b"));
                    if(inputWord.length()>1)
                        Scanner(inputWord.substring(1),false);
                    return;
            }

            //播放
            if(inputWord.length()>=5){
                if(inputWord.substring(0,5).equals("play(")){
                    if(!inputWord.endsWith(")")||inputWord.length()==5){
                        error = true;
                        System.out.println(inputWord+" : 播放语句格式有误");
                        return;
                    }
                    syn=6;
                    tokens.add(new Token(syn,"play"));
                    tokens.add(new Token(7,"("));
                    int start=5;
                    for(int i=5;i<inputWord.length()-1;i++){
                        if(inputWord.charAt(i)==','){
                            String temp=inputWord.substring(start,i);
                            Scanner(temp,true);
                            start=i+1;
                            tokens.add(new Token(16,","));
                        }
                        else if(inputWord.charAt(i)=='&'){
                            String temp=inputWord.substring(start,i);
                            Scanner(temp,true);
                            start=i+1;
                            tokens.add(new Token(17,"&"));
                        }
                        if(i==inputWord.length()-2){
                            String temp=inputWord.substring(start,i+1);
                            Scanner(temp,true);
                            start=i;
                        }
                    }
                    tokens.add(new Token(8,")"));
                    return;
                }
            }

            //速度
            if(inputWord.length()>=6){
                if(inputWord.substring(0,6).equals("speed=")){
                    if(inputWord.length()==6){
                        error=true;
                        System.out.println(inputWord+" : \"speed=\"后缺少对应速度");
                        return;
                    }
                    for (int i=6;i<inputWord.length();i++) {
                        if (!isNumber(inputWord.charAt(i))) {
                            error = true;
                            System.out.println(inputWord+" : 速度中出现非法字符：" + inputWord.charAt(i));
                            return;
                        }
                    }
                    syn=3;
                    tokens.add(new Token(syn,"speed="));
                    tokens.add(new Token(96,inputWord.substring(6)));
                    return;
                }
            }
            //标识符 保留字
            for(int i=1;i<inputWord.length();i++){
                if(!isLetter(inputWord.charAt(i))&&!isNumber(inputWord.charAt(i))){
                   error=true;
                   System.out.println(inputWord+" : 标识符中出现非法字符："+inputWord.charAt(i));
                   return;
                }
            }
            syn=searchReserve(inputWord);
            if(syn==6){
                error=true;
                System.out.println(inputWord+" : play播放操作格式不正确");
                return;
            }
            if(syn==-1)
                syn=100;
            tokens.add(new Token(syn,inputWord));
        }
        //调性 旋律
        else if(isNumber(inputWord.charAt(0))){
            if(inputWord.length()>=2&&inputWord.charAt(0)=='1'&&inputWord.charAt(1)=='='){
                syn=4;
                tokens.add(new Token(syn,"1="));
                Scanner(inputWord.substring(2),false);
            }
            else{
                    if (!isNote(inputWord.charAt(0))) {
                        error = true;
                        System.out.println(inputWord+" : 旋律中出现非法字符：" + inputWord.charAt(0));
                        return;
                    }
                syn=98;
                tokens.add(new Token(syn,String.valueOf(inputWord.charAt(0))));
                if(inputWord.length()>1)
                    Scanner(inputWord.substring(1),false);
            }
        }
        else if(inputWord.charAt(0)=='('){
            syn=7;
            tokens.add(new Token(syn,String.valueOf(inputWord.charAt(0))));
            if(inputWord.length()>1)
                Scanner(inputWord.substring(1),false);
        }
        else if(inputWord.charAt(0)==')'){
            syn=8;
            tokens.add(new Token(syn,String.valueOf(inputWord.charAt(0))));
            if(inputWord.length()>1)
                Scanner(inputWord.substring(1),false);
        }
        else if(inputWord.charAt(0)=='['){
            syn=9;
            tokens.add(new Token(syn,String.valueOf(inputWord.charAt(0))));
            if(inputWord.length()>1)
                Scanner(inputWord.substring(1),false);
        }
        else if(inputWord.charAt(0)==']'){
            syn=10;
            tokens.add(new Token(syn,String.valueOf(inputWord.charAt(0))));
            if(inputWord.length()>1)
                Scanner(inputWord.substring(1),false);
        }
        else if(inputWord.charAt(0)=='{'){
            syn=11;
            tokens.add(new Token(syn,String.valueOf(inputWord.charAt(0))));
            if(inputWord.length()>1)
                Scanner(inputWord.substring(1),false);
        }
        else if(inputWord.charAt(0)=='}'){
            syn=12;
            tokens.add(new Token(syn,String.valueOf(inputWord.charAt(0))));
            if(inputWord.length()>1)
                Scanner(inputWord.substring(1),false);
        }
        //时长
        else if(inputWord.charAt(0)=='<'&&inputWord.charAt(inputWord.length()-1)=='>'){
            if(inputWord.length()==2){
                error = true;
                System.out.println(inputWord+" : <>间缺少时长" );
                return;
            }
            if(inputWord.charAt(1)=='*'){
                error = true;
                System.out.println(inputWord+" : 附点*必须跟在其他音符时长之后" );
                return;
            }
            tokens.add(new Token(13,"<"));
            for (int i=1;i<inputWord.length()-1;i++) {
                if (!isTime(inputWord.charAt(i))) {
                    if(inputWord.charAt(i)==' ')
                        continue;
                    error = true;
                    System.out.println(inputWord+" : 时长中出现非法字符：" + inputWord.charAt(i));
                    return;
                }
                tokens.add(new Token(99,String.valueOf(inputWord.charAt(i))));
            }
            tokens.add(new Token(14,">"));
        }
        else if(inputWord.charAt(0)=='#'){
            syn=18;
            tokens.add(new Token(syn,String.valueOf(inputWord.charAt(0))));
            if(inputWord.length()>1)
                Scanner(inputWord.substring(1),false);
        }
        else if(inputWord.equals("\n"))
            return;
        else{
            error = true;
            System.out.println(inputWord+" : 出现非法字符：" + inputWord.charAt(0));
        }
    }


    public void Lex(String input){
        input=filterResource(input);
        boolean isIdentifier=false;
        if(input==null){
            System.out.println("词法分析检测到错误，停止程序");
            tokens.add(new Token(-1,"error"));
            return;
        }
        int start=0;
        for(int i=0;i<input.length()&&!error;i++){
            if(input.charAt(i)==' '&&start==i){
                i++;
                start=i;
                continue;
            }
            if(input.charAt(i)==' '){
                String temp=input.substring(start,i);
                Scanner(temp,isIdentifier);
                if(temp.equals("paragraph")||temp.equals("score"))
                    isIdentifier=true;
                else
                    isIdentifier=false;
                start=i+1;
                continue;
            }
            if(input.charAt(i)=='\n') {
                String temp=input.substring(start,i);
                if(!temp.equals(""))
                    Scanner(temp,isIdentifier);
                isIdentifier=false;
                tokens.add(new Token(97, "\\n"));
                start=i+1;
                continue;
            }
            if(i==input.length()-1){
                String temp=input.substring(start,i+1);
                Scanner(temp,isIdentifier);
                start=i;
                continue;
            }

        }
        if(error) {
            System.out.println("词法分析检测到错误，停止程序");
            tokens.add(new Token(-1,"error"));
        }


    }

    public ArrayList<Token> getTokens(){return this.tokens;}




}
