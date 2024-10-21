import java.util.ArrayList;

public class CheckersKingPiece extends Piece
{
    public CheckersKingPiece(int x, int y, boolean white)
    {
        super(x,y, white, "C", "images\\checkersKing");
    }

    public ArrayList<ArrayList<Integer>> getPossibleMoves(Board b)
    {
        ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
        moves.add(new ArrayList<>());
        moves.add(new ArrayList<>());

        int x = getX();
        int y = getY();

        int yMultiplier = 1;

        for (int i = 0; i < 2; i++)
        {
            if (x > 0 && !b.isOccupied(x - 1, y + 1 * yMultiplier))
            {
                moves.get(0).add(x - 1);
                moves.get(1).add(y + 1 * yMultiplier);
            }
            else if (x > 1 && !b.isOccupied(x - 2, y + 2 * yMultiplier) && b.occupation(x - 1, y + 1 * yMultiplier) == Board.boolToOccupation(!isWhite()))
            {
                moves.get(0).add(x - 2);
                moves.get(1).add(y + 2 * yMultiplier);
            }

            if (x < 8 && !b.isOccupied(x + 1, y + 1 * yMultiplier))
            {
                moves.get(0).add(x + 1);
                moves.get(1).add(y + 1 * yMultiplier);
            }
            else if (x < 7 && !b.isOccupied(x + 2, y + 2 * yMultiplier) && b.occupation(x + 1, y + 1 * yMultiplier) == Board.boolToOccupation(!isWhite()))
            {
                moves.get(0).add(x + 2);
                moves.get(1).add(y + 2 * yMultiplier);
            }
            yMultiplier -= 2;
        }

        return moves;
    }

    @Override
    public CheckersKingPiece copy()
    {
        return new CheckersKingPiece(getX(), getY(), isWhite());
    }

    @Override
    public int getPieceCode()
    {
        if (isWhite())
            return 2;
        return -2;
    }
   
    @Override
    public int getScore()
    {
  	  return 9;
    }
}
