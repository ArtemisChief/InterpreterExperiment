package calculator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class LexicalAnalysis {

    private ArrayList<Token> tokens;


    private boolean isLetter(char letter) {
        return letter >= 'a' && letter <= 'z' || letter >= 'A' && letter <= 'Z';
    }

    private boolean isnumber(char num) {
        return num >= '0' && num <= '9';
    }

    private boolean isOperater(char op) {
        return op == '+' || op == '-' || op == '*' || op == '/' || op == '(' || op == ')';
    }

    public LexicalAnalysis() {
        tokens = new ArrayList<>();
    }

    void Lex(String input) {
        int mark = 0, state = 0;
        for (int curr = 0; curr < input.length(); curr++) {
            if (isOperater(input.charAt(curr))) {
                if (state == 1) {
                    System.out.println(input.substring(mark, curr) + "\t是标识符\t\t类型码:1");
                    tokens.add(new Token("1", input.substring(mark, curr)));
                } else if (state == 2) {
                    System.out.println(input.substring(mark, curr) + "\t是数字\t\t类型码:2");
                    tokens.add(new Token("2", input.substring(mark, curr)));
                }
                mark = curr + 1;
                state = 3;
                System.out.println(input.substring(curr, curr + 1) + "\t是运算符\t\t类型码:3");
                tokens.add(new Token("3", input.substring(curr, curr + 1)));
            } else if (isnumber(input.charAt(curr))) {
                if (mark == curr)
                    state = 2;
            } else if (isLetter(input.charAt(curr))) {
                if (state == 2) {
                    System.out.println("词法分析检测到错误，数字串中不能出现字母");
                    return;
                }
                if (mark == curr)
                    state = 1;
            } else {
                System.out.println("词法分析检测到非法字符");
                return;
            }
        }
        if (state == 1) {
            System.out.println(input.substring(mark) + "\t是标识符\t\t类型码:1");
            tokens.add(new Token("1", input.substring(mark)));
        } else if (state == 2) {
            System.out.println(input.substring(mark) + "\t是数字\t\t类型码:2");
            tokens.add(new Token("2", input.substring(mark)));
        }

        StringBuilder output = new StringBuilder();
        for (Token t : tokens) {
            output.append("\'").append(t.getContent()).append("\'\t ");
        }
        System.out.println("字符栈: " + output + "\n词法正确");
    }
}