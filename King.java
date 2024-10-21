import java.util.ArrayList;

public class King extends Piece
{
  public King(int x, int y, boolean white)
  {
    super(x,y,white,"K","images\\king");
  }

  @Override
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
                if (other instanceof Pawn pawn)
                    otherMoves = pawn.getAttacks(b);
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
        if (x > -1 && x < 8 && y > -1 && y < 8  && b.occupation(x,y) != Board.boolToOccupation(isWhite()) && !Main.checkContainsCoordinate(check, x, y) && b.occupation(x,y) != Board.Occupation.EMPTY)
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
          if (other instanceof Pawn pawn)
            otherMoves = pawn.getAttacks(b);
          else
            otherMoves = other.getPossibleMoves(b);

            if (!otherMoves.isEmpty())
                for (int c = 0; c < otherMoves.get(0).size(); c++)
                    if (otherMoves.get(0).get(c) == getX() && otherMoves.get(1).get(c) == getY())
                    return true;
        }
      }
    return false;
  }

  public boolean checkmate(Board b)
  {
    ArrayList<ArrayList<Integer>> otherMoves;
    for (int i = 0; i < 8; i++)  // check every piece on board
      for (int j = 0; j < 8; j++)
      {
        Piece other = b.getPiece(j,i);
        if (other != null && other.isWhite() != isWhite() && !(other instanceof King))  // if the piece is a different color and not king
        {
          if (other instanceof Pawn pawn)   // get moves
            otherMoves = pawn.getAttacks(b);
          else
            otherMoves = other.getPossibleMoves(b);

          for (int c = 0; c < otherMoves.get(0).size(); c++)  // loop through the moves
          {
            if (otherMoves.get(0).get(c) == getX() && otherMoves.get(1).get(c) == getY()) // check if the move attacks the king
              {
                ArrayList<ArrayList<Integer>> moves = getPossibleMoves(b);
                for (int m = 0; m < moves.get(0).size(); m++)  // Check if King can move out of check
                {
                  Board temp = b.copy();
                  temp.movePiece(getX(), getY(), moves.get(0).get(m), moves.get(1).get(m), true);
                  if (!check(temp))
                    return false;
                }

                // Check every piece on same team to see if it can block
                for (int g = 0; g < 8; g++)
                  for (int h = 0; h < 8; h++)
                  {
                    Piece team = b.getPiece(g,h);
                    if (team != null && team.isWhite() == isWhite() && !(other instanceof King))
                    {
                      ArrayList<ArrayList<Integer>> blocks = team.getPossibleMoves(b);
                      if (!blocks.isEmpty())
                        for (int m = 0; m < blocks.get(0).size(); m++)
                        {
                            Board temp = b.copy();
                            temp.movePiece(team.getX(), team.getY(), blocks.get(0).get(m), blocks.get(1).get(m), true);
                            if (!check(temp))
                            return false;
                        }
                    }
                  }
              }
          }
        } 
      }
    return true;
  }

  @Override
  public King copy()
  {
    return new King(getX(), getY(), isWhite());
  }

  @Override
    public int getPieceCode()
    {
        if (isWhite())
            return 6;
        return -6;
    }
  
  @Override
  public int getScore()
  {
	  return 10;
  }
}