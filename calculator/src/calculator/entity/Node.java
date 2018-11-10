package calculator.entity;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> childNodes;
    private String content;

    public Node(String content) {
        childNodes = new ArrayList<>();
        this.content = content;
    }

    public Node() {
        childNodes = new ArrayList<>();
    }

    public void setContent(String content) {
        this.content = content;
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

    public String toString() {
        return String.format("%s\n", content);
    }

    public void DFS(int height) {
        for (int i = 0; i < height; i++) {
            System.out.printf("\t");
        }
        System.out.printf("%s\n", this.toString());
        for (Node child : childNodes) {
            child.DFS(height + 1);
        }
    }
}