package GameDevelopments.gui.actions;

import javax.swing.*;
import java.awt.*;

public class OpenFrameSettings extends CustomAction {
    @Override
    protected void doSomething() {
        JFrame frame = new JFrame();
        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        JPanel panel = new JPanel(new GridLayout(2,2));
        panel.setBounds(0,0,300,300);
        frame.add(panel);
        JButton resLeft = new JButton("Res <");
        resLeft.addActionListener(new ChangeResAction(-1));
        JButton resRight = new JButton("Res >");
        resRight.addActionListener(new ChangeResAction(1));
        JButton langLeft = new JButton("Lang <");
        langLeft.addActionListener(new ChangeLanguageAction(-1));
        JButton langRight = new JButton("Lang >");
        langRight.addActionListener(new ChangeLanguageAction(1));
        panel.add(langLeft);
        panel.add(langRight);
        panel.add(resLeft);
        panel.add(resRight);

    }
}
