package mini.component;

import mini.entity.Node;
import mini.entity.Token;

import java.util.ArrayList;

public class SyntacticAnalysis {

    private ArrayList<Token> tokens;
    private Node AbstractSyntaxTree;
    private int index;
    //private boolean isError;
    private boolean sentenceError;
    private ArrayList<Integer> errorList;

    //Start
    public Node Parse(ArrayList<Token> tokens) {
        index = 0;
        //isError = false;
        errorList = new ArrayList<>();
        this.tokens = tokens;
        AbstractSyntaxTree = new Node("root");

        //Node score = parseScore();
        while (tokens.get(index) != null && tokens.get(index).getSyn() == 2) {
            Node paragraph = parseParagraph();
            AbstractSyntaxTree.addChild(paragraph);
        }


        if (tokens.get(index) != null) {
            Node execution = parseExecution();
            AbstractSyntaxTree.addChild(execution);
        }

        return AbstractSyntaxTree;
    }

    //paragraph -> 'paragraph' identifier speed tone { sentence } 'end'
    public Node parseParagraph() {
        Node paragraph = new Node("score");
        Node terminalNode;

        //'paragraph',因为遇到'paragraph'才进入此函数，所以第一个不需要判断
        Node statement = new Node("statement");
//        terminalNode = new Node("paragraph","paragraph",tokens.get(index).getCount());
//        statement.addChild(terminalNode);
        index++;


        //identifier(段落名)
        if (tokens.get(index).getSyn() != 100) {
            int syn=tokens.get(index).getSyn();
            if(syn!=3&&syn!=4&&syn!=7&&syn!=9&&syn!=98)
                nextLine();
            statement.addChild(new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少标识符"));
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
        } else {
            terminalNode = new Node("identifier", tokens.get(index).getContent(),tokens.get(index).getCount());
            statement.addChild(terminalNode);
            index++;
        }
        paragraph.addChild(statement);

        int tempSyn = tokens.get(index).getSyn();
        boolean hadSpeed = false, hadTone = false, hadInstrument = false, hadVolume = false;
        while(tempSyn==3|tempSyn==4|tempSyn==20|tempSyn==21){
            switch(tempSyn){
                case 3:
                    //speed
                    Node speed = parseSpeed();
                    paragraph.addChild(speed);
                    hadSpeed = true;
                    break;
                case 4:
                    //tone
                    Node tone = parseTone();
                    paragraph.addChild(tone);
                    hadTone = true;
                    break;
                case 20:
                    //instrument
                    Node instrument = parseInstrument();
                    paragraph.addChild(instrument);
                    hadInstrument = true;
                    break;
                case 21:
                    //volume
                    Node volume = parseVolume();
                    paragraph.addChild(volume);
                    hadVolume = true;
                    break;
                default:
                    break;
            }

            tempSyn = tokens.get(index).getSyn();
        }
        if(!hadSpeed){
            Node speed = parseSpeed();
            paragraph.addChild(speed);
        }
        if(!hadTone){
            Node tone = parseTone();
            paragraph.addChild(tone);
        }
        if(!hadInstrument){
            Node instrument = parseInstrument();
            paragraph.addChild(instrument);
        }
        if(!hadVolume){
            Node volume = parseVolume();
            paragraph.addChild(volume);
        }






        //{ sentence }
        while (tokens.get(index).getSyn() != 5) {
            //没遇到end就遇到play或paragraph
            if (tokens.get(index).getSyn() == 6 | tokens.get(index).getSyn() == 2) {
                paragraph.addChild(new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少end标识"));
                //isError = true;
                errorList.add(tokens.get(index - 1).getCount());
                return paragraph;
            }

            sentenceError = false;

            Node sentence = parseSentence();
            paragraph.addChild(sentence);
        }

        //'end',因为上一步sentence判断遇到end才会跳出，所以这一步肯定是end
        terminalNode = new Node("end paragraph", "end",tokens.get(index).getCount());
        paragraph.addChild(terminalNode);
        index++;

        return paragraph;
    }

    //instument
    public Node parseInstrument(){
        Node instrument = new Node("instrument");
        Node terminalNode;
        if(tokens.get(index).getSyn()!=20){
//            //instrument=
//            terminalNode = new Node("instrumentMark","instrument=");
//            instrument.addChild((terminalNode));
            //乐器编号
            terminalNode = new Node("instrumentValue","0");
            instrument.addChild(terminalNode);

            return instrument;
        }

//        //instrument=
//        terminalNode = new Node("instrumentMark","instrument=");
//        instrument.addChild((terminalNode));
        index++;
        //乐器编号
        terminalNode = new Node("instrumentValue",tokens.get(index).getContent());
        instrument.addChild(terminalNode);
        index++;

        return instrument;
    }

    public Node parseVolume(){
        Node volume = new Node("volume");
        Node terminalNode;
        if(tokens.get(index).getSyn()!=21){
//            //volume=
//            terminalNode = new Node("volumeMark","volume=");
//            volume.addChild((terminalNode));
            //乐器编号
            terminalNode = new Node("volumeValue","127");
            volume.addChild(terminalNode);

            return volume;
        }

//        //volume=
//        terminalNode = new Node("volumeMark","volume=");
//        volume.addChild((terminalNode));
        index++;
        //乐器编号
        terminalNode = new Node("volumeValue",tokens.get(index).getContent());
        volume.addChild(terminalNode);
        index++;

        return volume;
    }



    //speed -> 'speed=' speedNum
    public Node parseSpeed() {
        Node speed = new Node("speed");
        Node terminalNode;
        if(tokens.get(index).getSyn()!=3){
//            //speed=
//            terminalNode = new Node("speedMark","speed=");
//            speed.addChild((terminalNode));
            //乐器编号
            terminalNode = new Node("speedValue","90");
            speed.addChild(terminalNode);

            return speed;
        }

//        //speed=
//        terminalNode = new Node("speedMark","speed=");
//        speed.addChild((terminalNode));
        index++;
        //乐器编号
        terminalNode = new Node("speedValue",tokens.get(index).getContent());
        speed.addChild(terminalNode);
        index++;

        return speed;
    }

    //tone -> ([#|b] toneValue)|toneValue
    public Node parseTone() {
        if(tokens.get(index).getSyn()!=4){
            return getTone();
        }
        Node tone = new Node("tonality");
        Node terminalNode;
        String tonality = "";
        //'1='
//        terminalNode = new Node("tone mark","1=",tokens.get(index).getCount());
//        tone.addChild(terminalNode);
        index++;

        //#|b
        if (tokens.get(index).getSyn() == 18 | tokens.get(index).getSyn() == 19) {
            tonality += tokens.get(index).getContent();
//            terminalNode = new Node("lift mark",tokens.get(index).getContent(),tokens.get(index).getCount());
//            tone.addChild(terminalNode);
            index++;
        }

        //tone value
        if (tokens.get(index).getSyn() != 95) {
            nextLine();
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  调号不正确");
        }
        tonality += tokens.get(index).getContent();
        terminalNode = new Node("tone value", tonality,tokens.get(index).getCount());
        tone.addChild(terminalNode);
//        terminalNode = new Node("tone value",tokens.get(index).getContent());
//        tone.addChild(terminalNode);
        index++;

        return tone;
    }

    //sentence -> melody rhythm
    public Node parseSentence() {
        Node sentence = new Node("sentence");

        //melody
        Node melody = parseMelody();
        sentence.addChild(melody);
        if (sentenceError) {
            return sentence;
        }

        //rhythm
        Node rhythm = parseRhythm();
        sentence.addChild(rhythm);

        return sentence;
    }

    //melody -> { NotesInEight }
    public Node parseMelody() {
        Node melody = new Node("melody");

        //一整句存在melody节点的content中
        //String notes = "";
        int group = 0;
        while (tokens.get(index).getSyn() != 13) {
            //'(',低八度左括号
            if (tokens.get(index).getSyn() == 7) {
                if (group > 0) {
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  八度转换错误");
                }
                group--;
//                notes += "(";
                melody.addChild(new Node("lower left parentheses","(",tokens.get(index).getCount()));
                index++;
                continue;
            }
            //')',低八度右括号
            if (tokens.get(index).getSyn() == 8) {
                if (group >= 0) {
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  八度转换错误");
                }
                if(tokens.get(index-1).getSyn()==7){
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  括号内不能为空");
                }
                group++;
//                notes += ")";
                melody.addChild(new Node("lower right parentheses",")",tokens.get(index).getCount()));
                index++;
                continue;
            }
            //'['，高八度左括号
            if (tokens.get(index).getSyn() == 9) {
                if (group < 0) {
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  八度转换错误");
                }
                group++;
//                notes += "[";
                melody.addChild(new Node("higher left parentheses","[",tokens.get(index).getCount()));
                index++;
                continue;
            }
            //']',高八度右括号
            if (tokens.get(index).getSyn() == 10) {
                if (group <= 0) {
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  八度转换错误");
                }
                if(tokens.get(index-1).getSyn()==9){
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  括号内不能为空");
                }
                group--;
//                notes += "]";
                melody.addChild(new Node("higher right parentheses","]",tokens.get(index).getCount()));
                index++;
                continue;
            }

            //音符
            Node note = parseNotes();
            if (sentenceError) {
                //isError = true;
                errorList.add(tokens.get(index - 1).getCount());
                return new Node("Error",note.getContent());
            }
//            notes += note.getContent();
            melody.addChild(note);

//            Node notesInEight = parseNotesInEight();
//            if(sentenceError){
//                melody = new Node("melody");
//                melody.addChild(notesInEight);
//                return melody;
//            }
//            for(Node childNode : notesInEight.getChildren()){
//                if(childNode.isTerminal()){
//                    notes += childNode.getContent();
//                    continue;
//                }
//                for(Node cNode : childNode.getChildren()){
//                    notes += cNode.getContent();
//                }
//
//            }

        }//end while
        if(group!=0){
            sentenceError = true;
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            nextLine();
            return new Node("Error","Line: " + (tokens.get(index-1).getCount()) +" 八度转换错误");
        }

//每个音符作为一个节点保存
//        while(tokens.get(index).getSyn()!=13){
//            if(sentenceError)
//                break;
//            Node notesInEight = parseNotesInEight();
//            melody.addChild(notesInEight);
//        }

        //melody.addChild(new Node("melodyValue", notes));

        return melody;
    }

    //NotesInEight -> '(' Notes ')' | '[' Notes ']' | Notes
    public Node parseNotesInEight() {
        Node notesInEight = new Node("NotesInEight");

        switch (tokens.get(index).getSyn()) {
            case 7:
                notesInEight.addChild(new Node("lower left parentheses", "(",tokens.get(index).getCount()));
                index++;

                notesInEight.addChild(parseNotes());
                if (sentenceError)
                    return notesInEight;

                if (tokens.get(index).getSyn() != 8) {
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少右括号");
                }
                notesInEight.addChild(new Node("lower right parentheses", ")",tokens.get(index).getCount()));
                index++;
                break;
            case 9:
                notesInEight.addChild(new Node("lower left parentheses", "[",tokens.get(index).getCount()));
                index++;

                notesInEight.addChild(parseNotes());
                if (sentenceError)
                    return notesInEight;

                if (tokens.get(index).getSyn() != 10) {
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少右括号");
                }
                notesInEight.addChild(new Node("lower right parentheses", "]",tokens.get(index).getCount()));
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
    public Node parseNotes() {
        Node notes;
        //String notesValue = "";

        //'0',休止符
        if (tokens.get(index).getSyn() == 94) {
            //terminalNode = new Node("Rest","0");
            //notes.addChild(terminalNode);
            notes = new Node("Notes", "0",tokens.get(index).getCount());
            index++;
            return notes;
        }

        //#|b
        if (tokens.get(index).getSyn() == 18 | tokens.get(index).getSyn() == 19) {
            notes = new Node("lift mark",tokens.get(index).getContent(),tokens.get(index).getCount());
            //notes.addChild(terminalNode);
            //notesValue += tokens.get(index).getContent();
            index++;

            return notes;
        }

        //notesValue
        if (tokens.get(index).getSyn() != 98) {
            nextLine();
            sentenceError = true;
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  音符不正确");
        }
        notes = new Node("notes value",tokens.get(index).getContent(),tokens.get(index).getCount());
        //notes.addChild(terminalNode);
        //notesValue += tokens.get(index).getContent();
        index++;

        //notes = new Node("Notes", notesValue);
        return notes;
    }

    //rhythm -> '<' length '>'
    public Node parseRhythm() {
        Node rhythm = new Node("rhythm");
        Node terminalNode;
        //String rhythmContent = "";

        //'<'
        if (tokens.get(index).getSyn() != 13) {
            nextLine();
            sentenceError = true;
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少节奏");
        }
        terminalNode = new Node("left Angle brackets","<",tokens.get(index).getCount());
        rhythm.addChild(terminalNode);
        index++;

        //length
        boolean inCurlyBraces = false;
        while (tokens.get(index).getSyn() != 14) {

            //'{'，连音左括号
            if (tokens.get(index).getSyn() == 11) {
                if (inCurlyBraces) {
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  连音括号中出现连音括号");
                }
                inCurlyBraces = true;
                terminalNode = new Node("leftCurlyBrace","{",tokens.get(index).getCount());
                rhythm.addChild(terminalNode);
                //rhythmContent += "{";
                index++;
                continue;
            }

            //'}',连音右括号
            if (tokens.get(index).getSyn() == 12) {
                if (!inCurlyBraces) {
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少连音左括号");
                }
                if(tokens.get(index-1).getSyn()==11){
                    nextLine();
                    sentenceError = true;
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  连音括号内不能为空");
                }
                inCurlyBraces = false;
                terminalNode = new Node("rightCurlyBrace","}",tokens.get(index).getCount());
                rhythm.addChild(terminalNode);
                //rhythmContent += "}";
                index++;
                continue;
            }

            //音符长度
            if (tokens.get(index).getSyn() != 99) {
                nextLine();
                sentenceError = true;
                //isError = true;
                errorList.add(tokens.get(index - 1).getCount());
                return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  节奏格式错误");
            }
            //terminalNode = new Node("length",tokens.get(index).getContent());
            //rhythm.addChild(terminalNode);
            //rhythmContent += tokens.get(index).getContent();
            String len = "";
            len += tokens.get(index).getContent();
            index++;

            //附点
            if (tokens.get(index).getSyn() == 15) {
                //terminalNode = new Node("Dot.","*");
                //rhythm.addChild(terminalNode);
                //rhythmContent += "*";
                len += "*";
                index++;
            }
            terminalNode = new Node("length",len,tokens.get(index).getCount());
            rhythm.addChild(terminalNode);
        }
        if(inCurlyBraces){
            sentenceError = true;
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            nextLine();
            return new Node("Error","Line: " + (tokens.get(index-1).getCount()) +" 连音符号错误");
        }


        //rhythm.addChild(new Node("rhythmValue", rhythmContent));


        //'>'
        if (tokens.get(index).getSyn() != 14) {
            nextLine();
            sentenceError = true;
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少右尖括号");
        }
        terminalNode = new Node("left Angle brackets",">",tokens.get(index).getCount());
        rhythm.addChild(terminalNode);
        index++;

        return rhythm;
    }

    //get default speed if never set
    public Node getSpeed() {
        Node speed = new Node("speed");
        Node terminalNode;

//        terminalNode = new Node("speed mark", "speed=",tokens.get(index).getCount());
//        speed.addChild(terminalNode);
        terminalNode = new Node("speed value", "90",tokens.get(index).getCount());
        speed.addChild(terminalNode);

        return speed;
    }

    //get default tone if never set
    public Node getTone() {
        Node tone = new Node("tonality");
        Node terminalNode;

//        terminalNode = new Node("tone mark","1=",tokens.get(index).getCount());
//        tone.addChild(terminalNode);
        terminalNode = new Node("tone value", "C",tokens.get(index).getCount());
        tone.addChild(terminalNode);

        return tone;
    }

    //execution -> play ( playlist )
    public Node parseExecution() {
        Node execution = new Node("execution");
        Node terminalNode;

        //play
        if (tokens.get(index).getSyn() != 6) {
            nextLine();
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少Play执行语句");
        }
//        terminalNode = new Node("play","play",tokens.get(index).getCount());
//        execution.addChild(terminalNode);
        index++;

        //leftParentheses,(
        if (tokens.get(index).getSyn() != 7) {
            nextLine();
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少左小括号");
        }
//        terminalNode = new Node("leftParentheses","(",tokens.get(index).getCount());
//        execution.addChild(terminalNode);
        index++;

        //playlist
        Node playlist = parsePlayList();
        execution.addChild(playlist);

        //rightParentheses,(
        if (tokens.get(index).getSyn() != 8) {
            nextLine();
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少右小括号");
        }
//        terminalNode = new Node("rightParentheses",")",tokens.get(index).getCount());
//        execution.addChild(terminalNode);
        index++;

        return execution;
    }

    //playlist -> identifier { [&|,] identifier }
    public Node parsePlayList() {
        Node playlist = new Node("playlist");
        Node terminalNode;

        //identifier(段落名)
        if (tokens.get(index).getSyn() != 100) {
            nextLine();
            //isError = true;
            errorList.add(tokens.get(index - 1).getCount());
            return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少标识符");
        }
        terminalNode = new Node("identifier", tokens.get(index).getContent(),tokens.get(index).getCount());
        playlist.addChild(terminalNode);
        index++;

        while (tokens.get(index).getSyn() != 8) {
            // "&" or ","
            switch (tokens.get(index).getSyn()) {
                case 16:
                    terminalNode = new Node("comma", ",",tokens.get(index).getCount());
                    break;
                case 17:
                    terminalNode = new Node("and", "&",tokens.get(index).getCount());
                    break;
                default:
                    //isError = true;
                    errorList.add(tokens.get(index - 1).getCount());
                    return new Node("Error", "Line: " + tokens.get(index - 1).getCount() +"  缺少逗号或&符号");
            }
            playlist.addChild(terminalNode);
            index++;

            ///identifier(段落名)
            if (tokens.get(index).getSyn() != 100) {
                nextLine();
                //isError = true;
                errorList.add(tokens.get(index - 1).getCount());
                return new Node("Error", "Line: " + tokens.get(index - 1).getCount() + "  缺少标识符");
            }
            terminalNode = new Node("identifier", tokens.get(index).getContent(),tokens.get(index).getCount());
            playlist.addChild(terminalNode);
            index++;
        }

        return playlist;
    }

    //换到下一行
    public void nextLine() {

        while (index<tokens.size()-1&&tokens.get(index).getCount() == tokens.get(++index).getCount()) {

        }
    }

    public String getErrors(Node curNode) {
        String errorsInfo = "";

        if (curNode.getType().equals("Error"))
            errorsInfo += curNode.getContent()+"\n";

        for (Node child : curNode.getChildren()) {
            errorsInfo += getErrors(child);
        }
        return errorsInfo;
    }

    public boolean getIsError(){
        return !errorList.isEmpty();
    }

    public ArrayList<Integer> getErrorList(){
        return errorList;
    }
}
