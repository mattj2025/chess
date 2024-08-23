import java.util.ArrayList;

public class Duck extends Piece
{
    public Duck(int x, int y)
    {
        super(x,y,true,"d","images\\duck");
    }

  @Override
  public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
  {
    ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());

    for (int i = 0; i < 8; i++)
        for (int j = 0; j < 8; j++)
                if (!b.isOccupied(i,j))
                {
                    moves.get(0).add(i);
                    moves.get(1).add(j);
                }

    return moves;
  }

  @Override
  public Duck copy()
  {
    return new Duck(getX(), getY());
  }
}
