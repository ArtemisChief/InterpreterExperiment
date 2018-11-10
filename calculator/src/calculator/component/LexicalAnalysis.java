package calculator.component;

import calculator.entity.Token;

import java.util.ArrayList;

/**
 * Token Set
 * number   1
 * add      2
 * sub      3
 * mul      4
 * div      5
 * lparen   6
 * rparen   7
 */

public class LexicalAnalysis {

    private ArrayList<Token> tokens;

    private boolean isDigit(char num) {
        return num >= '0' && num <= '9';
    }

    private boolean isDot(char dot) {
        return dot == '.';
    }

    private boolean isOperater(char op) {
        return op == '+' || op == '-' || op == '*' || op == '/' || op == '(' || op == ')';
    }

    public LexicalAnalysis() {
        tokens = new ArrayList<>();
    }

    public ArrayList<Token> Lex(String input) {
        System.out.println("-------------------------------------------------------------------\n");
        int mark = 0, state = 0;
        boolean isFloat = false;
        for (int curr = 0; curr < input.length(); curr++) {
            if (isOperater(input.charAt(curr))) {
                if (state == 1) {
                    if (input.charAt(curr - 1) == '.') {
                        System.out.println("词法分析检测到错误，实数不能以\'.\'结尾");
                        return null;
                    }
                    System.out.println(input.substring(mark, curr) + "\t\tis Number\t\t\tType: 1");
                    tokens.add(new Token(1, input.substring(mark, curr)));
                }
                mark = curr + 1;
                state = 2;
                switch (input.substring(curr, curr + 1)) {
                    case "+":
                        System.out.println(input.substring(curr, curr + 1) + "\t\tis Add   \t\t\tType: 2");
                        tokens.add(new Token(2, input.substring(curr, curr + 1)));
                        break;
                    case "-":
                        System.out.println(input.substring(curr, curr + 1) + "\t\tis Sub   \t\t\tType: 3");
                        tokens.add(new Token(3, input.substring(curr, curr + 1)));
                        break;
                    case "*":
                        System.out.println(input.substring(curr, curr + 1) + "\t\tis Mul   \t\t\tType: 4");
                        tokens.add(new Token(4, input.substring(curr, curr + 1)));
                        break;
                    case "/":
                        System.out.println(input.substring(curr, curr + 1) + "\t\tis Div   \t\t\tType: 5");
                        tokens.add(new Token(5, input.substring(curr, curr + 1)));
                        break;
                    case "(":
                        System.out.println(input.substring(curr, curr + 1) + "\t\tis Lparen\t\t\tType: 6");
                        tokens.add(new Token(6, input.substring(curr, curr + 1)));
                        break;
                    case ")":
                        System.out.println(input.substring(curr, curr + 1) + "\t\tis Rparen\t\t\tType: 7");
                        tokens.add(new Token(7, input.substring(curr, curr + 1)));
                        break;
                }

            } else if (isDigit(input.charAt(curr))) {
                if (mark == curr) {
                    state = 1;
                    isFloat = false;
                }
            } else if (isDot(input.charAt(curr))) {
                if (isFloat) {
                    System.out.println("词法分析检测到错误，实数中不能出现多个\".\"");
                    return null;
                }
                if (state == 2 || curr == 0) {
                    System.out.println("词法分析检测到错误，实数不能以\".\"开头");
                    return null;
                }
                isFloat = true;
            } else {
                System.out.println("词法分析检测到非法字符");
                return null;
            }
        }
        if (state == 1) {
            if (input.charAt(input.length() - 1) == '.') {
                System.out.println("词法分析检测到错误，实数不能以\'.\'结尾");
                return null;
            }
            System.out.println(input.substring(mark) + "\t\tis Number\t\t\tType: 1");
            tokens.add(new Token(1, input.substring(mark)));
        }

        StringBuilder output = new StringBuilder();
        for (Token t : tokens) {
            output.append("\'").append(t.getContent()).append("\'\t ");
        }
        System.out.println("\nTokens:\n" + output + "\nLexical Analysis Complete\n");
        System.out.println("-------------------------------------------------------------------\n");
        tokens.add(new Token(0, "END"));
        return tokens;
    }
}