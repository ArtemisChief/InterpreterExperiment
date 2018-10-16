package mini;

public class Token {
    private int syn;
    private String content;

    public Token(){
        this.syn=0;
        this.content="";
    }

    public Token(int syn, String content){
        this.syn=syn;
        this.content=content;
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
                type="段落";
                break;
            case 3:
                type="速度";
                break;
            case 4:
                type="调性";
                break;
            case 5:
                type="段落结束标识";
                break;
            case 6:
                type="播放操作";
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
        return String.format("< %s : %s >",type,content);
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
}
