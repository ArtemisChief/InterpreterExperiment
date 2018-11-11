package calculator.entity;

public class Quadruple {
    private String op;
    private String arg1;
    private String arg2;
    private String result;

    public Quadruple(String op,String arg1,String arg2,String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public String toString() {
        return String.format("(%s, %s, %s, %s)\n", op, arg1, arg2, result);
    }

    public String getOp(){
        return op;
    }

    public String getArg1(){
        return arg1;
    }

    public String getArg2(){
        return arg2;
    }

    public String getResult(){
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
