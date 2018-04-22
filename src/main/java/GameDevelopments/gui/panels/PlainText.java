package GameDevelopments.gui.panels;

import GameDevelopments.dictionary.CustomDictionary;
import GameDevelopments.event.ListenerManager;
import GameDevelopments.settings.SettingsListener;
import GameDevelopments.utilities.Painter;

import javax.swing.*;
import java.awt.*;

public class PlainText extends JLabel implements SettingsListener, OthelloComponent
{
    private int textID = -1;
    private String text = null;
    private String staticText = "";
    private ListenerManager<SettingsListener> listener = new ListenerManager<>(this);
    private GameDevelopments.utilities.Painter painter = null;
    public PlainText(int textId, int fontSize, Color foreground, double x, double y, int align)
    {
        this.textID = textId;
        text = CustomDictionary.getInstance().getWordById(textID) + staticText;
        listener = new ListenerManager(this);
        listener.addListener("Language_lang");
        init(fontSize,foreground,x,y, align);
    }

    public PlainText(String text, int fontSize, Color foreground, double x, double y, int align)
    {
        this.text = text + staticText;
        init(fontSize,foreground,x,y, align);
    }

    public PlainText(String text,String staticText, int fontSize, Color foreground, double x, double y, int align)
    {
        this.staticText = staticText;
        this.text = text + staticText;
        init(fontSize,foreground,x,y, align);
    }

    public PlainText(int textId, String staticText, int fontSize, Color foreground, double x, double y, int align) {
        this.textID = textId;
        text = CustomDictionary.getInstance().getWordById(textID) + staticText;
        listener = new ListenerManager(this);
        listener.addListener("Language_lang");
        this.staticText = staticText;
        init(fontSize,foreground,x,y, align);
    }

    private void init(int fontSize, Color foreground, double x, double y, int align)
    {
        painter = new Painter(fontSize,foreground,x,y,align,this);
        listener.addListener("Window_resolution");
        setText(text);
        painter.resizeToFont(text);
    }
    public void changeText(String text)
    {
        this.text = text + staticText;
        setText(text);
        painter.resizeToFont(text);
    }

    public void changeText(int id)
    {
        this.textID = id;
        text = CustomDictionary.getInstance().getWordById(textID) + staticText;
        setText(text);
        painter.resizeToFont(text);
    }

    private void changeText()
    {
        text = CustomDictionary.getInstance().getWordById(textID) + staticText;
        setText(text);
        painter.resizeToFont(text);
    }

    public void changeStatic(String newText)
    {
        this.staticText = newText;
        if(textID>=0)
            changeText();
        else
            changeText(text);
    }

    @Override
    public void settingsChanged(String key, String value)
    {
        if(key=="Language_lang")
            changeText();
        else if(key=="Window_resolution")
            painter.placeLocation();
    }

    public void unregister()
    {
        listener.unregister();
    }

    public void showText()
    {
        setVisible(true);
    }
}
