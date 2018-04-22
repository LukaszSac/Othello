package GameDevelopments.game.elements;

import GameDevelopments.game.utilities.Pos;

public class Tile
{
    private Pos pos;
    private Stone piece = null;
    public Tile(Pos pos)
    {
        piece = new Stone(Stone.NONE);
        this.pos = pos;
    }

    public Stone getPiece() {
        return piece;
    }

    public void setPiece(Stone piece) {
        this.piece = piece;
    }

    public Pos getPos() {
        return pos;
    }
}
