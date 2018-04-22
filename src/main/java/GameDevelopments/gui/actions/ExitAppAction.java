package GameDevelopments.gui.actions;

import GameDevelopments.gui.frame.MainFrame;
import java.awt.event.WindowEvent;

public class ExitAppAction extends CustomAction{
    @Override
    protected void doSomething() {
        MainFrame.instance.dispatchEvent(new WindowEvent(MainFrame.instance, WindowEvent.WINDOW_CLOSING));
    }
}
