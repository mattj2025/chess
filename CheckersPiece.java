import java.util.ArrayList;

public class CheckersPiece extends Piece
{
    private final int lastRow;

    public CheckersPiece(int x, int y, boolean white)
    {
        super(x,y, white, "c", "images\\checkers");

        if (isWhite())
            lastRow = 0;
        else
            lastRow = 7;
    }

    @Override
    public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
    {
        ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
        moves.add(new ArrayList<>());
        moves.add(new ArrayList<>());
        
        ArrayList<Boolean> jumps = new ArrayList<>();

        int x = getX();
        int y = getY();

        int yMultiplier = 1;
        if (isWhite())
            yMultiplier = -1;

        if (x > 0 && !jumps.contains(true) && !b.isOccupied(x - 1, y + 1 * yMultiplier))
        {
            moves.get(0).add(x - 1);
            moves.get(1).add(y + 1 * yMultiplier);
            jumps.add(false);
        }
        else if (x > 1 && !b.isOccupied(x - 2, y + 2 * yMultiplier) && b.occupation(x - 1, y + 1 * yMultiplier) == Board.boolToOccupation(!isWhite()))
        {
            moves.get(0).add(x - 2);
            moves.get(1).add(y + 2 * yMultiplier);
            jumps.add(true);
        }

        if (x < 8 && !jumps.contains(true) && !b.isOccupied(x + 1, y + 1 * yMultiplier))
        {
            moves.get(0).add(x + 1);
            moves.get(1).add(y + 1 * yMultiplier);
            jumps.add(false);
        }
        else if (x < 7 && !b.isOccupied(x + 2, y + 2 * yMultiplier) && b.occupation(x + 1, y + 1 * yMultiplier) == Board.boolToOccupation(!isWhite()))
        {
            moves.get(0).add(x + 2);
            moves.get(1).add(y + 2 * yMultiplier);
            jumps.add(true);
        }

        if (jumps.contains(true))
            for (int i = 0; i < jumps.size(); i++)
            {
                if (!jumps.get(i))
                {
                    moves.get(0).remove(i);
                    moves.get(1).remove(i);
                    jumps.remove(i);
                }
            }
    
        return moves;
    }

    @Override
    public boolean move(int x, int y, Board b, boolean test)
    {
        boolean r = super.move(x,y,b, test);
    
        if (y == lastRow)
            b.setPiece(x,y,new CheckersKingPiece(x,y,isWhite()));

        return r;
  }

  @Override
  public CheckersPiece copy()
  {
    return new CheckersPiece(getX(), getY(), isWhite());
  }

    @Override
    public int getPieceCode()
    {
        if (isWhite())
            return 3;
        return -3;
    }
    
    @Override
    public int getScore()
    {
  	  return 1;
    }
}
