package GameDevelopments.game.utilities;

public class Pos
{
    private int x, y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static Pos parse(String value)
    {
        int inter = value.indexOf('x');
        int newx = Integer.parseInt(value.substring(0,inter));
        int newy = Integer.parseInt(value.substring(inter+1,value.length()));
        return new Pos(newx, newy);
    }

    public String toString()
    {
        return new String(x + "x" +y);
    }
}
