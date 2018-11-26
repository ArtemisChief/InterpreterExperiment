package mini;

import mini.ui.MiniGUI;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;

public class Mini {

    public static void main(String[] args) {
        try {

            System.setProperty("sun.java2d.noddraw", "true");
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        MiniGUI miniGUI = new MiniGUI();
        miniGUI.setVisible(true);

    }

}