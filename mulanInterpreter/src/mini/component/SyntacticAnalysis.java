package mini.component;

import mini.entity.Node;
import mini.entity.Token;

import java.util.ArrayList;

public class SyntacticAnalysis {

    private ArrayList<Token> tokens;
    private Node AbstractSyntaxTree;
    private int index;

    private boolean sentenceError;

    //Start
    public Node Parse(ArrayList<Token> tokens){
        index = 0;
        this.tokens = tokens;
        System.out.println("\n-------------------------------------------------------------------\n");
        AbstractSyntaxTree = new Node("root");

        //Node score = parseScore();
        while(tokens.get(index)!=null&&tokens.get(index).getSyn()==2){
            Node paragraph = parseParagraph();
            AbstractSyntaxTree.addChild(paragraph);
        }



        if(tokens.get(index)!=null){
            Node execution = parseExecution();
            AbstractSyntaxTree.addChild(execution);
        }

        //...


        return AbstractSyntaxTree;
    }

    //paragraph -> 'paragraph' identifier speed tone { sentence } 'end'
    public Node parseParagraph(){
        Node paragraph = new Node("paragraph");
        Node terminalNode;

        //'paragraph',因为遇到'paragraph'才进入此函数，所以第一个不需要判断
        terminalNode = new Node("paragraph","paragraph");
        paragraph.addChild(terminalNode);
        index++;

        //identifier(段落名)
        if(tokens.get(index).getSyn()!=100){
            nextLine();
            paragraph.addChild(new Node("Error", "缺少标识符"));
        }else{
            terminalNode = new Node("identifier",tokens.get(index).getContent());
            paragraph.addChild(terminalNode);
            index++;
        }

        //speed & tone
        Node speed,tone;
        switch(tokens.get(index).getSyn()){
            case 3:
                speed=parseSpeed();
                if(tokens.get(index).getSyn()!=4)
                    tone=getTone();
                else
                    tone=parseTone();
                break;
            case 4:
                tone=parseTone();
                if(tokens.get(index).getSyn()!=3)
                    speed=getSpeed();
                else
                    speed=parseSpeed();
                break;
            default:
                speed=getSpeed();
                tone=getTone();
        }

        paragraph.addChild(speed);
        paragraph.addChild(tone);

        //{ sentence }
        while(tokens.get(index).getSyn()!=5){
            //没遇到end就遇到play或paragraph
            if(tokens.get(index).getSyn()==6|tokens.get(index).getSyn()==2){
                paragraph.addChild(new Node("Error","缺少end标识"));
                return paragraph;
            }

            sentenceError = false;

            Node sentence = parseSentence();
            paragraph.addChild(sentence);
        }

        //'end',因为上一步sentence判断遇到end才会跳出，所以这一步肯定是end
        terminalNode = new Node("end paragraph","end");
        paragraph.addChild(terminalNode);
        index++;


        return paragraph;
    }

    //speed -> 'speed=' speedNum
    public Node parseSpeed(){
        Node speed = new Node("speed");
        Node terminalNode;

        //'speed='
        terminalNode = new Node("speed mark","speed=");
        speed.addChild(terminalNode);
        index++;

        //speed value
        if(tokens.get(index).getSyn()!=96){
            nextLine();
            return new Node("Error", "速度常数不正确");
        }
        terminalNode = new Node("speed value",tokens.get(index).getContent());
        speed.addChild(terminalNode);
        index++;


        return speed;
    }

    //tone -> ([#|b] toneValue)|toneValue
    public Node parseTone(){
        Node tone = new Node("tone");
        Node terminalNode;

        //'1='
        terminalNode = new Node("tone mark","1=");
        tone.addChild(terminalNode);
        index++;

        //#|b
        if(tokens.get(index).getSyn()==18 | tokens.get(index).getSyn()==19){
            terminalNode = new Node("lift mark",tokens.get(index).getContent());
            tone.addChild(terminalNode);
            index++;
        }

        //tone value
        if(tokens.get(index).getSyn()!=95){
            nextLine();
            return new Node("Error", "调号不正确");
        }
        terminalNode = new Node("tone value",tokens.get(index).getContent());
        tone.addChild(terminalNode);
        index++;

        return tone;
    }

    //sentence -> melody rhythm
    public Node parseSentence(){
        Node sentence = new Node("sentence");

        //melody
        Node melody = parseMelody();
        sentence.addChild(melody);
        if(sentenceError){
            return sentence;
        }

        //rhythm
        Node rhythm = parseRhythm();
        sentence.addChild(rhythm);

        return sentence;
    }

    //melody -> { NotesInEight }
    public Node parseMelody(){
        Node melody = new Node("melody");

        while(tokens.get(index).getSyn()!=13){
            if(sentenceError)
                break;
            Node notesInEight = parseNotesInEight();
            melody.addChild(notesInEight);
        }

        return melody;
    }

