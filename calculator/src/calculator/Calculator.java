package calculator;

public class Calculator {

    public static void main(String[] args) {
        LexicalAnalysis lexicalAnalysis=new LexicalAnalysis();
        String input = "(t1+p)*(42+3)";
        lexicalAnalysis.Lex(input);
    }
}
