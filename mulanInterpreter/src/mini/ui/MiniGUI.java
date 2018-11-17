/*
 * Created by Chief on Thu Nov 15 10:05:17 CST 2018
 */

package mini.ui;

import mini.component.LexicalAnalysis;
import mini.component.SemanticAnalysis;
import mini.component.SyntacticAnalysis;
import mini.entity.Token;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chief
 */
public class MiniGUI extends JFrame {

    private File file;
    private boolean hasSaved = false;
    private boolean hasChanged=false;
    private boolean ctrlPressed=false;
    private boolean sPressed=false;
    private SimpleAttributeSet attributeSet;
    private SimpleAttributeSet durationAttributeSet;
    private SimpleAttributeSet normalAttributeSet;
    private SimpleAttributeSet commentAttributeSet;
    private StyledDocument inputStyledDocument;
    //todo 输出内容高亮
    private StyledDocument outputStyledDocument;
    private Pattern keywordPattern;
    private Pattern parenPattern;

    private LexicalAnalysis lexicalAnalysis;
    private SyntacticAnalysis syntacticAnalysis;
    private SemanticAnalysis semanticAnalysis;

    public MiniGUI() {
        initComponents();
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //样式
        attributeSet = new SimpleAttributeSet();
        durationAttributeSet = new SimpleAttributeSet();
        normalAttributeSet = new SimpleAttributeSet();
        commentAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attributeSet, new Color(30, 80, 180));
        StyleConstants.setBold(attributeSet, true);
        StyleConstants.setForeground(durationAttributeSet, new Color(54, 163, 240));
        StyleConstants.setForeground(commentAttributeSet, new Color(128, 128, 128));
        inputStyledDocument = inputTextPane.getStyledDocument();
        outputStyledDocument = outputTextPane.getStyledDocument();
        keywordPattern = Pattern.compile("\\bparagraph\\b|\\bspeed=|\\b1=|\\bend\\b|\\bplay");
        parenPattern = Pattern.compile("<(\\s*\\{?\\s*(1|2|4|8|g|\\*)+\\s*\\}?\\s*)+>");

