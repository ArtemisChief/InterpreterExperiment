package calculator.entity;

public class Token {
    private final String nullToken="NULL_TOKEN";
    private String content;
    private String type;


    public Token(){
        this.type=nullToken;
    }

    public Token(String content, String type) {
        this.content = content;
        this.type = type;
    }

    public String toString(){
        return String.format("类型码: %s\t内容: %s\n",content,type);
    }

    public String getType(){
        return type;
    }

    public String getContent(){
        return content;
    }

    public boolean isEmpty(){
        return type.equals(nullToken);
    }
}
