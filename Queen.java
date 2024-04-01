import java.util.ArrayList;

public class Queen extends Piece
{
  public Queen(int x, int y, boolean white)
  {
    super(x,y,white,"Q","images\\queen");
  }

  public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
  {
    ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());
    
    Boolean obstructed = false;
    int xCoord = getX() + 1;
    int yCoord = getY();
    

    // horizontal and vertical

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

    // diagonal

    obstructed = false;
    xCoord = getX() + 1;
    yCoord = getY() + 1;
    
    if (xCoord != 8 || yCoord != -1)
      while (!obstructed) // right up
      {
        if (xCoord == 8 || yCoord == 8 || b.occupation(xCoord, yCoord) == Main.boolToInt(isWhite()))
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
        yCoord++;
      }

    xCoord = getX() - 1;
    yCoord = getY() - 1;
    obstructed = false;
      
    if (xCoord != -1 || yCoord != -1)
      while (!obstructed) // left up
      {
        if (xCoord == -1 || yCoord == -1 || b.occupation(xCoord, yCoord) == Main.boolToInt(isWhite()))
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
        yCoord--;
      }
      
    xCoord = getX() + 1;
    yCoord = getY() - 1;
    obstructed = false;

    if (xCoord != 8 || yCoord != 8)
      while (!obstructed) // right down
      {
        if (yCoord == -1 || xCoord == 8 || b.occupation(xCoord, yCoord) == Main.boolToInt(isWhite()))
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
        yCoord--;
      }
    
    xCoord = getX() - 1;
    yCoord = getY() + 1;
    obstructed = false;

    if (xCoord != -1 || yCoord != 8)
      while (!obstructed) // left down
      {
        if (yCoord == 8 || xCoord == -1 || b.occupation(xCoord, yCoord) == Main.boolToInt(isWhite()))
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
        xCoord--;
      }

    return moves;
  }
}