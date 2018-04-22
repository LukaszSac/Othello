package GameDevelopments.dictionary;

import GameDevelopments.settings.Settings;
import GameDevelopments.settings.SettingsListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CustomDictionary implements SettingsListener
{
    private static final int POLSKI = 0;
    private static final int ENGLISH = 1;
    private static final int lCount = 1;
    public static int currentLanguage = 0;
    private static CustomDictionary instance = new CustomDictionary();
    Properties dictionery = new Properties();

    public static CustomDictionary getInstance(){return instance;}

    public static String getLangNameText(int id)
    {
        switch(id)
        {
            case 0:
                return "EU_pl";
            case 1:
                return "EU_en";
            default:
                return null;
        }
    }

    public static String getLangName(int id)
    {
        switch(id)
        {
            case 0:
                return "Polski";
            case 1:
                return "English";
            default:
                return null;
        }
    }
    private CustomDictionary()
    {
        Settings.getInstance().addListener("Language_lang",this);
    }

    public void loadDictionery()
    {
        try {
            dictionery.clear();
            dictionery.loadFromXML(new FileInputStream(Settings.getInstance().getLangPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWordById(int id)
    {
        String number = String.valueOf(id);
        String text = dictionery.getProperty(number);
        if(text==null)
        {
            throw new WordNotFoundException();
        }
        return text;
    }

    public static int getNextLangID()
    {
        int currentLangId = currentLanguage;
        currentLangId ++;
        if(currentLangId>lCount)currentLangId = 0;
        return currentLangId;
    }

    public static int getPrevLangID()
    {
        int currentLangId = currentLanguage;
        currentLangId --;
        if(currentLangId<0)currentLangId = lCount;
        return currentLangId;
    }
    @Override
    public void settingsChanged(String key, String value)
    {
        if(key=="Language_lang")
        {
            loadDictionery();
            if(value.matches("EU_en")) currentLanguage = ENGLISH;
            else if(value.matches("EU_pl")) currentLanguage = POLSKI;
        }
    }

    @Override
    public void unregister() {

    }

    public static int getLangID(int way)
    {
        int currentLangId = currentLanguage;
        currentLangId +=way;
        if(currentLangId>lCount)currentLangId = 0;
        if(currentLangId<0)currentLangId = lCount;
        return currentLangId;
    }
}