    //NotesInEight -> '(' Notes ')' | '[' Notes ']' | Notes
    public Node parseNotesInEight(){
        Node notesInEight = new Node("NotesInEight");

        switch (tokens.get(index).getSyn()){
            case 7:
                notesInEight.addChild(new Node("lower left parentheses","("));
                index++;

                notesInEight.addChild(parseNotes());
                if(sentenceError)
                    return  notesInEight;

                if(tokens.get(index).getSyn()!=8){
                    nextLine();
                    sentenceError = true;
                    return new Node("Error","缺少右括号");
                }
                notesInEight.addChild(new Node("lower right parentheses",")"));
                index++;
                break;
            case 9:
                notesInEight.addChild(new Node("lower left parentheses","["));
                index++;

                notesInEight.addChild(parseNotes());
                if(sentenceError)
                    return  notesInEight;

                if(tokens.get(index).getSyn()!=10){
                    nextLine();
                    sentenceError = true;
                    return new Node("Error","缺少右括号");
                }
                notesInEight.addChild(new Node("lower right parentheses","]"));
                index++;
                break;
            default:
                notesInEight.addChild(parseNotes());
                //if(sentenceError)
                //    return  notesInEight;
        }

        return notesInEight;
    }

    //Notes -> ([#|b] notesValue) | notesValue | 0
    public Node parseNotes(){
        Node notes = new Node("Notes");
        Node terminalNode;

        //'0',休止符
        if(tokens.get(index).getSyn()==94){
            terminalNode = new Node("Rest","0");
            notes.addChild(terminalNode);
            index++;
            return notes;
        }

        //#|b
        if(tokens.get(index).getSyn()==18 | tokens.get(index).getSyn()==19){
            terminalNode = new Node("lift mark",tokens.get(index).getContent());
            notes.addChild(terminalNode);
            index++;
        }

        //notesValue
        if(tokens.get(index).getSyn()!=98){
            nextLine();
            sentenceError = true;
            return new Node("Error", "音符不正确");
        }
        terminalNode = new Node("notes value",tokens.get(index).getContent());
        notes.addChild(terminalNode);
        index++;

        return notes;
    }

    //rhythm -> '<' length '>'
    public Node parseRhythm(){
        Node rhythm = new Node("Rhythm");
        Node terminalNode;

        //'<'
        if(tokens.get(index).getSyn()!=13){
            nextLine();
            sentenceError = true;
            return new Node("Error","缺少节奏");
        }
        terminalNode = new Node("left Angle brackets","<");
        rhythm.addChild(terminalNode);
        index++;

        //length
        boolean inCurlyBraces = false;
        while(tokens.get(index).getSyn()!=14){

            //'{'，连音左括号
            if(tokens.get(index).getSyn()==11){
                if(inCurlyBraces){
                    nextLine();
                    sentenceError = true;
                    return new Node("Error","连音括号中出现连音括号");
                }
                inCurlyBraces=true;
                terminalNode = new Node("leftCurlyBrace","{");
                rhythm.addChild(terminalNode);
                index++;
            }

            //'}',连音右括号
            if(tokens.get(index).getSyn()==12){
                if(!inCurlyBraces){
                    nextLine();
                    sentenceError = true;
                    return new Node("Error","缺少连音左括号");
                }
                inCurlyBraces=false;
                terminalNode = new Node("rightCurlyBrace","}");
                rhythm.addChild(terminalNode);
                index++;
            }

            //音符长度
            if(tokens.get(index).getSyn()!=99){
                nextLine();
                sentenceError = true;
                return new Node("Error","节奏格式错误");
            }
            terminalNode = new Node("length",tokens.get(index).getContent());
            rhythm.addChild(terminalNode);
            index++;

            //附点
            if(tokens.get(index).getSyn()==15){
                terminalNode = new Node("Dot.","*");
                rhythm.addChild(terminalNode);
                index++;
            }
        }


        //'>'
        if(tokens.get(index).getSyn()!=14){
            nextLine();
            sentenceError = true;
            return new Node("Error","缺少右尖括号");
        }
        terminalNode = new Node("left Angle brackets",">");
        rhythm.addChild(terminalNode);
        index++;

        return rhythm;
    }


    //get default speed if never set
    public Node getSpeed(){
        Node speed = new Node("speed");
        Node terminalNode;

        terminalNode = new Node("speed mark","speed=");
        speed.addChild(terminalNode);
        terminalNode = new Node("speed value","90");
        speed.addChild(terminalNode);

        return speed;
    }

    //get default tone if never set
    public Node getTone(){
        Node tone = new Node("tone");
        Node terminalNode;

        terminalNode = new Node("tone mark","1=");
        tone.addChild(terminalNode);
        terminalNode = new Node("tone value","C");
        tone.addChild(terminalNode);

        return tone;
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
            return new Node("Error", "缺少左小括号");
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
            return new Node("Error", "缺少右小括号");
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
