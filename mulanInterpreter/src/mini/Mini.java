package mini;

import java.io.*;
import java.util.ArrayList;

public class Mini {

    public static void main(String[] args) {

        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
        String pathname ="D:\\test.txt";
        try{
            File file = new File(pathname);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
                lexicalAnalysis.Lex(lineTxt);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Token> tokens=lexicalAnalysis.getTokens();
        for (Token token:tokens) {
            System.out.println(token.toString());
        }
    }
}
