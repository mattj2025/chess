import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Pawn extends Piece
{
    private boolean firstMove;

    public Pawn(int x, int y, boolean white)
    {
    super(x,y,white,"P","images\\pawn");
    firstMove = true;
    }

    // TODO - add en passant
    public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
    {
    ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());

    int i = 1;

    int x = getX();
    int y = getY();

    if (isWhite())
        i = -1;

    if (!b.isOccupied(x, y + i))
    {
      moves.get(0).add(x);
      moves.get(1).add(y + i);
      if ((firstMove || Main.torpedo) && !b.isOccupied(x, y + 2 * i))
      {
        moves.get(0).add(x);
        moves.get(1).add(y + 2 * i);
      }
    }
    if (x != 0 && b.isOccupied(x - 1, y + i) && b.occupation(x - 1, y + i) != Main.boolToInt(isWhite()) && !b.getPiece(x - 1, y + i).equals(new Duck(x - 1, y + i)) && b.occupation(x - 1, y + i) != 3)
    {
        moves.get(0).add(x - 1);
        moves.get(1).add(y  + i);
    }
    if (x != 7 && b.isOccupied(x + 1, y + i) && b.occupation(x + 1, y + i) != Main.boolToInt(isWhite()) && !b.getPiece(x + 1, y + i).equals(new Duck(x + 1, y + i)) && b.occupation(x - 1, y + i) != 3)
    {
        moves.get(0).add(x + 1);
        moves.get(1).add(y  + i);
    }

    if (Main.sideways)
    {
        if (x != 0 && !b.isOccupied(x - 1, y))
        {
            moves.get(0).add(x - 1);
            moves.get(1).add(y);
        }
        if (x != 7 && !b.isOccupied(x + 1, y))
        {
            moves.get(0).add(x + 1);
            moves.get(1).add(y);
        }
    }

    return moves;
  }

  public ArrayList<ArrayList<Integer>> getAttacks(Board b)
  {
    ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
    moves.add(new ArrayList<>());
    moves.add(new ArrayList<>());

    int i = 1;

    int x = getX();
    int y = getY();

    if (isWhite())
        i = -1;

    if (x != 0 && b.isOccupied(x - 1, y + i) && b.occupation(x - 1, y + i) != Main.boolToInt(isWhite()) && b.occupation(x - 1, y + i) != 3)
    {
        moves.get(0).add(x - 1);
        moves.get(1).add(y  + i);
    }
    if (x != 7 && b.isOccupied(x + 1, y + i) && b.occupation(x + 1, y + i) != Main.boolToInt(isWhite()) && b.occupation(x - 1, y + i) != 3)
    {
        moves.get(0).add(x + 1);
        moves.get(1).add(y  + i);
    }

    return moves;
  }

  public boolean move(int x, int y, Board b)
  {
    boolean r = super.move(x,y,b);
    if (y == 0)
    {
      String[] options = {"Queen", "Knight", "Rook", "Bishop"};
      int pick = JOptionPane.showOptionDialog(null,
                                              "Pick a piece",
                                              "Pick a piece",
                                              JOptionPane.YES_NO_CANCEL_OPTION,
                                              JOptionPane.QUESTION_MESSAGE,
                                              null,
                                              options,
                                              options[0]);

      if (pick == 0)
        b.setPiece(x,y,new Queen(x,y,isWhite()));
      else if (pick == 1)
        b.setPiece(x,y,new Knight(x,y,isWhite()));
      else if (pick == 2)
        b.setPiece(x,y,new Rook(x,y,isWhite()));
      else
        b.setPiece(x,y,new Bishop(x,y,isWhite()));
    }
    if (firstMove)
        firstMove = !r;
    return r;
  }
}