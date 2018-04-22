package GameDevelopments.game.events;

import GameDevelopments.game.elements.Stone;
import GameDevelopments.game.elements.Tile;
import GameDevelopments.game.elements.TileListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameHandler
{
    private List<GameListener> listeners = null;
    private HashMap<Tile,ArrayList<TileListener>> tileListeners = null;
    public GameHandler()
    {
        listeners = new ArrayList<>();
        tileListeners = new HashMap<>();
    }
    public void addTileListener(Tile tile, TileListener listener)
    {
        if(!tileListeners.containsKey(tile))
        {
            tileListeners.put(tile,new ArrayList<>());
            tileListeners.get(tile).add(listener);
        }
    }
    public void changeTileState(Tile tile, Stone stone)
    {
        if(tileListeners.containsKey(tile))
        {
            for(TileListener listener : tileListeners.get(tile))
            {
                listener.stateChanged(stone);
            }
        }
    }
}
