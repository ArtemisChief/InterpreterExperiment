package calculator;

public class Token {
    private final String nullToken="NULL_TOKEN";
    private String type;
    private String content;

    public Token(){
        this.type=nullToken;
    }

    public Token(String type, String content){
        this.type=type;
        this.content=content;
    }

    public String toString(){
        return String.format("%s: %s",type,content);
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
