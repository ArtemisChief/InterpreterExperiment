package calculator;

import calculator.component.LexicalAnalysis;
import calculator.component.SyntacticAnalysis;
import calculator.entity.Token;
import calculator.ui.CalculatorGUI;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        CalculatorGUI calculatorGUI = new CalculatorGUI();
        calculatorGUI.setVisible(true);

        while (true) {
            System.out.println("-------------------------------------------------------------------\n");
            System.out.println("Please type a expression:");
            Scanner in = new Scanner(System.in);
            String input = in.next();
            LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
            ArrayList<Token> tokens = lexicalAnalysis.Lex(input);

            if (tokens == null)
                return;

            SyntacticAnalysis syntacticAnalysis = new SyntacticAnalysis(tokens);
            syntacticAnalysis.parse();
        }

    }
}