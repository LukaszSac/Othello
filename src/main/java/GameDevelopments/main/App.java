package GameDevelopments.main;

import GameDevelopments.dictionary.CustomDictionary;
import GameDevelopments.gui.frame.MainFrame;
import GameDevelopments.settings.Settings;

import java.awt.*;

public class App 
{

    public static void main( String[] args )
    {
        Settings.getInstance().getSettings();
        CustomDictionary dictionery = CustomDictionary.getInstance();
        dictionery.loadDictionery();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
