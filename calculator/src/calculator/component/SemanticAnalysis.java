package calculator.component;

import calculator.entity.Node;
import calculator.entity.Quadruple;

import java.util.ArrayList;

public class SemanticAnalysis {
    private Node root;

    ArrayList<Quadruple> quadruples;

    public SemanticAnalysis(Node root) {
        this.root = root;
        quadruples = new ArrayList<>();
    }

    public ArrayList<Quadruple> GenerateQuadruples() {
        DFS();
        return quadruples;
    }

    private void DFS() {
        for (Node child : root.getChildren()) {
            //todo 深度优先遍历语法分析树，产生四元式
            child.getContent();
        }
    }
}
