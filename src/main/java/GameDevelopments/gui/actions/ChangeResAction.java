package GameDevelopments.gui.actions;
import GameDevelopments.utilities.Resolution;
import GameDevelopments.settings.Settings;

public class ChangeResAction extends CustomAction{
    private int way;

    public ChangeResAction(int way)
    {
        this.way = way;
    }

    @Override
    protected void doSomething() {
        String nextRes = Resolution.getRes(way);
        Settings.getInstance().changeValue("Window_resolution",nextRes);
    }
}
