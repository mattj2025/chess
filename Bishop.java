import java.util.ArrayList;

public class Bishop extends Piece
{
  public Bishop(int x, int y, boolean white)
  {
    super(x,y,white,"B","images\\bishop");
  }

  @Override
  public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
  {
    ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());

    Boolean obstructed = false;
    int xCoord = getX() + 1;
    int yCoord = getY() + 1;
    
    if (xCoord != 8 || yCoord != -1)
      while (!obstructed) // right up
      {
        if (xCoord == 8 || yCoord == 8 || b.occupation(xCoord, yCoord) == Board.boolToOccupation(isWhite()))
          obstructed = true;
        else if (b.isOccupied(xCoord, yCoord) && b.occupation(xCoord, yCoord) != Board.boolToOccupation(isWhite()))
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
        yCoord++;
      }

    xCoord = getX() - 1;
    yCoord = getY() - 1;
    obstructed = false;
      
    if (xCoord != -1 || yCoord != -1)
      while (!obstructed) // left up
      {
        if (xCoord == -1 || yCoord == -1 || b.occupation(xCoord, yCoord) == Board.boolToOccupation(isWhite()))
          obstructed = true;
        else if (b.isOccupied(xCoord, yCoord) && b.occupation(xCoord, yCoord) != Board.boolToOccupation(isWhite()))
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
        yCoord--;
      }
      
    xCoord = getX() + 1;
    yCoord = getY() - 1;
    obstructed = false;

    if (xCoord != 8 || yCoord != 8)
      while (!obstructed) // right down
      {
        if (yCoord == -1 || xCoord == 8 || b.occupation(xCoord, yCoord) == Board.boolToOccupation(isWhite()))
          obstructed = true;
        else if (b.isOccupied(xCoord, yCoord) && b.occupation(xCoord, yCoord) != Board.boolToOccupation(isWhite()))
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
        yCoord--;
      }
    
    xCoord = getX() - 1;
    yCoord = getY() + 1;
    obstructed = false;

    if (xCoord != -1 || yCoord != 8)
      while (!obstructed) // left down
      {
        if (yCoord == 8 || xCoord == -1 || b.occupation(xCoord, yCoord) == Board.boolToOccupation(isWhite()))
          obstructed = true;
        else if (b.isOccupied(xCoord, yCoord) && b.occupation(xCoord, yCoord) != Board.boolToOccupation(isWhite()))
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
        xCoord--;
      }

    return moves;
  }

  @Override
  public Bishop copy()
  {
    return new Bishop(getX(), getY(), isWhite());
  }

  @Override
  public int getPieceCode()
  {
    if (isWhite())
      return 1;
    return -1;
  }
  
  @Override
  public int getScore()
  {
	  return 3;
  }
}