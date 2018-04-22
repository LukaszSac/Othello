package GameDevelopments.event;
import GameDevelopments.settings.Settings;
import GameDevelopments.settings.SettingsListener;
import java.util.ArrayList;

public class ListenerManager<T>
{
    private java.util.List<String> keys = new ArrayList<>();
    private T listener;

    public ListenerManager(T listener) {
        this.listener = listener;
    }

    public void addListener(String key)
    {
        keys.add(key);
        if(listener instanceof SettingsListener)
            Settings.getInstance().addListener(key, (SettingsListener) listener);

    }

    public void unregister()
    {
        for(String key: keys)
        {
            if(listener instanceof SettingsListener)
                Settings.getInstance().removeListener(key, (SettingsListener) listener);
        }
    }


}
