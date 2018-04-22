package GameDevelopments.gui.actions;

import GameDevelopments.gui.threads.BuildSceneThread;

public class OpenWindowAction extends CustomAction {

    private int openSceneID;

    public OpenWindowAction(int openSceneID)
    {
        this.openSceneID = openSceneID;
    }
    @Override
    protected void doSomething() {
        new BuildSceneThread(openSceneID).start();
    }
}
