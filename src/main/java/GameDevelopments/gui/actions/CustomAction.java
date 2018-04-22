package GameDevelopments.gui.actions;

import GameDevelopments.gui.actions.customActions.CustomListenerAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public abstract class CustomAction extends AbstractAction
{
    ArrayList<CustomListenerAction> actions = new ArrayList<>();

    public void addCustomAction(CustomListenerAction action)
    {
        actions.add(action);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        for(CustomListenerAction action : actions)
        {
            action.performAction();
        }
        doSomething();
    }

    protected abstract void doSomething();
}
