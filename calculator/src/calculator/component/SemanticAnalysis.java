package calculator.component;

import calculator.entity.Node;
import calculator.entity.Quadruple;

import java.util.ArrayList;

/**
 * 语义分析生成四元式
 * 四元式格式：
 * (op, arg1, arg2, result)
 */
public class SemanticAnalysis {

    private ArrayList<Quadruple> quadruples;
    private int resultCount;

    //开始语义分析
    public ArrayList<Quadruple> GenerateQuadruples(Node abstractSyntaxTree) {
        quadruples=new ArrayList<>();
        resultCount = 0;
        System.out.println("\n-------------------------------------------------------------------\n");

        DFS(abstractSyntaxTree);
        quadruples.get(quadruples.size() - 1).setResult("OUT");   //设置最后一个四元式的寄存器为输出

        for (Quadruple quadruple : quadruples)
            System.out.println(quadruple);

        return quadruples;
    }


    private String DFS(Node curNode) {
        Quadruple quadruple;
        String op = null, arg1 = null, arg2 = null, result = null;
        int termCount = 0;
        int factorCount = 0;
        for (Node child : curNode.getChildren()) {
            switch (child.getType()) {
                case "expression":
                    arg1 = DFS(child);
                    break;

                case "term":
                    termCount++;
                    if (termCount == 1)
                        arg1 = DFS(child);
                    else
                        arg2 = DFS(child);
                    break;

                case "addop":
                    op = child.getChild(0).getContent();
                    break;

                case "factor":
                    factorCount++;
                    if (child.getChild(0).getType() == "number") {
                        if (factorCount == 1)
                            arg1 = child.getChild(0).getContent();
                        else
                            arg2 = child.getChild(0).getContent();
                    } else if (child.getChild(0).getType() == "lparen") {
                        if (factorCount == 1)
                            arg1 = DFS(child.getChild(1));
                        else
                            arg2 = DFS(child.getChild(1));
                    }
                    break;

                case "mulop":
                    op = child.getChild(0).getContent();
                    break;
            }

            if (termCount == 2 || factorCount == 2) {
                resultCount++;
                result = "T" + resultCount;
                quadruple = new Quadruple(op, arg1, arg2, result);
                quadruples.add(quadruple);
                arg1 = result;
            }

            if (termCount == 2)
                termCount--;

            if (factorCount == 2)
                factorCount--;
        }

        if (arg2 == null)
            return arg1;
        else
            return result;
    }
}
