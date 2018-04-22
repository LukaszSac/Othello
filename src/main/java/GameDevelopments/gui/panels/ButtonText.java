package GameDevelopments.gui.panels;
import GameDevelopments.dictionary.CustomDictionary;
import GameDevelopments.event.ListenerManager;
import GameDevelopments.settings.SettingsListener;
import GameDevelopments.utilities.Painter;

import javax.swing.*;
import java.awt.*;

public class ButtonText extends JButton implements SettingsListener, OthelloComponent
{
    private GameDevelopments.utilities.Painter painter = null;
    private String text = null;
    private int textID;
    private ListenerManager<SettingsListener> listener = new ListenerManager<> (this);

    public ButtonText(ImageIcon icon, double x, double y, int align)
    {
        setIcon(icon);
        painter = new GameDevelopments.utilities.Painter(x,y,align,this);
        setSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
        listener.addListener("Window_resolution");
        painter.placeLocation();

    }

    public ButtonText(String text,int fontsize, Color foreground, double x, double y, int align)
    {
        this.text = text;
        init(fontsize,foreground,x,y,align);
    }

    public ButtonText(int textId,int fontsize ,Color foreground, double x, double y, int align)
    {
        this.textID = textId;
        text = CustomDictionary.getInstance().getWordById(textID);
        listener.addListener("Language_lang");
        init(fontsize,foreground,x,y,align);
    }

    public void init(int fontsize, Color foreground, double x, double y, int align)
    {
        listener.addListener("Window_resolution");
        painter = new Painter(fontsize,foreground,x,y,align,this);
        setText(text);
        setFocusable(false);
        painter.resizeToFont(text);
    }

    protected void changeText()
    {
        text = CustomDictionary.getInstance().getWordById(textID);
        setText(text);
        painter.resizeToFont(text);
    }

    @Override
    public void settingsChanged(String key, String value)
    {
        if(key=="Language_lang")
            changeText();
        else if(key=="Window_resolution")
            painter.placeLocation();
    }

    @Override
    public void unregister()
    {
        if(listener!=null)
            listener.unregister();
    }
}