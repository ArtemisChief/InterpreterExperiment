package calculator.component;

import calculator.entity.Token;

import java.util.ArrayList;

/*
标识符  letter(letter | digit)*            1
整形数  (digit)+                           2
浮点数  (digit)+.(digit)+                  3
运算符  {'+', '-', '*', '/', '(', ')'}     4
 */

public class LexicalAnalysis {

    private ArrayList<Token> tokens;

    private boolean isLetter(char letter) {
        return letter >= 'a' && letter <= 'z' || letter >= 'A' && letter <= 'Z';
    }

    private boolean isNumber(char num) {
        return num >= '0' && num <= '9';
    }

    private boolean isDot(char dot) {
        return dot == '.';
    }

    private boolean isOperater(char op) {
        return op == '+' || op == '-' || op == '*' || op == '/' || op == '(' || op == ')' || op == '=';
    }

    public LexicalAnalysis() {
        tokens = new ArrayList<>();
    }

    public ArrayList<Token> Lex(String input) {
        int mark = 0, state = 0;
        boolean isFloat = false;
        for (int curr = 0; curr < input.length(); curr++) {
            if (isOperater(input.charAt(curr))) {
                if (state == 1) {
                    System.out.println(input.substring(mark, curr) + "\t是标识符\t\t类型码:1");
                    tokens.add(new Token("1", input.substring(mark, curr)));
                } else if (state == 2) {
                    if (input.charAt(curr - 1) == '.') {
                        System.out.println("词法分析检测到错误，常数不能以\'.\'结尾");
                        return null;
                    }
                    if (isFloat) {
                        System.out.println(input.substring(mark, curr) + "\t是浮点数\t\t类型码:3");
                        tokens.add(new Token("3", input.substring(mark, curr)));
                    } else {
                        System.out.println(input.substring(mark, curr) + "\t是整形数\t\t类型码:2");
                        tokens.add(new Token("2", input.substring(mark, curr)));
                    }
                }
                mark = curr + 1;
                state = 4;
                System.out.println(input.substring(curr, curr + 1) + "\t是运算符\t\t类型码:4");
                tokens.add(new Token("4", input.substring(curr, curr + 1)));
            } else if (isNumber(input.charAt(curr))) {
                if (mark == curr) {
                    state = 2;
                    isFloat = false;
                }
            } else if (isLetter(input.charAt(curr))) {
                if (state == 2) {
                    System.out.println("词法分析检测到错误，常数中不能出现字母");
                    return null;
                }
                if (mark == curr)
                    state = 1;
            } else if (isDot(input.charAt(curr))) {
                if (isFloat) {
                    System.out.println("词法分析检测到错误，常数中不能出现多个\".\"");
                    return null;
                }
                if (state == 1) {
                    System.out.println("词法分析检测到错误，标识符中不能出现\".\"");
                    return null;
                }
                if (state == 4||curr==0) {
                    System.out.println("词法分析检测到错误，常数不能以\".\"开头");
                    return null;
                }
                isFloat = true;
            } else {
                System.out.println("词法分析检测到非法字符");
                return null;
            }
        }
        if (state == 1) {
            System.out.println(input.substring(mark) + "\t是标识符\t\t类型码:1");
            tokens.add(new Token("1", input.substring(mark)));
        } else if (state == 2) {
            if (input.charAt(input.length() - 1) == '.') {
                System.out.println("词法分析检测到错误，数字串不能以\'.\'结尾");
                return null;
            }
            if (isFloat) {
                System.out.println(input.substring(mark) + "\t是浮点数\t\t类型码:3");
                tokens.add(new Token("3", input.substring(mark)));
            } else {
                System.out.println(input.substring(mark) + "\t是整形数\t\t类型码:2");
                tokens.add(new Token("2", input.substring(mark)));
            }
        }

        StringBuilder output = new StringBuilder();
        for (Token t : tokens) {
            output.append("\'").append(t.getContent()).append("\'\t ");
        }
        System.out.println("字符栈: " + output + "\n词法正确\n");
        return tokens;
    }
}