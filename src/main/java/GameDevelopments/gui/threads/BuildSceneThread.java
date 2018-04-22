package GameDevelopments.gui.threads;

import GameDevelopments.gui.frame.MainFrame;
import GameDevelopments.gui.scene.Scene;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BuildSceneThread extends Thread
{
    private Method method = null;
    private int sceneId = 0;
    private Object[] parameters = null;
    public BuildSceneThread(Method method, Object[] parameters)
    {
        this.method = method;
        this.parameters = parameters;
    }

    public BuildSceneThread(int sceneId) {
        this.sceneId = sceneId;
    }

    @Override
    public void run() {
        if(method!=null) {
            try {
                MainFrame.instance.changeScene((Scene) method.invoke(null,parameters));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        else
        {
            MainFrame.instance.changeScene(sceneId);
        }
    }
}
