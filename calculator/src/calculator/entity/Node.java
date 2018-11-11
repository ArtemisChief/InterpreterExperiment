package calculator.entity;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> childNodes;
    private String content;
    private String type;

    public Node(String type) {
        childNodes = new ArrayList<>();
        this.type = type;
        content = null;
    }

    public Node(String type, String content) {
        childNodes = new ArrayList<>();
        this.type = type;
        this.content = content;
    }

    public String getType(){
        return type;
    }

    public String getContent() {
        return content;
    }

    public void addChild(Node node) {
        childNodes.add(node);
    }

    public ArrayList<Node> getChildren() {
        return childNodes;
    }

    public Node getChild(int index) {
        if (index < childNodes.size())
            return childNodes.get(index);
        return null;
    }

    private boolean isTerminal(){
        if(type=="add"||type=="sub"||type=="mul"||type=="div"||type=="number"||type=="lparen"||type=="rparen")
            return true;
        return false;
    }

    public String toString() {
        return String.format("%s\n", type);
    }

    public String toString(boolean isTerminal) {
        if (isTerminal)
            return String.format("%s\n", content);
        return String.format("%s\n", type);
    }

    public void print(int height) {
        for (int i = 0; i < height; i++) {
            System.out.printf("\t");
        }
        System.out.printf("%s\n", this.toString(isTerminal()));
        for (Node child : childNodes) {
            child.print(height + 1);
        }
    }

    public String findError(){
        String errorStr="";
        for(Node child:childNodes) {
            if (child.getType() == "Error") {
                errorStr += child.getContent();
            }
            errorStr += child.findError();
        }
        return errorStr;
    }
}