import javax.swing.*;
import java.util.ArrayList;

public abstract class Piece
{
  private int x;
  private int y;
  private boolean white;
  private String abbr;
  private ImageIcon icon;
  
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

  public boolean move(int xCoord, int yCoord, Board b)
  {
    ArrayList<ArrayList<Integer>> moves = getPossibleMoves(b);
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());

    boolean canMove = false;
    for (int i = 0; i < moves.get(0).size(); i++)
      if (moves.get(0).get(i) == xCoord && moves.get(1).get(i) == yCoord){
        canMove = true;}
    
    if (canMove)
    {
      b.movePiece(x, y, xCoord, yCoord);
      x = xCoord;
      y = yCoord;
      return true;
    }
    return false;
  }

  public boolean equals(Object o)
  {
    Piece other = (Piece) o;
    return x == other.x && y == other.y && abbr.equals(other.abbr) && white == other.white;
  }
}