package calculator.component;

import calculator.entity.Quadruple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟机执行器
 * 执行语义分析产生的四元式
 * 并计算出结果
 */
public class Executer {

    //执行四元式产生结果
    public String execute(ArrayList<Quadruple> quadruples) {

        //用于存储变量和值表
        Map<String, BigDecimal> variables = new HashMap<>();

        //来自冬雨姐姐的安利，精确度和长度很高的数据类型
        BigDecimal result = null;
        BigDecimal arg1;
        BigDecimal arg2;
        String variable;

        for (Quadruple quadruple : quadruples) {
            variable = quadruple.getResult();

            //处理是常数还是变量
            if (quadruple.getArg1().charAt(0) != 'T')
                arg1 = new BigDecimal(quadruple.getArg1());
            else
                arg1 = variables.get(quadruple.getArg1());

            if (quadruple.getArg2().charAt(0) != 'T')
                arg2 = new BigDecimal(quadruple.getArg2());
            else
                arg2 = variables.get(quadruple.getArg2());

            switch (quadruple.getOp()) {
                case "+":
                    result = arg1.add(arg2);
                    break;
                case "-":
                    result = arg1.subtract(arg2);
                    break;
                case "*":
                    result = arg1.multiply(arg2);
                    break;
                case "/":
                    result = arg1.divide(arg2, 24, BigDecimal.ROUND_HALF_UP);
                    break;
            }
            if (result.toString().contains(".99999999999999999999999") || result.toString().contains(".00000000000000000000000"))
                result = result.setScale(0, BigDecimal.ROUND_HALF_UP);
            variables.put(variable, result);
        }

        return result.stripTrailingZeros().toPlainString();
    }
}
