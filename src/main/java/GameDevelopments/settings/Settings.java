package GameDevelopments.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Settings
{

    private static Settings instance = new Settings();
    private HashMap<String, String> hmap;
    private HashMap<String, ArrayList<SettingsListener>> listeners;
    public static Settings getInstance()
    {
        return instance;
    }

    private Settings()
    {
        hmap = new HashMap<>();
        listeners = new HashMap<>();
    }

    public void getSettings()
    {
        File file = new File("properties.ini");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(scanner == null);
        else searchFile(scanner);
    }

    private void searchFile(Scanner scanner)
    {
        while(scanner.hasNext())
        {
            String line = scanner.nextLine();
            if(line.length()>=2)
            {
                if(line.substring(0,2).matches("//"));
                else putIntoHash(line);
            }
        }
    }


    public String getValue(String key)
    {
        return hmap.get(key);
    }

    private void putIntoHash(String line)
    {
        int separator = line.indexOf('=');
        String key = line.substring(0, separator - 1);
        String value = line.substring(separator + 3, line.length() - 1);
        hmap.put(key, value);
        listeners.put(key,new ArrayList<>());
    }

    public String getLangPath()
    {

        String path = getValue("Language_path");
        String lang = getValue("Language_lang");
        String result = path + lang + ".xml";
        return result;
    }

    public void changeValue(String key, String value)
    {
        hmap.replace(key,value);
        ArrayList<SettingsListener> list = listeners.get(key);
        for(SettingsListener sl : list)
        {
            sl.settingsChanged(key,value);
        }
    }

    public void addListener(String key, SettingsListener listener)
    {
        if(listener!=null)
            if(!listeners.get(key).contains(listener))
                listeners.get(key).add(listener);
    }

    public void removeListener(String key, SettingsListener listener)
    {
        listeners.get(key).remove(listener);
    }

}
