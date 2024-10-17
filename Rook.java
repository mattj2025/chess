import java.util.ArrayList;

public class Rook extends Piece
{
  public Rook(int x, int y, boolean white)
  {
    super(x,y,white,"R","images\\rook");
  }

  @Override
  public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
  {
    ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());

    Boolean obstructed = false;
    int xCoord = getX() + 1;
    int yCoord = getY();
    
    while (!obstructed) // x going right
    {
      if (xCoord == 8 || (b.occupation(xCoord, yCoord) == Main.boolToInt(isWhite())))
        obstructed = true;
      else if (b.isOccupied(xCoord, yCoord) && b.occupation(xCoord, yCoord) != Main.boolToInt(isWhite()))
      {
        obstructed = true;
        moves.get(0).add(xCoord);
        moves.get(1).add(yCoord);
      }
      else
      {
        moves.get(0).add(xCoord);
        moves.get(1).add(yCoord);
      }
      xCoord++;
    }

    xCoord = getX() - 1;
    obstructed = false;
      
    while (!obstructed) // x going left
    {
      if (xCoord == -1 || (b.occupation(xCoord, yCoord) == Main.boolToInt(isWhite())))
        obstructed = true;
      else if (b.isOccupied(xCoord, yCoord) && b.occupation(xCoord, yCoord) != Main.boolToInt(isWhite()))
      {
        obstructed = true;
        moves.get(0).add(xCoord);
        moves.get(1).add(yCoord);
      }
      else
      {
        moves.get(0).add(xCoord);
        moves.get(1).add(yCoord);
      }
      xCoord--;
    }
    
    xCoord = getX();
    yCoord = getY() - 1;
    obstructed = false;

    while (!obstructed) // y going up
    {
      if (yCoord == -1 || (b.occupation(xCoord, yCoord) == Main.boolToInt(isWhite())))
        obstructed = true;
      else if (b.isOccupied(xCoord, yCoord) && b.occupation(xCoord, yCoord) != Main.boolToInt(isWhite()))
      {
        obstructed = true;
        moves.get(0).add(xCoord);
        moves.get(1).add(yCoord);
      }
      else
      {
        moves.get(0).add(xCoord);
        moves.get(1).add(yCoord);
      }
      yCoord--;
    }
    
    yCoord = getY() + 1;
    obstructed = false;

    while (!obstructed) // y going down
    {
      if (yCoord == 8 || (b.occupation(xCoord, yCoord) == Main.boolToInt(isWhite())))
        obstructed = true;
      else if (b.isOccupied(xCoord, yCoord) && b.occupation(xCoord, yCoord) != Main.boolToInt(isWhite()))
      {
        obstructed = true;
        moves.get(0).add(xCoord);
        moves.get(1).add(yCoord);
      }
      else
      {
        moves.get(0).add(xCoord);
        moves.get(1).add(yCoord);
      }
      yCoord++;
    }

    return moves;
  }

  @Override
  public Rook copy()
  {
    return new Rook(getX(), getY(), isWhite());
  }

  @Override
    public int getPieceCode()
    {
        if (isWhite())
            return 10;
        return -10;
    }
  
  @Override
  public int getScore()
  {
	  return 5;
  }
}