package calculator;

import calculator.ui.CalculatorGUI;
import javax.swing.*;

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
    }
}
