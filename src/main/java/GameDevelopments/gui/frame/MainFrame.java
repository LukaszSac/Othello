package GameDevelopments.gui.frame;

import GameDevelopments.gui.scene.Scene;
import GameDevelopments.gui.scene.SceneListener;
import GameDevelopments.gui.scene.SceneManager;
import GameDevelopments.gui.scene.SceneManagerFacade;
import GameDevelopments.utilities.Resolution;
import GameDevelopments.settings.Settings;
import GameDevelopments.settings.SettingsListener;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.util.concurrent.Semaphore;

public class MainFrame extends JFrame implements SettingsListener, SceneListener
{
    public static double WIDTH,HEIGHT;
    public static Dimension res;
    private static SceneManagerFacade sm = null;
    public static MainFrame instance = null;
    private Scene activeScene = null;
    public static Semaphore sceneLoaderSemaphore = new Semaphore(1);
    public MainFrame() {
        super();
        Settings.getInstance().addListener("Window_resolution", this);
        downloadSettings();
        init();
        instance = this;
        try {
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            UIManager.setLookAndFeel(new MetalLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void downloadRes()
    {
        Resolution resSet = Resolution.parse(Settings.getInstance().getValue("Window_resolution"));

        this.WIDTH = resSet.getWidth();
        this.HEIGHT = resSet.getHeight();
        res = new Dimension((int)WIDTH,(int)HEIGHT);
    }

    private void downloadSettings()
    {
        downloadRes();
    }


    private void resize()
    {
        setSize((int)WIDTH, (int)HEIGHT);
    }

    private void init()
    {
        resize();
        setTitle("Game of Othello");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        sm = new SceneManager();
        add(activeScene = sm.getNewScene());
    }

    @Override
    public void settingsChanged(String key, String value)
    {
        if(key.matches("Window_resolution"))
        {
            downloadRes();
            resize();
        }
    }

    @Override
    public void unregister() {

    }

    @Override
    public synchronized void changeScene(int ID)
    {
        activeScene.unregister();
        remove(activeScene);
        sm.changeScene(ID);
        add(activeScene = sm.getNewScene());
        repaint();
        sceneLoaderSemaphore.release();
    }

    public synchronized void changeScene(Scene scene)
    {
        activeScene.unregister();
        remove(activeScene);
        add(activeScene = scene);
        repaint();
        sceneLoaderSemaphore.release();
    }
}
