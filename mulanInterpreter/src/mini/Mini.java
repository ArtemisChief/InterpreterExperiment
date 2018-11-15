package mini;

import mini.ui.MiniGUI;

import javax.swing.*;

public class Mini {

    public static void main(String[] args) {
        try {
            System.setProperty("sun.java2d.noddraw", "true");
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        MiniGUI miniGUI=new MiniGUI();
        miniGUI.setVisible(true);

    }
}
