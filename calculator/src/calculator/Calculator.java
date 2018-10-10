package calculator;

import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        while(true) {
            LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
            Scanner in = new Scanner(System.in);
            String input = in.next();
//            String input = "23+a *42";
            lexicalAnalysis.Lex(input);
        }
    }
}
