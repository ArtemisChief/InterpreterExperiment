package calculator;

import javax.swing.*;
import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        try
        {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible",false);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        CalculatorGUI calculatorGUI=new CalculatorGUI();
        calculatorGUI.setVisible(true);



//        while(true) {
//            System.out.println("请输入运算符表达式: ");
//            LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
//            Scanner in = new Scanner(System.in);
//            String input = in.next();
////            String input = "-25 * 2";
//            lexicalAnalysis.Lex(input);
//        }
    }
}
