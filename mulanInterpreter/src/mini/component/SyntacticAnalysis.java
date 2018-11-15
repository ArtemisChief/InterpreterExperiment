package mini.component;

import mini.entity.Node;
import mini.entity.Token;

import java.util.ArrayList;

public class SyntacticAnalysis {

    private ArrayList<Token> tokens;
    private Node AbstractSyntaxTree;
    private int index;

    //Start
    public Node Parse(ArrayList<Token> tokens){
        index = 0;
        this.tokens = tokens;
        System.out.println("\n-------------------------------------------------------------------\n");
        AbstractSyntaxTree = new Node("root");

        //Node score = parseScore();
        Node execution = parseExecution();

        //...


        return AbstractSyntaxTree;
    }

    //execution -> play ( playlist )
    public Node parseExecution(){
        Node execution = new Node("execution");
        Node terminalNode;

        //play
        if(tokens.get(index).getSyn()!=6){
            nextLine();
            return new Node("Error", "Syntax error");
        }
        terminalNode = new Node("play","play");
        execution.addChild(terminalNode);
        index++;

        //leftParentheses,(
        if(tokens.get(index).getSyn()!=7){
            nextLine();
            return new Node("Error", "缺少左括号");
        }
        terminalNode = new Node("leftParentheses","(");
        execution.addChild(terminalNode);
        index++;

        //playlist
        Node playlist = parsePlayList();
        execution.addChild(playlist);

        //rightParentheses,(
        if(tokens.get(index).getSyn()!=8){
            nextLine();
            return new Node("Error", "缺少右括号");
        }
        terminalNode = new Node("rightParentheses",")");
        execution.addChild(terminalNode);
        index++;


        return execution;
    }

    //playlist -> identifier { [&|,] identifier }
    public Node parsePlayList(){
        Node playlist = new Node("playlist");
        Node terminalNode;

        //identifier(段落名)
        if(tokens.get(index).getSyn()!=100){
            nextLine();
            return new Node("Error", "Syntax error");
        }
        terminalNode = new Node("identifier",tokens.get(index).getContent());
        playlist.addChild(terminalNode);
        index++;

        while(tokens.get(index).getSyn()!=8){
            // "&" or ","
            switch (tokens.get(index).getSyn()){
                case 16:
                    terminalNode = new Node("comma",",");
                    break;
                case 17:
                    terminalNode = new Node("and","&");
                    break;
                default:
                    return new Node("Error", "缺少逗号或&符号");
            }
            playlist.addChild(terminalNode);
            index++;

            ///identifier(段落名)
            if(tokens.get(index).getSyn()!=100){
                nextLine();
                return new Node("Error", "Syntax error");
            }
            terminalNode = new Node("identifier",tokens.get(index).getContent());
            playlist.addChild(terminalNode);
            index++;
        }


        return playlist;
    }


    //换到下一行
    public void nextLine(){

        while(tokens.get(index).getCount()==tokens.get(++index).getCount()){

        }
    }

}
