package GameDevelopments.utilities;

import GameDevelopments.settings.Settings;

public class Resolution
{
    private int width,height;
    private static int resCount =3;
    private static String[] ress = {"1280x720","1360x768","1600x900"};
    public Resolution(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
    public static Resolution parse(String s)
    {
        int point = s.indexOf('x');
        int width = Integer.parseInt(s.substring(0,point));
        int height = Integer.parseInt(s.substring(point+1,s.length()));
        return new Resolution(width,height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static String getNextRes()
    {
        String current = Settings.getInstance().getValue("Window_resolution");
        for(int i=0;i<=resCount;i++)
        {
            if(ress[i].matches(current))
            {
                i+=1;
                if(i>=resCount) i = 0;
                return ress[i];
            }
        }
        return ress[0];
    }
    public static String getPrevRes()
    {
        String current = Settings.getInstance().getValue("Window_resolution");
        for(int i=0;i<=resCount;i++)
        {
            if(ress[i].matches(current))
            {
                i-=1;
                if(i<0) i = resCount-1;
                return ress[i];
            }
        }
        return ress[0];
    }

    public static String getRes(int way)
    {
        String current = Settings.getInstance().getValue("Window_resolution");
        for(int i=0;i<=resCount;i++)
        {
            if(ress[i].matches(current))
            {
                i+=way;
                if(i<0) i = resCount-1;
                if(i>=resCount) i = 0;
                return ress[i];
            }
        }
        return ress[0];
    }
}
