import java.util.ArrayList;

public class King extends Piece
{
  public King(int x, int y, boolean white)
  {
    super(x,y,white,"K","images\\king");
  }

  public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
  {
    ArrayList<ArrayList<Integer>> check = new ArrayList<>();
    check.add(new ArrayList<>());
    check.add(new ArrayList<>());

    ArrayList<ArrayList<Integer>> otherMoves;

    for (int i = 0; i < 8; i++)
        for (int j = 0; j < 8; j++)
        {
            Piece other = b.getPiece(j,i);
            if (other != null && other.isWhite() != isWhite() && !(other instanceof King))
            {
                if (other instanceof Pawn)
                    otherMoves = ((Pawn) other).getAttacks(b);
                else
                    otherMoves = other.getPossibleMoves(b);
                if (!otherMoves.isEmpty())
                {   
                    check.get(0).addAll(otherMoves.get(0));
                    check.get(1).addAll(otherMoves.get(1));
                }
            }
        }

    // TODO - figure out how to implement castling
    ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());

    for (int x = getX() - 1; x < getX() + 2; x++)
    {
      for (int y = getY() - 1; y < getY() + 2; y++)
      {
        if (x > -1 && x < 8 && y > -1 && y < 8  && b.occupation(x,y) != Main.boolToInt(isWhite()) && !Main.checkContainsCoordinate(check, x, y) && b.occupation(x,y) != 3)
        {
          moves.get(0).add(x);
          moves.get(1).add(y);
        }
      }
    }

    return moves;
  }

  public boolean check(Board b)
  {
    ArrayList<ArrayList<Integer>> otherMoves;

    for (int i = 0; i < 8; i++)
        for (int j = 0; j < 8; j++)
        {
            Piece other = b.getPiece(j,i);
            if (other != null && other.isWhite() != isWhite() && !(other instanceof King))
            {
                if (other instanceof Pawn)
                    otherMoves = ((Pawn) other).getAttacks(b);
                else
                    otherMoves = other.getPossibleMoves(b);
                for (int c = 0; c < otherMoves.get(0).size(); c++)
                {
                    if (otherMoves.get(0).get(c) == getX() && otherMoves.get(1).get(c) == getY())
                        return true;
                }
            }
        }
    return false;
  }
}