/*
 * Created by JFormDesigner on Thu Nov 15 10:05:17 CST 2018
 */

package mini.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import mini.utils.LineNumberHeaderView;
import net.miginfocom.swing.*;
import sun.plugin2.util.ColorUtil;

/**
 * @author Chief
 */
public class MiniGUI extends JFrame {

    private String fileStr;
    private SimpleAttributeSet attributeSet;
    private SimpleAttributeSet rhythmAttributeSet;
    private SimpleAttributeSet normalAttributeSet;
    private SimpleAttributeSet commentAttributeSet;
    private StyledDocument inputStyledDocument;
    //todo 输出内容高亮
    private StyledDocument outputStyledDocument;
    private Pattern keywordPattern=Pattern.compile("\\bparagraph\\b|\\bspeed=|\\b1=|\\bend\\b|\\bplay");
    private Pattern parenPattern=Pattern.compile("<(\\s*\\{?\\s*\\d+\\s*\\}?\\s*)+>");

    public MiniGUI() {
        initComponents();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        attributeSet = new SimpleAttributeSet();
        rhythmAttributeSet = new SimpleAttributeSet();
        normalAttributeSet = new SimpleAttributeSet();
        commentAttributeSet = new SimpleAttributeSet();

        inputStyledDocument = inputTextPane.getStyledDocument();
        outputStyledDocument = outputTextPane.getStyledDocument();

        StyleConstants.setForeground(attributeSet, new Color(30, 80, 180));
        StyleConstants.setBold(attributeSet, true);
        StyleConstants.setForeground(rhythmAttributeSet, new Color(54, 163, 240));
        StyleConstants.setForeground(commentAttributeSet, new Color(128, 128, 128));

        //代码着色
        inputTextPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
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
                            rhythmAttributeSet, true
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
        });
    }

    private void newMenuItemMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void openMenuItemMouseClicked(MouseEvent e) {
        FileDialog fileDialog=new FileDialog(this,"Open File");
        fileStr=fileDialog.getFile();
    }

    private void saveMenuItemMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void saveAsMenuItemMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void LexMenuItemMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void synMenuItemMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void semMenuItemMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void buildMenuItemMouseClicked(MouseEvent e) {
        // TODO add your code here
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
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        inputTextPane = new JTextPane();
        scrollPane2 = new JScrollPane();
        outputTextPane = new JTextPane();

        //======== this ========
        setResizable(false);
        setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        setTitle("Music Interpreter");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout());

        //======== menuBar1 ========
        {

            //======== fileMenu ========
            {
                fileMenu.setText("File");

                //---- newMenuItem ----
                newMenuItem.setText("New");
                newMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        newMenuItemMouseClicked(e);
                    }
                });
                fileMenu.add(newMenuItem);

                //---- openMenuItem ----
                openMenuItem.setText("Open");
                openMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        openMenuItemMouseClicked(e);
                    }
                });
                fileMenu.add(openMenuItem);

                //---- saveMenuItem ----
                saveMenuItem.setText("Save");
                saveMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        saveMenuItemMouseClicked(e);
                    }
                });
                fileMenu.add(saveMenuItem);

                //---- saveAsMenuItem ----
                saveAsMenuItem.setText("Save As...");
                saveAsMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        saveAsMenuItemMouseClicked(e);
                    }
                });
                fileMenu.add(saveAsMenuItem);
            }
            menuBar1.add(fileMenu);

            //======== runMenu ========
            {
                runMenu.setText("Run");

                //---- LexMenuItem ----
                LexMenuItem.setText("Lexical Analysis");
                LexMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        LexMenuItemMouseClicked(e);
                    }
                });
                runMenu.add(LexMenuItem);

                //---- synMenuItem ----
                synMenuItem.setText("Syntactic Analysis");
                synMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        synMenuItemMouseClicked(e);
                    }
                });
                runMenu.add(synMenuItem);

                //---- semMenuItem ----
                semMenuItem.setText("Semantic Analysis");
                semMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        semMenuItemMouseClicked(e);
                    }
                });
                runMenu.add(semMenuItem);
            }
            menuBar1.add(runMenu);

            //======== buildMenu ========
            {
                buildMenu.setText("Build");

                //---- buildMenuItem ----
                buildMenuItem.setText("Generate .ino file");
                buildMenuItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        buildMenuItemMouseClicked(e);
                    }
                });
                buildMenu.add(buildMenuItem);
            }
            menuBar1.add(buildMenu);
        }
        setJMenuBar(menuBar1);

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[fill]"));

            //======== scrollPane1 ========
            {

                //---- inputTextPane ----
                inputTextPane.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
                inputTextPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                scrollPane1.setViewportView(inputTextPane);
            }
            panel1.add(scrollPane1, "cell 0 0,width 410:410:410,height 600:600:600");

            //======== scrollPane2 ========
            {

                //---- outputTextPane ----
                outputTextPane.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
                outputTextPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                scrollPane2.setViewportView(outputTextPane);
            }
            panel1.add(scrollPane2, "cell 1 0,width 410:410:410,height 600:600:600");
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
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTextPane inputTextPane;
    private JScrollPane scrollPane2;
    private JTextPane outputTextPane;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}