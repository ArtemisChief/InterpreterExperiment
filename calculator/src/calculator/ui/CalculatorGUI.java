/*
 * Created by JFormDesigner on Fri Nov 09 14:47:47 CST 2018
 */

package calculator.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import calculator.component.Executer;
import calculator.component.LexicalAnalysis;
import calculator.component.SemanticAnalysis;
import calculator.component.SyntacticAnalysis;
import calculator.entity.Node;
import calculator.entity.Quadruple;
import calculator.entity.Token;
import net.miginfocom.swing.*;

/**
 * @author Chief
 *
 * 计算器GUI版本：
 * 花了大量功夫做了输入限制
 * 基本上还原了Windows10自带计算器的输入模式
 * 交给解释器处理的内容几乎不可能出现错误了（笑）
 */
public class CalculatorGUI extends JFrame {

    StringBuilder inputString = new StringBuilder();          //输入字符串（下面一行）
    StringBuilder expressionString = new StringBuilder();     //表达式字符串（上面一行）
    String result="This is Result!";

    LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
    SyntacticAnalysis syntacticAnalysis = new SyntacticAnalysis();
    SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
    Executer executer = new Executer();

    /**
     * 按键监听，实现小键盘操作计算器
     */
    KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            switch (c) {
                case '0':
                    button0MouseClicked(null);
                    break;
                case '1':
                    button1MouseClicked(null);
                    break;
                case '2':
                    button2MouseClicked(null);
                    break;
                case '3':
                    button3MouseClicked(null);
                    break;
                case '4':
                    button4MouseClicked(null);
                    break;
                case '5':
                    button5MouseClicked(null);
                    break;
                case '6':
                    button6MouseClicked(null);
                    break;
                case '7':
                    button7MouseClicked(null);
                    break;
                case '8':
                    button8MouseClicked(null);
                    break;
                case '9':
                    button9MouseClicked(null);
                    break;
                case '(':
                    leftBracketBtnMouseClicked(null);
                    break;
                case ')':
                    rightBracketBtnMouseClicked(null);
                    break;
                case '+':
                    buttonAddMouseClicked(null);
                    break;
                case '-':
                    buttonMinusMouseClicked(null);
                    break;
                case '*':
                    buttonMutiplyMouseClicked(null);
                    break;
                case '/':
                    buttonDivideMouseClicked(null);
                    break;
                case '.':
                    buttonDotMouseClicked(null);
                    break;
                case '\n':
                    buttonEqualMouseClicked(null);
                    break;
                case '\b':
                    backBtnMouseClicked(null);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };

