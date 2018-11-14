package calculator.component;

import calculator.entity.Node;
import calculator.entity.Token;

import java.util.ArrayList;

/**
 * 文法：
 * expression -> term { addop term }
 * term -> factor { mulop factor }
 * addop -> "+" | "-"
 * mulop -> "*" | "/"
 * factor -> NUM | "(" expression ")"
 */
public class SyntacticAnalysis {

    private ArrayList<Token> tokens;
    private Node AbstractSyntaxTree;
    int index;

    //开始语法分析
    public Node Parse(ArrayList<Token> tokens) {
        index = 0;
        this.tokens = tokens;
        System.out.println("\n-------------------------------------------------------------------\n");
        AbstractSyntaxTree = new Node("root");
        AbstractSyntaxTree.addChild(Expression());

        if (analysisError())
            return null;

        AbstractSyntaxTree.print(0);
        return AbstractSyntaxTree;
    }

    //处理Error，输出错误信息
    private boolean analysisError() {
        if (index != tokens.size() - 1 || !AbstractSyntaxTree.findError().isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < tokens.size() - 1; i++) {
                stringBuilder.append(tokens.get(i).getContent());
            }

            String errorStr = "Error: At \"" + stringBuilder.toString() + "\"\n" + "           ";

            for (int i = 0; i < index; i++)
                for (int j = 0; j < tokens.get(i).getContent().length(); j++)
                    errorStr += " ";

            errorStr += "^\n" + AbstractSyntaxTree.findError();
            System.out.println(errorStr);

            return true;
        }
        return false;
    }

    //expression -> term { addop term }
    private Node Expression() {
        Node expression = new Node("expression");
        Node term = Term();
        expression.addChild(term);

        while (tokens.get(index).getType() == 2 || tokens.get(index).getType() == 3) {
            Node addop = Addop();
            expression.addChild(addop);

            term = Term();
            expression.addChild(term);
        }
        return expression;
    }

    //term -> factor { mulop factor }
    private Node Term() {
        Node term = new Node("term");
        Node factor = Factor();
        term.addChild(factor);

        while (tokens.get(index).getType() == 4 || tokens.get(index).getType() == 5) {
            Node mulop = Mulop();
            term.addChild(mulop);

            factor = Factor();
            term.addChild(factor);
        }
        return term;
    }

    //addop -> "+" | "-"
    private Node Addop() {
        Node addop = new Node("addop");
        Node terminalNode;
        Token Token = tokens.get(index);

        switch (Token.getType()) {
            case 2:
                terminalNode = new Node("add", "+");
                break;
            case 3:
                terminalNode = new Node("sub", "-");
                break;
            default:
                return new Node("Error", "应为\"+\"或\"-\"");
        }
        addop.addChild(terminalNode);
        index++;
        return addop;
    }

    // mulop -> "*" | "/"
    private Node Mulop() {
        Node mulop = new Node("mulop");
        Node terminalNode;
        Token Token = tokens.get(index);

        switch (Token.getType()) {
            case 4:
                terminalNode = new Node("mul", "*");
                break;
            case 5:
                terminalNode = new Node("div", "/");
                break;
            default:
                return new Node("Error:", "应为\"*\"或\"/\"");
        }
        mulop.addChild(terminalNode);
        index++;
        return mulop;
    }

    // factor -> NUM | "(" expression ")"
    private Node Factor() {
        Node factor = new Node("factor");
        Node terminalNode;
        Token Token = tokens.get(index);

        switch (Token.getType()) {
            case 1:
                terminalNode = new Node("number", Token.getContent());
                factor.addChild(terminalNode);
                index++;
                break;

            case 6:
                terminalNode = new Node("lparen", "(");
                factor.addChild(terminalNode);
                index++;

                Node expression = Expression();
                factor.addChild(expression);

                if (tokens.get(index).getType() != 7)
                    return new Node("Error", "缺少右括号");

                terminalNode = new Node("rparen", ")");
                factor.addChild(terminalNode);
                index++;
                break;
            default:
                return new Node("Error", "运算符前后缺少常数");
        }
        return factor;
    }
}