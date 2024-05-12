import java.util.ArrayList;

public class Empty extends Piece
{
    public Empty(int x, int y)
    {
        super(x, y, false, " ", "images\\pawn");
    }

    public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
    {
        return new ArrayList<ArrayList<Integer>>();
    }
}
