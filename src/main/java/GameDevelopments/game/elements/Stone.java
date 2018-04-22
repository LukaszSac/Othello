package GameDevelopments.game.elements;

public class Stone
{
    public static final int NONE = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    private int colour = NONE;
    public Stone(int colour)
    {
        this.colour = colour;
    }

    public int getColour() {
        return colour;
    }
    public static boolean sameColour(int colour1, int colour2)
    {
        if(colour1==colour2)return true;
        else if(colour1==NONE||colour2==NONE) return true;
        else return false;
    }
}
