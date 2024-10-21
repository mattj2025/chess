import java.util.ArrayList;

public class Trap extends Piece
{
  public Trap(int x, int y)
  {
    super(x, y, false, "T", "");
  }

  @Override
  public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
  {
    return new ArrayList<>();
  }

  @Override
  public Trap copy()
  {
    return new Trap(getX(), getY());
  }

  @Override
  public int getPieceCode()
  {
    if (isWhite())
      return 5;
    return -5;
  }

  @Override
  public Board.Occupation getOccupation()
  {
    return Board.Occupation.TRAP;
  }
  
  @Override
  public int getScore()
  {
	  return 0;
  }
}
