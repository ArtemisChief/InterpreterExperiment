package mini;

import java.util.ArrayList;
import java.util.Scanner;

public class Mini {

    public static void main(String[] args) {
        while(true) {
            LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
            Scanner in = new Scanner(System.in);
//            String input = "13#22";
            String input=in.next();
            ArrayList<Token> tokens=lexicalAnalysis.Lex(input);
            for (Token token:tokens) {
                System.out.println(token.toString());
            }
        }

    }
}