        //关闭窗口提示
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (showSaveComfirm("Exist unsaved content, save before exit?"))
                    System.exit(0);
            }
        });

        //着色与补全的监听
        inputTextPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP ||
                        e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_LEFT ||
                        e.getKeyCode() == KeyEvent.VK_RIGHT ||
                        e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_SHIFT ||
                        e.getKeyCode() == KeyEvent.VK_ALT)
                    return;

                if(e.getKeyCode()==KeyEvent.VK_CONTROL){
                    ctrlPressed=false;
                    return;
                }

                if(e.getKeyCode()==KeyEvent.VK_S) {
                    sPressed = false;
                    return;
                }

                autoComplete();
                refreshColor();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    autoRemove();
                    refreshColor();
                }

                if(e.getKeyCode()==KeyEvent.VK_CONTROL)
                    ctrlPressed=true;

                if(e.getKeyCode()==KeyEvent.VK_S)
                    sPressed=true;

                if(ctrlPressed&&sPressed) {
                    sPressed = false;
                    ctrlPressed = false;
                    saveMenuItemActionPerformed(null);
                }
            }
        });

        //是否有改动的监听
        inputTextPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                contentChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                contentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                contentChanged();
            }
        });

        //组件实例化
        lexicalAnalysis = new LexicalAnalysis();
        syntacticAnalysis = new SyntacticAnalysis();
        semanticAnalysis = new SemanticAnalysis();

        //行号与滚动条
        scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        String lineStr = "";
        for (int i = 1; i < 1000; i++)
            lineStr += i + "\n";
        lineTextArea.setText(lineStr);
        scrollPane1.getVerticalScrollBar().addAdjustmentListener(e -> scrollPane3.getVerticalScrollBar().setValue(scrollPane1.getVerticalScrollBar().getValue()));
    }

    //内容变动调用的函数
    private void contentChanged(){
        if(hasChanged)
            return;

        hasChanged=true;
        if(this.getTitle().lastIndexOf("(Unsaved)")==-1)
            this.setTitle(this.getTitle()+" (Unsaved)");
    }

    //内容变动之后是否保存
    private boolean showSaveComfirm(String confirm){
        if (hasChanged) {
            int exit = JOptionPane.showConfirmDialog(null, confirm, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (exit) {
                case JOptionPane.YES_OPTION:
                    saveMenuItemActionPerformed(null);
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return false;
            }
        }
        return true;
    }

    //自动删除界符
    private void autoRemove() {
        StringBuilder input = new StringBuilder(inputTextPane.getText().replace("\r", ""));
        int pos = inputTextPane.getCaretPosition();
        if (input.length() > 1 && pos < input.length() && pos > 0) {
            if ((input.charAt(pos - 1) == '(' && input.charAt(pos) == ')') ||
                    (input.charAt(pos - 1) == '[' && input.charAt(pos) == ']') ||
                    (input.charAt(pos - 1) == '<' && input.charAt(pos) == '>') ||
                    (input.charAt(pos - 1) == '{' && input.charAt(pos) == '}')) {
                input.deleteCharAt(pos);
                inputTextPane.setText(input.toString());
                inputTextPane.setCaretPosition(pos);
                return;
            }
        }
    }

    //自动补全界符与注释符号
    private void autoComplete() {
        StringBuilder input = new StringBuilder(inputTextPane.getText().replace("\r", ""));
        int pos = inputTextPane.getCaretPosition();
        if (pos > 0) {
            if (pos < input.length() && (input.substring(pos, pos + 1).equals(" ")||input.substring(pos, pos + 1).equals("\n")) || pos == input.length())
                switch (input.charAt(pos - 1)) {
                    case '(':
                        input.insert(pos, ')');
                        inputTextPane.setText(input.toString());
                        inputTextPane.setCaretPosition(pos);
                        return;
                    case '[':
                        input.insert(pos, ']');
                        inputTextPane.setText(input.toString());
                        inputTextPane.setCaretPosition(pos);
                        return;
                    case '<':
                        input.insert(pos, '>');
                        inputTextPane.setText(input.toString());
                        inputTextPane.setCaretPosition(pos);
                        return;
                    case '{':
                        input.insert(pos, '}');
                        inputTextPane.setText(input.toString());
                        inputTextPane.setCaretPosition(pos);
                        return;
                    case '*':
                        if (input.charAt(pos - 2) == '/') {
                            input.insert(inputTextPane.getCaretPosition(), "\n\n*/");
                            inputTextPane.setText(input.toString());
                            inputTextPane.setCaretPosition(pos + 1);
                        }
                        return;
                }
        }
    }

    //代码着色
    private void refreshColor() {
        String input = inputTextPane.getText().replace("\r", "");

        inputStyledDocument.setCharacterAttributes(
                0,
                input.length(),
                normalAttributeSet, true
        );

        //关键字着色
        Matcher inputMatcher = keywordPattern.matcher(input);
        while (inputMatcher.find()) {
            inputStyledDocument.setCharacterAttributes(
                    inputMatcher.start(),
                    inputMatcher.end() - inputMatcher.start(),
                    attributeSet, true
            );
        }

        //节奏片段着色
        Matcher parenMatcher = parenPattern.matcher(input);
        while (parenMatcher.find()) {
            inputStyledDocument.setCharacterAttributes(
                    parenMatcher.start(),
                    parenMatcher.end() - parenMatcher.start(),
                    durationAttributeSet, true
            );
        }

        //注释着色
        for (int i = 0; i < input.length(); i++) {
            //单行注释
            if (i + 1 < input.length())
                if (input.charAt(i) == '/' && input.charAt(i + 1) == '/')
                    while (i + 1 < input.length() && input.charAt(i) != '\n') {
                        i++;
                        inputStyledDocument.setCharacterAttributes(
                                i - 1,
                                2,
                                commentAttributeSet, true
                        );
                    }

            //多行注释
            if (i + 1 < input.length() && input.charAt(i) == '/' && input.charAt(i + 1) == '*')
                while (i + 1 < input.length() && (input.charAt(i) != '*' || input.charAt(i + 1) != '/')) {
                    i++;
                    inputStyledDocument.setCharacterAttributes(
                            i - 1,
                            3,
                            commentAttributeSet, true
                    );
                }
        }
    }

    //新建文件
    private void newMenuItemActionPerformed(ActionEvent e) {
        if(showSaveComfirm("Exist unsaved content, save before new file?")) {
            hasSaved = false;
            inputTextPane.setText("");
            outputTextPane.setText("");
            hasChanged = false;
            this.setTitle("Music Interpreter - New File");
        }
    }

    //打开文件
    private void openMenuItemActionPerformed(ActionEvent e) {
        if (!showSaveComfirm("Exist unsaved content, save before open fire?"))
            return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Music Interpreter File", "mui");
        fileChooser.setFileFilter(filter);
        fileChooser.showOpenDialog(this);
        file = fileChooser.getSelectedFile();
        if (file == null)
            return;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content);
                stringBuilder.append(System.getProperty("line.separator"));
            }
            bufferedReader.close();
            inputTextPane.setText(stringBuilder.toString());
            outputTextPane.setText("");
            refreshColor();
            hasSaved = true;
            hasChanged = false;
            this.setTitle("Music Interpreter - "+file.getName());
        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
        } catch (IOException e1) {
//            e1.printStackTrace();
        }
    }

    //保存文件
    private void saveMenuItemActionPerformed(ActionEvent e) {
        if (!hasSaved) {
            saveAsMenuItemActionPerformed(null);
        } else {
            try {
                if (!file.exists())
                    file.createNewFile();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                bufferedWriter.write(inputTextPane.getText());
                bufferedWriter.close();
                hasChanged = false;
                this.setTitle("Music Interpreter - " + file.getName());
            } catch (IOException e1) {
//                e1.printStackTrace();
            }
        }
    }

    //另存为文件
    private void saveAsMenuItemActionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Music Interpreter File", "mui");
        fileChooser.setFileFilter(filter);
        fileChooser.showSaveDialog(this);
        if (fileChooser.getSelectedFile() == null)
            return;
        String fileStr = fileChooser.getSelectedFile().getAbsoluteFile().toString();
        if (fileStr.lastIndexOf(".mui") == -1)
            fileStr += ".mui";
        file = new File(fileStr);
        try {
            if (!file.exists())
                file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bufferedWriter.write(inputTextPane.getText());
            bufferedWriter.close();
            hasSaved = true;
            hasChanged = false;
            this.setTitle("Music Interpreter - " + file.getName());
        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
        } catch (IOException e1) {
//            e1.printStackTrace();
        }
    }

    //执行词法分析
    private void LexMenuItemActionPerformed(ActionEvent e) {
        StringBuilder stringBuilder = new StringBuilder();
        lexicalAnalysis.Lex(inputTextPane.getText());
        if (lexicalAnalysis.getError())
            stringBuilder.append("检测到词法错误:\n");
        for (Token token : lexicalAnalysis.getTokens()) {
            stringBuilder.append(token);
        }
        outputTextPane.setText(stringBuilder.toString());
    }

    //执行语法分析
    private void synMenuItemActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    //执行语义分析
    private void semMenuItemActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    //保存执行文件
    private void buildMenuItemActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    //关于
    private void aboutMenuItemActionPerformed(ActionEvent e) {
        String str = "-----------------------------------------------------------\n" +
                "Music Language Interpreter\nMade By Chief, yzdxm and AsrielMao\nVersion: 0.0.1\n\n" +
                "A light weight interpreter for converting digit score       \n" +
                "to Arduino code\n" +
                "-----------------------------------------------------------";
        JOptionPane.showMessageDialog(this, str, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    //展示Demo
    private void demoMenuItemActionPerformed(ActionEvent e) {
        if(!showSaveComfirm("Exist unsaved content, save before open the demo?"))
            return;

        String str="/*\n" +
                " 欢乐颂\n" +
                " 女高音 + 女中音\n" +
                " 双声部 Version\n" +
                " */\n" +
                "\n" +
                "//女高音\n" +
                "paragraph soprano\n" +
                "speed= 100\n" +
                "1= D\n" +
                "3345 5432 <4444 4444>\n" +
                "1123 322 <4444 4*82>\n" +
                "3345 5432 <4444 4444>\n" +
                "1123 211 <4444 4*82>\n" +
                "2231 23431 <4444 4{88}44>\n" +
                "23432 12(5) <4{88}44 {44}4>\n" +
                "33345 54342 <{44}444 44{48}8>\n" +
                "1123 211 <4444 4*82>\n" +
                "end\n" +
                "\n" +
                "//女中音\n" +
                "paragraph alto\n" +
                "speed= 100\n" +
                "1= D\n" +
                "1123 321(5) <4444 4444>\n" +
                "(3555) 1(77) <4444 4*82>\n" +
                "1123 321(5) <4444 4444>\n" +
                "(3555) (533) <4444 4*82>\n" +
                "(77)1(5) (77)1(5) <4444 4444>\n" +
                "(7#5#5#56#45) <4444 {44}4>\n" +
                "11123 3211(5) <{44}444 44{48}8>\n" +
                "(3555 533) <4444 4*82>\n" +
                "end\n" +
                "\n" +
                "//双声部同时播放\n" +
                "play(melody&bass)";
        inputTextPane.setText(str);
        refreshColor();
        hasChanged=false;
        this.setTitle("Music Interpreter - Demo (Unsaved)");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        fileMenu = new JMenu();
        newMenuItem = new JMenuItem();
        openMenuItem = new JMenuItem();
        saveMenuItem = new JMenuItem();
        saveAsMenuItem = new JMenuItem();
        runMenu = new JMenu();
        LexMenuItem = new JMenuItem();
        synMenuItem = new JMenuItem();
        semMenuItem = new JMenuItem();
        buildMenu = new JMenu();
        buildMenuItem = new JMenuItem();
        helpMenu = new JMenu();
        demoMenuItem = new JMenuItem();
        aboutMenuItem = new JMenuItem();
        panel1 = new JPanel();
        scrollPane3 = new JScrollPane();
        lineTextArea = new JTextArea();
        scrollPane1 = new JScrollPane();
        inputTextPane = new JTextPane();
        scrollPane2 = new JScrollPane();
        outputTextPane = new JTextPane();

        //======== this ========
        setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        setTitle("Music Interpreter - New File");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout());

        //======== menuBar1 ========
        {

            //======== fileMenu ========
            {
                fileMenu.setText("File");

                //---- newMenuItem ----
                newMenuItem.setText("New");
                newMenuItem.addActionListener(e -> newMenuItemActionPerformed(e));
                fileMenu.add(newMenuItem);

                //---- openMenuItem ----
                openMenuItem.setText("Open");
                openMenuItem.addActionListener(e -> openMenuItemActionPerformed(e));
                fileMenu.add(openMenuItem);

                //---- saveMenuItem ----
                saveMenuItem.setText("Save");
                saveMenuItem.addActionListener(e -> saveMenuItemActionPerformed(e));
                fileMenu.add(saveMenuItem);

                //---- saveAsMenuItem ----
                saveAsMenuItem.setText("Save As...");
                saveAsMenuItem.addActionListener(e -> saveAsMenuItemActionPerformed(e));
                fileMenu.add(saveAsMenuItem);
            }
            menuBar1.add(fileMenu);

            //======== runMenu ========
            {
                runMenu.setText("Run");

                //---- LexMenuItem ----
                LexMenuItem.setText("Lexical Analysis");
                LexMenuItem.addActionListener(e -> LexMenuItemActionPerformed(e));
                runMenu.add(LexMenuItem);

                //---- synMenuItem ----
                synMenuItem.setText("Syntactic Analysis");
                synMenuItem.addActionListener(e -> synMenuItemActionPerformed(e));
                runMenu.add(synMenuItem);

                //---- semMenuItem ----
                semMenuItem.setText("Semantic Analysis");
                semMenuItem.addActionListener(e -> semMenuItemActionPerformed(e));
                runMenu.add(semMenuItem);
            }
            menuBar1.add(runMenu);

            //======== buildMenu ========
            {
                buildMenu.setText("Build");

                //---- buildMenuItem ----
                buildMenuItem.setText("Generate .ino file");
                buildMenuItem.addActionListener(e -> buildMenuItemActionPerformed(e));
                buildMenu.add(buildMenuItem);
            }
            menuBar1.add(buildMenu);

            //======== helpMenu ========
            {
                helpMenu.setText("Help");

                //---- demoMenuItem ----
                demoMenuItem.setText("Demo");
                demoMenuItem.addActionListener(e -> demoMenuItemActionPerformed(e));
                helpMenu.add(demoMenuItem);

                //---- aboutMenuItem ----
                aboutMenuItem.setText("About");
                aboutMenuItem.addActionListener(e -> aboutMenuItemActionPerformed(e));
                helpMenu.add(aboutMenuItem);
            }
            menuBar1.add(helpMenu);
        }
        setJMenuBar(menuBar1);

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[fill]0" +
                "[fill]0" +
                "[fill]",
                // rows
                "[fill]"));

            //======== scrollPane3 ========
            {

                //---- lineTextArea ----
                lineTextArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
                lineTextArea.setEnabled(false);
                lineTextArea.setEditable(false);
                lineTextArea.setBorder(null);
                lineTextArea.setBackground(Color.white);
                lineTextArea.setForeground(new Color(153, 153, 153));
                scrollPane3.setViewportView(lineTextArea);
            }
            panel1.add(scrollPane3, "cell 0 0,width 40:40:40");

            //======== scrollPane1 ========
            {

                //---- inputTextPane ----
                inputTextPane.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
                inputTextPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                inputTextPane.setBorder(null);
                scrollPane1.setViewportView(inputTextPane);
            }
            panel1.add(scrollPane1, "cell 1 0,width 400:400:400,height 600:600:600");

            //======== scrollPane2 ========
            {

                //---- outputTextPane ----
                outputTextPane.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
                outputTextPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                outputTextPane.setBorder(null);
                scrollPane2.setViewportView(outputTextPane);
            }
            panel1.add(scrollPane2, "cell 2 0,width 400:400:400,height 600:600:600");
        }
        contentPane.add(panel1);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu fileMenu;
    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenu runMenu;
    private JMenuItem LexMenuItem;
    private JMenuItem synMenuItem;
    private JMenuItem semMenuItem;
    private JMenu buildMenu;
    private JMenuItem buildMenuItem;
    private JMenu helpMenu;
    private JMenuItem demoMenuItem;
    private JMenuItem aboutMenuItem;
    private JPanel panel1;
    private JScrollPane scrollPane3;
    private JTextArea lineTextArea;
    private JScrollPane scrollPane1;
    private JTextPane inputTextPane;
    private JScrollPane scrollPane2;
    private JTextPane outputTextPane;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}