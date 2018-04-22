package GameDevelopments.gui.actions;

import GameDevelopments.dictionary.CustomDictionary;
import GameDevelopments.settings.Settings;

public class ChangeLanguageAction extends CustomAction {

    private int way;

    public ChangeLanguageAction(int way)
    {
        this.way = way;
    }

    @Override
    protected void doSomething()
    {
        int langId = CustomDictionary.getLangID(way);
        Settings.getInstance().changeValue("Language_lang",CustomDictionary.getLangNameText(langId));
    }
}
