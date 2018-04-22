package GameDevelopments.game;

import GameDevelopments.game.elements.Stone;
import GameDevelopments.game.elements.Tile;
import GameDevelopments.game.elements.TileListener;
import GameDevelopments.game.events.GameHandler;
import GameDevelopments.game.threads.ServerThread;
import GameDevelopments.game.utilities.InvalidMoveException;
import GameDevelopments.game.utilities.Pos;
import GameDevelopments.gui.panels.PlainText;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class OthelloGame
{
    private final PlainText result;
    private List <Tile> freeTiles;
    private Tile[][] tiles = null;
    private GameHandler handler = null;
    private int colour,opponentColour;
    boolean yourTurn = false;
    private ServerThread serverThread;
    private int yourCount = 0, oppCount = 0;
    private PlainText turnText,blackCount,whiteCount;
    private boolean gameEnded = false;
    public OthelloGame(String colour, Socket connection, PlainText turn, PlainText blackCount, PlainText whiteCount, PlainText result)
    {
        this.result = result;
        this.blackCount = blackCount;
        this.whiteCount = whiteCount;
        freeTiles = new ArrayList<>();
        this.turnText = turn;
        tiles = new Tile[8][8];
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                tiles[i][j] = new Tile(new Pos(i,j));
                freeTiles.add(tiles[i][j]);
            }
        }
        handler = new GameHandler();
        if(colour.matches("White")) {
            this.colour = Stone.WHITE;
            opponentColour = Stone.BLACK;
            yourTurn = true;
            turn.changeText(8);
        } else if(colour.matches("Black"))
        {
            this.colour = Stone.BLACK;
            opponentColour = Stone.WHITE;
            turn.changeText(9);
        }
        serverThread = new ServerThread(this,connection);
        serverThread.start();
    }

    private List<Pos> hasAdjacent(int x, int y, int colour)
    {
        List<Pos> positions = new ArrayList<>();
            for(int i=-1;i<=1;i++)
            {
                for(int j=-1;j<=1;j++)
                {
                    try {
                        if (!Stone.sameColour(tiles[x + i][y + j].getPiece().getColour(), colour)) {
                            positions.add(new Pos( i, j));
                        }
                    }catch(IndexOutOfBoundsException e)
                    {

                    }
                }
            }
        return positions;
    }


    private List<Pos> hasAcross(List<Pos> positions, int x, int y, int colour)
    {
        List<Pos> acrossPositions = new ArrayList<>();
        for(Pos pos : positions)
        {
            int x2=pos.getX()+x;
            int y2=pos.getY()+y;
            boolean end = false;
            while(!end)
            {
                try {
                    if (tiles[x2][y2].getPiece().getColour() == Stone.NONE) end = true;
                    else if (tiles[x2][y2].getPiece().getColour() == colour) {
                        acrossPositions.add(pos);
                        end = true;
                    } else {
                        x2 += pos.getX();
                        y2 += pos.getY();
                    }
                } catch (IndexOutOfBoundsException e)
                {
                    end = true;
                }
            }
        }
        return acrossPositions;
    }

    public void addListenerToTile(int x, int y, TileListener listener)
    {
        handler.addTileListener(tiles[x][y],listener);
    }

    private void changeTile(int x, int y, int colour)
    {
        List<Pos>adjacent =  hasAdjacent(x,y,colour);
        adjacent = hasAcross(adjacent,x,y,colour);
        for(Pos pos : adjacent)
        {
            int x2=pos.getX()+x;
            int y2=pos.getY()+y;
            boolean end = false;
            while(!end)
            {
                try {
                    if (tiles[x2][y2].getPiece().getColour() == Stone.NONE) end = true;
                    else if (tiles[x2][y2].getPiece().getColour() == colour) {
                        end = true;
                    } else {
                        forcePiece(x2,y2,colour);
                        x2 += pos.getX();
                        y2 += pos.getY();
                    }
                } catch (IndexOutOfBoundsException e)
                {
                    end = true;
                }
            }
        }

    }

    public synchronized void checkState()
    {
        if(freeTiles.size()==0)
            throw new GameEndedException();
        int noOfMoves = 0;
        for(Tile tile : freeTiles)
        {
            Pos pos = tile.getPos();
            List<Pos> adjacent = hasAdjacent(pos.getX(), pos.getY(), colour);
            if(adjacent.size()>0)
            {
                List<Pos> toChange = hasAcross(adjacent,pos.getX(),pos.getY(),colour);
                if(toChange.size()>0)
                    noOfMoves++;
            }
        }
        if(noOfMoves>0) throw  new CanMakeMoveException();
        else throw new CannotMakeMoveException();
    }

    public synchronized void placePiece(int x, int y)
    {
        if(yourTurn) {
            if(tiles[x][y].getPiece().getColour()==Stone.NONE) {
                List<Pos> adjacent = hasAdjacent(x, y, colour);
                if (adjacent.size() > 0) {
                    List<Pos> toChange = hasAcross(adjacent,x,y,colour);
                    if (toChange.size() > 0) {
                        changeTile(x, y, colour);
                        forcePiece(x,y,colour);
                        nextTurn(false);
                        changeNumber();
                        result.setVisible(false);
                        serverThread.send(5,new Pos(x,y).toString());
                    } else throw new InvalidMoveException();
                } else throw new InvalidMoveException();
            }
            else throw new InvalidMoveException();
        } else
        {
            throw new InvalidMoveException();
        }
    }

    public void opponentHasNoMoves()
    {
        result.changeText(20);
        result.showText();
    }

    public void gameEnded(boolean youWon)
    {
        gameEnded = true;
        if(youWon)
            result.changeText(18);
        else
            result.changeText(19);
        result.showText();
        turnText.setVisible(false);
        nextTurn(false);
    }

    public void changeNumber()
    {
        int white = 0,black =0;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(tiles[i][j].getPiece().getColour()==Stone.WHITE)white++;
                else if(tiles[i][j].getPiece().getColour()==Stone.BLACK)black++;
            }
        }
        whiteCount.changeStatic(": " + white);
        blackCount.changeStatic(": " + black);
        if(colour == Stone.WHITE)
        {
            yourCount = white;
            oppCount = black;
        }
        else
        {
            yourCount = black;
            oppCount = white;
        }
    }

    public int getYourCount() {
        return yourCount;
    }

    public int getOppCount() {
        return oppCount;
    }

    public void forfeit()
    {
        if(!gameEnded) {
            System.out.println("Forfeit");
            serverThread.send(6, "1");
            serverThread.endConnection();
        }
    }



    public void forcePiece(int x, int y, int colour)
    {
        Stone stone = new Stone(colour);
        int prevCol = tiles[x][y].getPiece().getColour();
        if(prevCol==opponentColour)oppCount --;
        else if(prevCol == this.colour) yourCount --;
        if(this.colour == colour) yourCount++;
        else if(opponentColour == colour) oppCount ++;
        tiles[x][y].setPiece(stone);
        freeTiles.remove(tiles[x][y]);
        handler.changeTileState(tiles[x][y],stone);
    }

    public void nextTurn(boolean value)
    {
        if(value)
            turnText.changeText(8);
        else
            turnText.changeText(9);
        yourTurn = value;
    }

    public synchronized void opponentPlaced(int x, int y)
    {
        changeTile(x, y, opponentColour);
        forcePiece(x,y,opponentColour);
        changeNumber();
        nextTurn(true);
    }
    public int getOpponentColour() {
        return opponentColour;
    }

    public void endConnection()
    {
        serverThread.endConnection();
    }
}
