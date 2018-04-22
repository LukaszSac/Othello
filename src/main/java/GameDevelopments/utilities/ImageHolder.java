package GameDevelopments.utilities;

import GameDevelopments.settings.Settings;
import GameDevelopments.settings.SettingsListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageHolder implements SettingsListener
{

    //Keys
    public static final String TILE = "Tile";
    public static final String BLACK_PIECE = "Black piece";
    public static final String WHITE_PIECE = "White piece";
    public static final String OTHELLO_STRING = "Othello string";
    public static final String PLEASE_WAIT = "please wait";

    //Instance
    private static ImageHolder instance = null;

    //Listeners
    private HashMap<String,ArrayList<ImageListener>> listeners = null;

    //Icons
    private ImageIcon tile = null;
    private ImageIcon blackPiece = null;
    private ImageIcon whitePiece = null;
    private ImageIcon othelloString = null;
    private ImageIcon pleaseWait = null;

    //Percents
    private static double tileIconWidthPerc = 0.06;
    private static double othelloIconWidthPerc = 0.4;
    private static double othelloIconHeightPerc = 0.2;;
    private static double waitIconWidthPerc = 0.4;
    private static double waitIconHeightPerc = 0.2;
    private ImageHolder()
    {
        Settings.getInstance().addListener("Window_resolution",this);
        listeners = new HashMap<>();

        tile = FileLoader.loadImage("resources/tile.png");
        listeners.put(TILE,new ArrayList<>());

        whitePiece = FileLoader.loadImage("resources/white piece.png");
        listeners.put(WHITE_PIECE,new ArrayList<>());

        blackPiece = FileLoader.loadImage("resources/black piece.png");
        listeners.put(BLACK_PIECE,new ArrayList<>());

        othelloString = FileLoader.loadImage("resources/Othello.png");
        listeners.put(OTHELLO_STRING,new ArrayList<>());

        pleaseWait = FileLoader.loadImage("resources/PleaseWaitTxt.png");
        listeners.put(PLEASE_WAIT,new ArrayList<>());

        resizeToResolution();
    }

    public void addListener(String key, ImageListener listener)
    {
        listeners.get(key).add(listener);
    }

    public void removeListener(String key, ImageListener listener)
    {
        listeners.get(key).remove(listener);
    }

    public static ImageHolder getInstance()
    {
        if(instance==null)
            instance = new ImageHolder();
        return instance;
    }

    public static double getTileWidth()
    {
        return getIconWidth(tileIconWidthPerc);
    }

    private static int getIconWidth(double perc)
    {
        Resolution res = Resolution.parse(Settings.getInstance().getValue("Window_resolution"));
        return (int)(perc*res.getWidth());
    }

    private static int getIconHeight(double perc)
    {
        Resolution res = Resolution.parse(Settings.getInstance().getValue("Window_resolution"));
        return (int)(perc*res.getHeight());
    }


    private void resizeToResolution()
    {
        double width = getIconWidth(tileIconWidthPerc);
        int widthOthelloString = getIconWidth(othelloIconWidthPerc);
        int heightOthelloString = getIconHeight(othelloIconHeightPerc);
        tile = new ImageIcon(Scaler.getScaledImage(tile.getImage(),(int)width,(int)width));
        blackPiece = new ImageIcon(Scaler.getScaledImage(blackPiece.getImage(),(int)width,(int)width));
        whitePiece = new ImageIcon(Scaler.getScaledImage(whitePiece.getImage(),(int)width,(int)width));
        othelloString = new ImageIcon(Scaler.getScaledImage(othelloString.getImage(),widthOthelloString,heightOthelloString));
        pleaseWait = new ImageIcon(Scaler.getScaledImage(pleaseWait.getImage(),widthOthelloString,heightOthelloString));
        for(ImageListener listener : listeners.get(TILE))
        {
            listener.imageChanges(tile, TILE);
        }
        for(ImageListener listener : listeners.get(BLACK_PIECE))
        {
            listener.imageChanges(blackPiece, TILE);
        }
        for(ImageListener listener : listeners.get(WHITE_PIECE))
        {
            listener.imageChanges(whitePiece, TILE);
        }
        for(ImageListener listener : listeners.get(OTHELLO_STRING))
        {
            listener.imageChanges(othelloString, OTHELLO_STRING);
        }
        for(ImageListener listener : listeners.get(PLEASE_WAIT))
        {
            listener.imageChanges(pleaseWait, PLEASE_WAIT);
        }
    }

    public ImageIcon getIcon(String key)
    {
        switch(key)
        {
            case TILE:
                return tile;
            case BLACK_PIECE:
                return blackPiece;
            case WHITE_PIECE:
                return whitePiece;
            case OTHELLO_STRING:
                return othelloString;
            case PLEASE_WAIT:
                return pleaseWait;
            default:
                return null;
        }
    }

    @Override
    public void settingsChanged(String key, String value)
    {
        if(key=="Window_resolution")
        {
            resizeToResolution();
        }
    }

    @Override
    public void unregister() {

    }
}
