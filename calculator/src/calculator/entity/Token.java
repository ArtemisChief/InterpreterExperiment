package calculator.entity;

public class Token {
    private String content;
    private int type;

    public Token(int type,String content) {
        this.content = content;
        this.type = type;
    }

    public String toString() {
        return String.format("Type: %s\tContent: %s\n", type, content);
    }

    public int getType(){
        return type;
    }

    public String getContent(){
        return content;
    }

    public boolean isExist() {
        return type != 0;
    }
}
