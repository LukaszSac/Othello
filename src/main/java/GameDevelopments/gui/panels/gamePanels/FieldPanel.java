package GameDevelopments.gui.panels.gamePanels;

import GameDevelopments.game.OthelloGame;
import GameDevelopments.game.elements.Stone;
import GameDevelopments.game.elements.TileListener;
import GameDevelopments.game.utilities.InvalidMoveException;
import GameDevelopments.gui.panels.OthelloComponent;
import GameDevelopments.utilities.ImageHolder;
import GameDevelopments.utilities.ImageListener;
import GameDevelopments.utilities.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FieldPanel extends JLabel implements OthelloComponent, TileListener, ImageListener, MouseListener
{
    private static FieldPanel chosen = null;
    private Painter painter = null;
    private String currentPiece = null;
    private JLabel pieceLabel = null;
    private int xPos, yPos;
    private OthelloGame gameInstance = null;
    public FieldPanel(double x, double y, int align, int xPos, int yPos, OthelloGame gameInstance)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.gameInstance = gameInstance;
        setFocusable(false);
        setIcon(ImageHolder.getInstance().getIcon(ImageHolder.TILE));
        painter = new Painter(x,y,align,this);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        placeLocation();
        addMouseListener(this);
    }

    @Override
    public void unregister() {

    }

    private void placeLocation()
    {
        double width = ImageHolder.getTileWidth();
        setSize((int) width, (int) width);
        painter.placeLocation();
    }

    private void placePiece(ImageIcon icon)
    {
        if(pieceLabel!=null)
        {
            remove(pieceLabel);
        }
        JLabel piece = new JLabel();
        piece.setIcon(icon);
        add(piece);
        int width = (int)ImageHolder.getTileWidth();
        piece.setSize(width,width);
        pieceLabel = piece;
    }

    private void removePieceListener()
    {
        if(currentPiece!=null)
        {
            ImageHolder.getInstance().removeListener(currentPiece,this);
        }
    }

    @Override
    public void stateChanged(Stone stone)
    {
        removePieceListener();
        if(stone!=null)
        {
            if(stone.getColour()==Stone.BLACK)
                currentPiece = ImageHolder.BLACK_PIECE;
            else if(stone.getColour()==Stone.WHITE)
                currentPiece = ImageHolder.WHITE_PIECE;
            placePiece(ImageHolder.getInstance().getIcon(currentPiece));
        }
    }

    @Override
    public void imageChanges(ImageIcon icon, String key)
    {
        if(key == ImageHolder.TILE) {
            setIcon(icon);
            placeLocation();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        chosen = this;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(chosen == this)
        {
            try {
                gameInstance.placePiece(xPos, yPos);
            } catch (InvalidMoveException e2)
            {

            }
        }
        chosen = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        chosen = null;
    }
}
