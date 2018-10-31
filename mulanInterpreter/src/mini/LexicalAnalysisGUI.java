package mini;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LexicalAnalysisGUI {

    public static void main(String[] args) {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("Lexical Analysis");
        // Setting the width and height of frame
        frame.setSize(650, 730);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        /*
         * 调用用户定义的方法并添加组件到面板
         */
        placeComponents(panel);

        // 设置界面可见
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        // 创建 JLabel
        JLabel inputLabel = new JLabel("输入:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        inputLabel.setFont(new Font("微软雅黑",0,18));
        inputLabel.setBounds(10,20,80,25);
        panel.add(inputLabel);

        /*
         * 创建文本域用于用户输入
         */
        JScrollPane inputPane=new JScrollPane();
        inputPane.setBounds(70,20,550,400);
        panel.add(inputPane);
        JTextArea inputText = new JTextArea(10,20);
        inputText.setFont(new Font("微软雅黑",0,15));
        inputText.setBounds(70,20,550,400);
        inputPane.setViewportView(inputText);

        // 输出的文本域
        JLabel outputLabel = new JLabel("输出：");
        outputLabel.setFont(new Font("微软雅黑",0,18));
        outputLabel.setBounds(10,440,80,25);
        panel.add(outputLabel);

        /*
         *创建文本域用于显示输出
         */
        JScrollPane outputPane=new JScrollPane();
        outputPane.setBounds(70,440,550,200);
        panel.add(outputPane);
        JTextArea outputText = new JTextArea(9,20);
        outputText.setFont(new Font("微软雅黑",0,15));
        outputText.setBounds(70,440,550,200);
        outputPane.setViewportView(outputText);

        // 创建按钮
        JButton analyzeButton = new JButton("分析");
        analyzeButton.setBounds(540, 655, 80, 30);
        analyzeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
                String input=inputText.getText();
                lexicalAnalysis.Lex(input);
                ArrayList<Token> tokens=lexicalAnalysis.getTokens();
                if(!tokens.isEmpty()) {
                    outputText.setText("");
                    for (Token token : tokens) {
                        outputText.append(token.toString() + "\n");
                    }
                }
                else
                {
                    outputText.setText("输入为空！");
                }
            }
        });
        panel.add(analyzeButton);


    }


}
