package GameDevelopments.gui.scene;

public class SceneManager implements SceneManagerFacade
{
    public static final int MENU = 0;
    public static final int SETTINGS = 1;
    public static final int GAME_LOAD = 2;
    public static final int GAME_START = 3;
    private int activeScene = 0;
    @Override
    public void changeScene(int ID)
    {
        activeScene = ID;
    }

    @Override
    public Scene getNewScene() {
        switch(activeScene)
        {
            case MENU:
                return SceneBuilder.buildMenu();
            case SETTINGS:
                return  SceneBuilder.buildSettings();
            case GAME_LOAD:
                return SceneBuilder.buildGame();
        }
        return null;
    }
}