    public CalculatorGUI() {
        initComponents();
        setFocusable(true);     //只有Form可以聚焦，其他的控件都设置为false以保证按键监听有效
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(keyListener);

        resultTxtField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeFontSizeOnResultTxtField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeFontSizeOnResultTxtField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeFontSizeOnResultTxtField();
            }
        });
    }

    private void changeFontSizeOnResultTxtField() {
        if (resultTxtField.getText().length() < 20)
            resultTxtField.setFont(new Font("Consolas", Font.BOLD, 17));
        else if (resultTxtField.getText().length() < 25)
            resultTxtField.setFont(new Font("Consolas", Font.BOLD, 15));
        else if (resultTxtField.getText().length() < 30)
            resultTxtField.setFont(new Font("Consolas", Font.BOLD, 13));
        else
            resultTxtField.setFont(new Font("Consolas", Font.BOLD, 11));
    }

    private boolean isOperator(char op) {
        if (op == '+' || op == '-' || op == '×' || op == '÷' || op == '*' || op == '/')
            return true;
        return false;
    }

    /**
     * 用于将一个个Digit组成Number添加进表达式中
     */
    private void newNumber() {
        if (inputString.length() > 0 && inputString.charAt(inputString.length() - 1) == '.')
            inputString.deleteCharAt(inputString.length() - 1);
        if (inputString.length() > 2 && hasDot && inputString.substring(2).replace("0", "").isEmpty())
            inputString.delete(inputString.indexOf("."), inputString.length());
        if (inputString.toString().equals("divide by zero"))
            inputString = new StringBuilder();
        expressionString.append(inputString);
        inputString = new StringBuilder();
        resultTxtField.setText(inputString.toString());
        hasDot = false;
    }

    /**
     * 输入单个Digit的同时做了对括号的部分处理
     * 表现在如果右括号出现了，后面跟着输入的不是运算符而是数字
     * 则将括号成对删除
     */
    private void button0MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('0');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void button1MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('1');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void button2MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('2');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void button3MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('3');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void button4MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('4');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void button5MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('5');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void button6MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('6');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void button7MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('7');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void button8MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('8');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void button9MouseClicked(MouseEvent e) {
        if (inputString.length()>0 && inputString.charAt(0) == '0' && !hasDot)
            inputString.deleteCharAt(0);
        inputString.append('9');
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }


    int bracketCount = 0;         //记录还没有成对的左括号数量，即需要的右括号数量

    /**
     * 成对删除括号
     * 思路是找到最后一个左括号之后有几个右括号
     * 然后遍历找到对应数量的左括号的位置
     * 然后删除之间的内容
     */
    private void removeBrackets() {
        int count = 0;
        String str = expressionString.substring(expressionString.lastIndexOf("("), expressionString.length());
        for (int j = 0; j < str.length(); j++) {
            if (str.charAt(j) == ')')
                count++;
        }
        str = expressionString.substring(0, expressionString.lastIndexOf("(") + 1);
        for (int i = 0; i < count; i++) {
            str = str.substring(0, str.lastIndexOf("("));
        }
        expressionString.delete(str.length(), expressionString.length());
    }

    private void leftBracketBtnMouseClicked(MouseEvent e) {
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        expressionString.append('(');
        bracketCount++;
        expressionTxtField.setText(expressionString.toString());
    }

    private void rightBracketBtnMouseClicked(MouseEvent e) {
        if (bracketCount > 0) {
            newNumber();
            if (expressionString.charAt(expressionString.length() - 1) == '(')
                expressionString.append("0");
            if (!isOperator(expressionString.charAt(expressionString.length() - 2))) {
                expressionString.append(')');
                bracketCount--;
            }
            expressionTxtField.setText(expressionString.toString());
        }
    }

    private void backBtnMouseClicked(MouseEvent e) {
        if (inputString.length() != 0) {

            if (inputString.charAt(inputString.length() - 1) == '(')
                bracketCount--;
            if (inputString.charAt(inputString.length() - 1) == ')')
                bracketCount++;
            if (inputString.charAt(inputString.length() - 1) == '.')
                hasDot = false;

            inputString.deleteCharAt(inputString.length() - 1);
            resultTxtField.setText(inputString.toString());
        }
    }

    private void clearBtnMouseClicked(MouseEvent e) {
        newNumber();
        result="This is Result!";
        expressionString=new StringBuilder();
        bracketCount = 0;
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void buttonDivideMouseClicked(MouseEvent e) {
        newNumber();
        if (expressionString.length() == 0)
            expressionString.append('0');
        if (expressionString.charAt(expressionString.length() - 1) == '(')
            expressionString.append('0');
        if (expressionString.length() > 1 && isOperator(expressionString.charAt(expressionString.length() - 2)))
            expressionString.setCharAt(expressionString.length() - 2, '÷');
        else
            expressionString.append(" ÷ ");
        expressionTxtField.setText(expressionString.toString());
    }

    private void buttonMutiplyMouseClicked(MouseEvent e) {
        newNumber();
        if (expressionString.length() == 0)
            expressionString.append('0');
        if (expressionString.charAt(expressionString.length() - 1) == '(')
            expressionString.append('0');
        if (expressionString.length() > 1 && isOperator(expressionString.charAt(expressionString.length() - 2)))
            expressionString.setCharAt(expressionString.length() - 2, '×');
        else
            expressionString.append(" × ");
        expressionTxtField.setText(expressionString.toString());
    }

    private void buttonMinusMouseClicked(MouseEvent e) {
        newNumber();
        if (expressionString.length() == 0)
            expressionString.append('0');
        if (expressionString.charAt(expressionString.length() - 1) == '(')
            expressionString.append('0');
        if (expressionString.length() > 1 && isOperator(expressionString.charAt(expressionString.length() - 2)))
            expressionString.setCharAt(expressionString.length() - 2, '-');
        else
            expressionString.append(" - ");
        expressionTxtField.setText(expressionString.toString());
    }

    private void buttonAddMouseClicked(MouseEvent e) {
        newNumber();
        if (expressionString.length() == 0)
            expressionString.append('0');
        if (expressionString.charAt(expressionString.length() - 1) == '(')
            expressionString.append('0');
        if (expressionString.length() > 1 && isOperator(expressionString.charAt(expressionString.length() - 2)))
            expressionString.setCharAt(expressionString.length() - 2, '+');
        else
            expressionString.append(" + ");
        expressionTxtField.setText(expressionString.toString());
    }

    boolean hasDot = false;

    private void buttonDotMouseClicked(MouseEvent e) {
        if (expressionString.length() > 0 && expressionString.charAt(expressionString.length() - 1) == ')')
            removeBrackets();
        if (!hasDot) {
            if (inputString.length() == 0)
                inputString.append("0");
            inputString.append('.');
            hasDot = true;
        }
        expressionTxtField.setText(expressionString.toString());
        resultTxtField.setText(inputString.toString());
    }

    private void buttonEqualMouseClicked(MouseEvent e) {
        if(inputString.toString().isEmpty())
            return;

        if(expressionString.toString().isEmpty())
            return;

        newNumber();

        calculateResult();

        if(result.contains(".")){
            hasDot=true;
        }
        inputString.append(result);
        resultTxtField.setText(inputString.toString());
        expressionString=new StringBuilder();
        expressionTxtField.setText(expressionString.toString());
    }

    private void calculateResult() {
        while (bracketCount > 0)
            rightBracketBtnMouseClicked(null);

        String input = expressionString.toString().
                replace(" ", "").
                replace('÷', '/').
                replace('×', '*');

        //词法分析
        ArrayList<Token> tokens = lexicalAnalysis.Lex(input);
        if (tokens == null)
            return;

        //语法分析
        Node AbstractSyntaxTree = syntacticAnalysis.Parse(tokens);
        if (AbstractSyntaxTree == null)
            return;

        //语义分析（生成四元式）
        ArrayList<Quadruple> quadruples = semanticAnalysis.GenerateQuadruples(AbstractSyntaxTree);

        //虚拟机执行器
        try {
            result = executer.execute(quadruples);
        } catch (ArithmeticException e) {

            result = e.toString().substring(42);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        expressionTxtField = new JTextField();
        resultTxtField = new JTextField();
        leftBracketBtn = new JButton();
        rightBracketBtn = new JButton();
        backBtn = new JButton();
        clearBtn = new JButton();
        button7 = new JButton();
        button8 = new JButton();
        button9 = new JButton();
        buttonDivide = new JButton();
        button4 = new JButton();
        button5 = new JButton();
        button6 = new JButton();
        buttonMutiply = new JButton();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        buttonMinus = new JButton();
        buttonDot = new JButton();
        button0 = new JButton();
        buttonEqual = new JButton();
        buttonAdd = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout());

        //======== panel1 ========
        {
            panel1.setFocusable(false);
            panel1.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- expressionTxtField ----
            expressionTxtField.setEditable(false);
            expressionTxtField.setFocusable(false);
            expressionTxtField.setFont(new Font("Consolas", Font.PLAIN, 10));
            expressionTxtField.setHorizontalAlignment(SwingConstants.RIGHT);
            panel1.add(expressionTxtField, "cell 0 0 4 1");

            //---- resultTxtField ----
            resultTxtField.setEditable(false);
            resultTxtField.setFocusable(false);
            resultTxtField.setFont(new Font("Consolas", resultTxtField.getFont().getStyle() | Font.BOLD, resultTxtField.getFont().getSize() + 5));
            resultTxtField.setHorizontalAlignment(SwingConstants.RIGHT);
            panel1.add(resultTxtField, "cell 0 1 4 1");

            //---- leftBracketBtn ----
            leftBracketBtn.setText("(");
            leftBracketBtn.setFont(leftBracketBtn.getFont().deriveFont(leftBracketBtn.getFont().getSize() + 5f));
            leftBracketBtn.setFocusable(false);
            leftBracketBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    leftBracketBtnMouseClicked(e);
                }
            });
            panel1.add(leftBracketBtn, "cell 0 3,width 60:60:60,height 40:40:40");

            //---- rightBracketBtn ----
            rightBracketBtn.setText(")");
            rightBracketBtn.setFont(rightBracketBtn.getFont().deriveFont(rightBracketBtn.getFont().getSize() + 5f));
            rightBracketBtn.setFocusable(false);
            rightBracketBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    rightBracketBtnMouseClicked(e);
                }
            });
            panel1.add(rightBracketBtn, "cell 1 3,width 60:60:60,height 40:40:40");

            //---- backBtn ----
            backBtn.setText("\u2190");
            backBtn.setFont(backBtn.getFont().deriveFont(backBtn.getFont().getSize() + 5f));
            backBtn.setFocusable(false);
            backBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    backBtnMouseClicked(e);
                }
            });
            panel1.add(backBtn, "cell 2 3,width 60:60:60,height 40:40:40");

            //---- clearBtn ----
            clearBtn.setText("CE");
            clearBtn.setFont(clearBtn.getFont().deriveFont(clearBtn.getFont().getSize() + 5f));
            clearBtn.setFocusable(false);
            clearBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    clearBtnMouseClicked(e);
                }
            });
            panel1.add(clearBtn, "cell 3 3,width 60:60:60,height 40:40:40");

            //---- button7 ----
            button7.setText("7");
            button7.setFont(button7.getFont().deriveFont(button7.getFont().getStyle() | Font.BOLD, button7.getFont().getSize() + 5f));
            button7.setFocusable(false);
            button7.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button7MouseClicked(e);
                }
            });
            panel1.add(button7, "cell 0 4,width 60:60:60,height 40:40:40");

            //---- button8 ----
            button8.setText("8");
            button8.setFont(button8.getFont().deriveFont(button8.getFont().getStyle() | Font.BOLD, button8.getFont().getSize() + 5f));
            button8.setFocusable(false);
            button8.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button8MouseClicked(e);
                }
            });
            panel1.add(button8, "cell 1 4,width 60:60:60,height 40:40:40");

            //---- button9 ----
            button9.setText("9");
            button9.setFont(button9.getFont().deriveFont(button9.getFont().getStyle() | Font.BOLD, button9.getFont().getSize() + 5f));
            button9.setFocusable(false);
            button9.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button9MouseClicked(e);
                }
            });
            panel1.add(button9, "cell 2 4,width 60:60:60,height 40:40:40");

            //---- buttonDivide ----
            buttonDivide.setText("\u00f7");
            buttonDivide.setFont(buttonDivide.getFont().deriveFont(buttonDivide.getFont().getSize() + 5f));
            buttonDivide.setFocusable(false);
            buttonDivide.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    buttonDivideMouseClicked(e);
                }
            });
            panel1.add(buttonDivide, "cell 3 4,width 60:60:60,height 40:40:40");

            //---- button4 ----
            button4.setText("4");
            button4.setFont(button4.getFont().deriveFont(button4.getFont().getStyle() | Font.BOLD, button4.getFont().getSize() + 5f));
            button4.setFocusable(false);
            button4.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button4MouseClicked(e);
                }
            });
            panel1.add(button4, "cell 0 5,width 60:60:60,height 40:40:40");

            //---- button5 ----
            button5.setText("5");
            button5.setFont(button5.getFont().deriveFont(button5.getFont().getStyle() | Font.BOLD, button5.getFont().getSize() + 5f));
            button5.setFocusable(false);
            button5.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button5MouseClicked(e);
                }
            });
            panel1.add(button5, "cell 1 5,width 60:60:60,height 40:40:40");

            //---- button6 ----
            button6.setText("6");
            button6.setFont(button6.getFont().deriveFont(button6.getFont().getStyle() | Font.BOLD, button6.getFont().getSize() + 5f));
            button6.setFocusable(false);
            button6.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button6MouseClicked(e);
                }
            });
            panel1.add(button6, "cell 2 5,width 60:60:60,height 40:40:40");

            //---- buttonMutiply ----
            buttonMutiply.setText("\u00d7");
            buttonMutiply.setFont(buttonMutiply.getFont().deriveFont(buttonMutiply.getFont().getSize() + 5f));
            buttonMutiply.setFocusable(false);
            buttonMutiply.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    buttonMutiplyMouseClicked(e);
                }
            });
            panel1.add(buttonMutiply, "cell 3 5,width 60:60:60,height 40:40:40");

            //---- button1 ----
            button1.setText("1");
            button1.setFont(button1.getFont().deriveFont(button1.getFont().getStyle() | Font.BOLD, button1.getFont().getSize() + 5f));
            button1.setFocusable(false);
            button1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button1MouseClicked(e);
                }
            });
            panel1.add(button1, "cell 0 6,width 60:60:60,height 40:40:40");

            //---- button2 ----
            button2.setText("2");
            button2.setFont(button2.getFont().deriveFont(button2.getFont().getStyle() | Font.BOLD, button2.getFont().getSize() + 5f));
            button2.setFocusable(false);
            button2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button2MouseClicked(e);
                }
            });
            panel1.add(button2, "cell 1 6,width 60:60:60,height 40:40:40");

            //---- button3 ----
            button3.setText("3");
            button3.setFont(button3.getFont().deriveFont(button3.getFont().getStyle() | Font.BOLD, button3.getFont().getSize() + 5f));
            button3.setFocusable(false);
            button3.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button3MouseClicked(e);
                }
            });
            panel1.add(button3, "cell 2 6,width 60:60:60,height 40:40:40");

            //---- buttonMinus ----
            buttonMinus.setText("-");
            buttonMinus.setFont(buttonMinus.getFont().deriveFont(buttonMinus.getFont().getSize() + 5f));
            buttonMinus.setFocusable(false);
            buttonMinus.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    buttonMinusMouseClicked(e);
                }
            });
            panel1.add(buttonMinus, "cell 3 6,width 60:60:60,height 40:40:40");

            //---- buttonDot ----
            buttonDot.setText(".");
            buttonDot.setFont(buttonDot.getFont().deriveFont(buttonDot.getFont().getSize() + 5f));
            buttonDot.setFocusable(false);
            buttonDot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    buttonDotMouseClicked(e);
                }
            });
            panel1.add(buttonDot, "cell 0 7,width 60:60:60,height 40:40:40");

            //---- button0 ----
            button0.setText("0");
            button0.setFont(button0.getFont().deriveFont(button0.getFont().getStyle() | Font.BOLD, button0.getFont().getSize() + 5f));
            button0.setFocusable(false);
            button0.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button0MouseClicked(e);
                }
            });
            panel1.add(button0, "cell 1 7,width 60:60:60,height 40:40:40");

            //---- buttonEqual ----
            buttonEqual.setText("=");
            buttonEqual.setFont(buttonEqual.getFont().deriveFont(buttonEqual.getFont().getSize() + 5f));
            buttonEqual.setFocusable(false);
            buttonEqual.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    buttonEqualMouseClicked(e);
                }
            });
            panel1.add(buttonEqual, "cell 2 7,width 60:60:60,height 40:40:40");

            //---- buttonAdd ----
            buttonAdd.setText("+");
            buttonAdd.setFont(buttonAdd.getFont().deriveFont(buttonAdd.getFont().getSize() + 5f));
            buttonAdd.setFocusable(false);
            buttonAdd.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    buttonAddMouseClicked(e);
                }
            });
            panel1.add(buttonAdd, "cell 3 7,width 60:60:60,height 40:40:40");
        }
        contentPane.add(panel1);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JTextField expressionTxtField;
    private JTextField resultTxtField;
    private JButton leftBracketBtn;
    private JButton rightBracketBtn;
    private JButton backBtn;
    private JButton clearBtn;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton buttonDivide;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton buttonMutiply;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton buttonMinus;
    private JButton buttonDot;
    private JButton button0;
    private JButton buttonEqual;
    private JButton buttonAdd;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}