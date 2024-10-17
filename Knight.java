import java.util.ArrayList;

public class Knight extends Piece
{
  public Knight(int x, int y, boolean white)
  {
    super(x,y,white,"N","images\\knight");
  }
  
  @Override
  public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
  {
    ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());

    int x = getX();
    int y = getY();

    if (x - 1 > -1 && y - 2 > -1 && b.occupation(x - 1, y - 2) != Main.boolToInt(isWhite()) && b.occupation(x - 1, y - 2) != 3)
    {
        moves.get(0).add(x - 1);
        moves.get(1).add(y - 2);
    }
    if (x + 1 < 8 && y - 2 > -1 && b.occupation(x + 1, y - 2) != Main.boolToInt(isWhite()) && b.occupation(x + 1, y - 2) != 3)
    {
        moves.get(0).add(x + 1);
        moves.get(1).add(y - 2);
    }
    if (x - 1 > -1 && y + 2 < 8 && b.occupation(x - 1, y + 2) != Main.boolToInt(isWhite()) && b.occupation(x - 1, y + 2) != 3)
    {
        moves.get(0).add(x - 1);
        moves.get(1).add(y + 2);
    }
    if (x + 1 < 8 && y + 2 < 8 && b.occupation(x + 1, y + 2) != Main.boolToInt(isWhite()) && b.occupation(x + 1, y + 2) != 3)
    {
        moves.get(0).add(x + 1);
        moves.get(1).add(y + 2);
    }
    
    if (x - 2 > -1 && y - 1 > -1 && b.occupation(x - 2, y - 1) != Main.boolToInt(isWhite()) && b.occupation(x - 2, y - 1) != 3)
    {
        moves.get(0).add(x - 2);
        moves.get(1).add(y - 1);
    }
    if (x + 2 < 8 && y - 1 > -1 && b.occupation(x + 2, y - 1) != Main.boolToInt(isWhite()) && b.occupation(x + 2, y - 1) != 3)
    {
        moves.get(0).add(x + 2);
        moves.get(1).add(y - 1);
    }
    if (x - 2 > -1 && y + 1 < 8 && b.occupation(x - 2, y + 1) != Main.boolToInt(isWhite()) && b.occupation(x - 2, y + 1) != 3)
    {
        moves.get(0).add(x - 2);
        moves.get(1).add(y + 1);
    }
    if (x + 2 < 8 && y + 1 < 8 && b.occupation(x + 2, y + 1) != Main.boolToInt(isWhite()) && b.occupation(x + 2, y + 1) != 3)
    {
        moves.get(0).add(x + 2);
        moves.get(1).add(y + 1);
    }

    return moves;
  }

  @Override
  public Knight copy()
  {
    return new Knight(getX(), getY(), isWhite());
  }

  @Override
    public int getPieceCode()
    {
        if (isWhite())
            return 7;
        return -7;
    }
  
  @Override
  public int getScore()
  {
	  return 3;
  }
}