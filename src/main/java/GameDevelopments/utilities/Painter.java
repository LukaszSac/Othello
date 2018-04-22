package GameDevelopments.utilities;

import GameDevelopments.settings.Settings;

import javax.swing.*;
import java.awt.*;

public class Painter
{
    public static final int CENTER = 0;
    public static final int LEFT = 1;
    public static final int EXACT = 2;
    private double x,y;
    private Color foreground;
    private int fontSize,align;
    private JComponent panel;
    private Font font = null;
    private String fontName = "Arial";

    public Painter(double x, double y, int align, JComponent panel)
    {
        this.x = x;
        this.y = y;
        this.align = align;
        this.panel = panel;
    }

    public Painter(int fontSize, Color foreground, double x, double y, int align, JComponent panel)
    {
        this.fontSize = fontSize;
        this.foreground = foreground;
        panel.setForeground(foreground);
        this.x = x;
        this.y = y;
        this.align = align;
        this.panel = panel;
        font = new Font(fontName,Font.PLAIN,fontSize);
    }

    public void placeLocation()
    {
        Resolution res = Resolution.parse(Settings.getInstance().getValue("Window_resolution"));
        if(align == CENTER)
            panel.setLocation((int)(res.getWidth()*x-panel.getSize().getWidth()/2.0),(int)(res.getHeight()*y-panel.getSize().getHeight()/2.0));
        else if(align == LEFT)
            panel.setLocation((int)(res.getWidth()*x),(int)(res.getHeight()*y-panel.getSize().getHeight()/2.0));
        else if(align == EXACT)
            panel.setLocation((int)(x),(int)(y));
    }

    public void resizeToFont(String text)
    {
        panel.setFont(font);
        FontMetrics fm = panel.getFontMetrics(font);
        int width = fm.stringWidth(text)+text.length() + 40;
        int height = fm.getHeight()+10;
        panel.setSize(new Dimension(width,height));
        placeLocation();
    }
}
