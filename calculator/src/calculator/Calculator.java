package calculator;

import calculator.component.Executer;
import calculator.component.LexicalAnalysis;
import calculator.component.SemanticAnalysis;
import calculator.component.SyntacticAnalysis;
import calculator.entity.Node;
import calculator.entity.Quadruple;
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
            System.out.println("Please type a expression:");
            Scanner in = new Scanner(System.in);
            String input = in.next();

            //词法分析
            LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
            ArrayList<Token> tokens = lexicalAnalysis.Lex(input);

            if (tokens == null)
                continue;

            //语法分析
            SyntacticAnalysis syntacticAnalysis = new SyntacticAnalysis();
            Node AbstractSyntaxTree = syntacticAnalysis.Parse(tokens);

            if (AbstractSyntaxTree == null)
                continue;

            //语义分析（生成四元式）
            SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
            ArrayList<Quadruple> quadruples = semanticAnalysis.GenerateQuadruples(AbstractSyntaxTree);

            if(quadruples==null)
                return;

            //虚拟机执行器
            Executer executer = new Executer();
            try {
                String result = executer.execute(quadruples);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}