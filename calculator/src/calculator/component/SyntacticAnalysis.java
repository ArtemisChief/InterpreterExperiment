package calculator.component;

import calculator.entity.Node;
import calculator.entity.Token;

import java.util.ArrayList;

public class SyntacticAnalysis {

    ArrayList<Token> tokens;

    public SyntacticAnalysis(ArrayList<Token> tokens){
        this.tokens=tokens;
    }

    public Node parse(){
        System.out.println(tokens);
        return null;
    }

}
