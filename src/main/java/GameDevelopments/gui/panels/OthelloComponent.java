package GameDevelopments.gui.panels;

import javax.swing.*;

public interface OthelloComponent
{
    default JComponent getComponent()
    {
        return (JComponent)this;
    }
    void unregister();
}
