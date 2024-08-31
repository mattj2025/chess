import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.*;

public abstract class Piece implements Serializable
{
  private int x;
  private int y;
  private final boolean white;
  private final String abbr;
  private final ImageIcon icon;
  
  public Piece(int xCoord, int yCoord, boolean w, String s, String filepath)
  {
    x = xCoord;
    y = yCoord;
    white = w;
    abbr = s;
    if (white)
      icon = new ImageIcon(filepath + "White.png");
    else
      icon = new ImageIcon(filepath + "Black.png");
  }

  public String getAbbr()
  {
    return abbr;
  }

  public ImageIcon getIcon()
  {
    return icon;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public boolean isWhite()
  {
    return white;
  }

  //public abstract int[][] getPossibleMoves(Board b);
  public abstract ArrayList<ArrayList<Integer>> getPossibleMoves(Board b);

  public boolean move(int xCoord, int yCoord, Board b, boolean test)
  {
    Board temp = b.copy();
    temp.movePiece(x, y, xCoord, yCoord, true);
    if (temp.inCheck(Main.whiteTurn))  // Check to make sure moving the piece won't lead to check
      return false;

    ArrayList<ArrayList<Integer>> moves = getPossibleMoves(b);
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());

    boolean canMove = false;

    for (int i = 0; i < moves.get(0).size(); i++)
      if (moves.get(0).get(i) == xCoord && moves.get(1).get(i) == yCoord)
        canMove = true;

    if (canMove)
    {
      b.movePiece(x, y, xCoord, yCoord, (false || test));
      x = xCoord;
      y = yCoord;
      return true;
    }
    
    return false;
  }

  @Override
  public boolean equals(Object o)
  {
    Piece other = (Piece) o;
    return x == other.x && y == other.y && abbr.equals(other.abbr) && white == other.white;
  }

  public ArrayList<ArrayList<Integer>> removeIllegalMoves(ArrayList<ArrayList<Integer>> illegalMoves, Board b)
  {
    if (!b.inCheck(white))
      return illegalMoves;

    ArrayList<ArrayList<Integer>> legalMoves = new ArrayList<>();

    for (int i = 0; i < illegalMoves.get(0).size(); i++)
    {
      for (int m = 0; m < illegalMoves.get(0).size(); m++)
      {
        Board temp = b.copy();
        temp.movePiece(x, y, illegalMoves.get(0).get(m), illegalMoves.get(1).get(m), true);
        if (temp.inCheck(white))
        {
          legalMoves.get(0).add(illegalMoves.get(0).get(i));
          legalMoves.get(1).add(illegalMoves.get(1).get(i));
        }
      }
    }

    return legalMoves;
  }

  public abstract Piece copy();
}