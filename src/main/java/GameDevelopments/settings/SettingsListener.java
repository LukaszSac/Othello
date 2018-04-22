package GameDevelopments.settings;

public interface SettingsListener
{
   void settingsChanged(String key, String value);
   void unregister();
}
