package calculator;

import calculator.component.LexicalAnalysis;
import calculator.ui.CalculatorGUI;
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

//        while (true) {
//            System.out.println("Please type a expression:");
//            Scanner in = new Scanner(System.in);
//            String input = in.next();
//            LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
//            lexicalAnalysis.Lex(input);
//        }

    }
}