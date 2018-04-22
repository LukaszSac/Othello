package GameDevelopments.gui.panels;

import GameDevelopments.event.ListenerManager;
import GameDevelopments.settings.SettingsListener;
import GameDevelopments.utilities.ImageHolder;
import GameDevelopments.utilities.ImageListener;
import GameDevelopments.utilities.Painter;

import javax.swing.*;

public class ImagePanel extends JLabel implements ImageListener, OthelloComponent{

    private Painter painter = null;
    private String imageIcon = null;
    public ImagePanel(double x, double y, int align, String imageIcon)
    {
        this.imageIcon = imageIcon;
        this.painter = new Painter(x,y,align,this);
        ImageHolder.getInstance().addListener(imageIcon,this);
        setIcon(ImageHolder.getInstance().getIcon(imageIcon));
        placeLocation();
    }

    private void placeLocation()
    {
        ImageIcon icon = ImageHolder.getInstance().getIcon(imageIcon);
        setSize(icon.getIconWidth(), icon.getIconHeight());
        painter.placeLocation();
    }

    @Override
    public void imageChanges(ImageIcon icon, String key) {
        if(key==imageIcon)
        {
            setIcon(icon);
            placeLocation();
        }
    }

    @Override
    public void unregister()
    {
        ImageHolder.getInstance().removeListener(imageIcon,this);
    }
}
