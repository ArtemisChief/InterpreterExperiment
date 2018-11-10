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
    private Node root;
    int index = 0;

    public SyntacticAnalysis(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public Node getRoot() {
        return root;
    }

    public Node parse() {
        root = Expression();

        if (index != tokens.size() - 1) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < tokens.size() - 1; i++) {
                stringBuilder.append(tokens.get(i).getContent());
            }

            String errorStr = "Error: At \"" + stringBuilder.toString() + "\"\n" + "           ";

            for (int i = 0; i < index; i++)
                for (int j = 0; j < tokens.get(i).getContent().length(); j++)
                    errorStr += " ";

            errorStr += "^\n";

            System.out.println(errorStr);
            return null;
        }

        root.print(0);
        return root;
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
                terminalNode = new Node("+");
                break;
            case 3:
                terminalNode = new Node("-");
                break;
            default:
                return new Node("Error: 应为\"+\"或\"-\"");
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
                terminalNode = new Node("*");
                break;
            case 5:
                terminalNode = new Node("/");
                break;
            default:
                return new Node("Error: 应为\"*\"或\"/\"");
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
                terminalNode = new Node(Token.getContent());
                factor.addChild(terminalNode);
                index++;
                break;

            case 6:
                terminalNode = new Node("(");
                factor.addChild(terminalNode);
                index++;

                Node expression = Expression();
                factor.addChild(expression);

                if (tokens.get(index).getType() != 7)
                    return new Node("Error: 缺少右括号");

                terminalNode = new Node(")");
                factor.addChild(terminalNode);
                index++;
                break;
        }
        return factor;
    }
}