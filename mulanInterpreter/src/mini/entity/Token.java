package mini.entity;

public class Token {
    private int syn;
    private String content;
    private  int count;

    public Token(){
        this.syn=0;
        this.content="";
        this.count=0;
    }

    public Token(int syn, String content,int count){
        this.syn=syn;
        this.content=content;
        this.count=count;
    }

    public String toString(){
        String type="";
        switch(this.syn){
            case -1:
                type="错误";
                break;
            case 1:
                type="乐谱";
                break;
            case 2:
                type="段落符号";
                break;
            case 3:
                type="速度符号";
                break;
            case 4:
                type="调性符号";
                break;
            case 5:
                type="段落结束标识";
                break;
            case 6:
                type="播放操作";
                break;
            case 7:
                type="左括号";
                break;
            case 8:
                type="右括号";
                break;
            case 9:
                type="高八度左括号";
                break;
            case 10:
                type="高八度右括号";
                break;
            case 11:
                type="连音左括号";
                break;
            case 12:
                type="连音右括号";
                break;
            case 13:
                type="时长左括号";
                break;
            case 14:
                type="时长右括号";
                break;
            case 15:
                type="附点";
                break;
            case 16:
                type="逗号";
                break;
            case 17:
                type="同时播放符号";
                break;
            case 18:
                type="升号";
                break;
            case 19:
                type="降号";
                break;
            case 95:
                type="调性";
                break;
            case 96:
                type="速度常数";
                break;
            case 97:
                type="换行符";
                break;
            case 98:
                type="旋律音符";
                break;
            case 99:
                type="音符时值";
                break;
            case 100:
                type="标识符";
                break;
        }
        if(count==-1)
            return String.format("%-9s\t%-13s\t类型码:%s\n",content,type,syn);
        return String.format("Line%-8s\t%-9s\t%-13s\t类型码:%s\n",count,content,type,syn);
    }

    public int getSyn(){
        return syn;
    }

    public String getContent(){
        return content;
    }

    public boolean isEmpty(){
        return syn==0;
    }

    public int getCount(){
        return count;
    }
}
