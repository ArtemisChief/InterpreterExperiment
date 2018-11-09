/*
 * Created by JFormDesigner on Fri Nov 09 14:47:47 CST 2018
 */

package calculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author Chief
 */
public class CalculatorGUI extends JFrame {

    StringBuilder inputString=new StringBuilder();
    KeyListener keyListener=new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
            char c=e.getKeyChar();
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
        setFocusable(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(keyListener);
    }


    private void button0MouseClicked(MouseEvent e) {
        inputString.append('0');
        expressionTxtField.setText(inputString.toString());
    }
    private void button1MouseClicked(MouseEvent e) {
        inputString.append('1');
        expressionTxtField.setText(inputString.toString());
    }
    private void button2MouseClicked(MouseEvent e) {
        inputString.append('2');
        expressionTxtField.setText(inputString.toString());
    }
    private void button3MouseClicked(MouseEvent e) {
        inputString.append('3');
        expressionTxtField.setText(inputString.toString());
    }
    private void button4MouseClicked(MouseEvent e) {
        inputString.append('4');
        expressionTxtField.setText(inputString.toString());
    }
    private void button5MouseClicked(MouseEvent e) {
        inputString.append('5');
        expressionTxtField.setText(inputString.toString());
    }
    private void button6MouseClicked(MouseEvent e) {
        inputString.append('6');
        expressionTxtField.setText(inputString.toString());
    }
    private void button7MouseClicked(MouseEvent e) {
        inputString.append('7');
        expressionTxtField.setText(inputString.toString());
    }
    private void button8MouseClicked(MouseEvent e) {
        inputString.append('8');
        expressionTxtField.setText(inputString.toString());
    }
    private void button9MouseClicked(MouseEvent e) {
        inputString.append('9');
        expressionTxtField.setText(inputString.toString());
    }
    private void leftBracketBtnMouseClicked(MouseEvent e) {
        inputString.append('(');
        expressionTxtField.setText(inputString.toString());
    }
    private void rightBracketBtnMouseClicked(MouseEvent e) {
        inputString.append(')');
        expressionTxtField.setText(inputString.toString());
    }
    private void backBtnMouseClicked(MouseEvent e) {
        if (inputString.length() != 0) {
            if (inputString.length() > 1)
                if (inputString.charAt(inputString.length() - 2) == '+'
                        || inputString.charAt(inputString.length() - 2) == '-'
                        || inputString.charAt(inputString.length() - 2) == '×'
                        || inputString.charAt(inputString.length() - 2) == '÷')
                    inputString.delete(inputString.length() - 3, inputString.length() - 1);

            inputString.deleteCharAt(inputString.length() - 1);
            expressionTxtField.setText(inputString.toString());
        }
    }
    private void clearBtnMouseClicked(MouseEvent e) {
        inputString.delete(0,inputString.length());
        expressionTxtField.setText(inputString.toString());
    }
    private void buttonDivideMouseClicked(MouseEvent e) {
        inputString.append(" ÷ ");
        expressionTxtField.setText(inputString.toString());
    }
    private void buttonMutiplyMouseClicked(MouseEvent e) {
        inputString.append(" × ");
        expressionTxtField.setText(inputString.toString());
    }
    private void buttonMinusMouseClicked(MouseEvent e) {
        inputString.append(" - ");
        expressionTxtField.setText(inputString.toString());
    }
    private void buttonAddMouseClicked(MouseEvent e) {
        inputString.append(" + ");
        expressionTxtField.setText(inputString.toString());
    }
    private void buttonDotMouseClicked(MouseEvent e) {
        inputString.append('.');
        expressionTxtField.setText(inputString.toString());
    }
    private void buttonEqualMouseClicked(MouseEvent e) {
        calculateResult();
    }

    private void calculateResult(){
        String input=inputString.toString().replace(" ","").replace('÷','/').replace('×','*');
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
        lexicalAnalysis.Lex(input);
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
            expressionTxtField.setFont(new Font("Consolas", Font.PLAIN, 15));
            expressionTxtField.setHorizontalAlignment(SwingConstants.RIGHT);
            panel1.add(expressionTxtField, "cell 0 0 4 1");

            //---- resultTxtField ----
            resultTxtField.setEditable(false);
            resultTxtField.setFocusable(false);
            resultTxtField.setFont(resultTxtField.getFont().deriveFont(resultTxtField.getFont().getStyle() | Font.BOLD, resultTxtField.getFont().getSize() + 5f));
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
