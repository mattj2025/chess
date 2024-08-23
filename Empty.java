import java.util.ArrayList;

public class Empty extends Piece
{
    public Empty(int x, int y)
    {
        super(x, y, false, " ", "");
    }

    @Override
    public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
    {
        return new ArrayList<>();
    }

    @Override
  public Empty copy()
  {
    return new Empty(getX(), getY());
  }
}
