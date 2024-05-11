/*
0 1 2 3 4 5 6 7
0 R k B Q K B k R
1 P P P P P P P P
2 
3 
4 
5 
6 P P P P P P P P
7 R k B Q K B k R
*/

import java.io.Serializable;
import java.util.*;

public class Board implements Serializable
{
    private Piece[][] pieces;
    private static final String[] pieceList = {"King","Queen","Rook","Rook","Knight","Knight","Bishop","Bishop"};

    public Board()
    {
        pieces = new Piece[8][8];
        //black pieces
        pieces[4][0] = new King(4,0,false);
        pieces[3][0] = new Queen(3,0,false);
        pieces[0][0] = new Rook(0,0,false);
        pieces[7][0] = new Rook(7,0,false);
        pieces[1][0] = new Knight(1,0,false);
        pieces[6][0] = new Knight(6,0,false);
        pieces[2][0] = new Bishop(2,0,false);
        pieces[5][0] = new Bishop(5,0,false);

        for (int i = 0; i < 8; i++)
            pieces[i][1] = new Pawn(i,1,false);

        //white pieces
        pieces[4][7] = new King(4,7,true);
        pieces[3][7] = new Queen(3,7,true);
        pieces[0][7] = new Rook(0,7,true);
        pieces[7][7] = new Rook(7,7,true);
        pieces[1][7] = new Knight(1,7,true);
        pieces[6][7] = new Knight(6,7,true);
        pieces[2][7] = new Bishop(2,7,true);
        pieces[5][7] = new Bishop(5,7,true);
        for (int i = 0; i < 8; i++)
            pieces[i][6] = new Pawn(i,6,true);
    }

    public Board(String variant)
    {
        pieces = new Piece[8][8];

        if (variant.equals("960"))
        {
            ArrayList<String> piecesLeft = new ArrayList<String>();
            Collections.addAll(piecesLeft, pieceList);
            int index;

            for (int i = 0; i < 8; i++)
            {
                index = (int) (Math.random() * (piecesLeft.size() - 1));
                String name = piecesLeft.remove(index);
                System.out.println(index + name);

                if (name.equals("Rook"))
                    pieces[i][0] = new Rook(i,0,false);
                else if (name.equals("Knight"))
                    pieces[i][0] = new Knight(i,0,false);
                else if (name.equals("Bishop"))
                    pieces[i][0] = new Bishop(i,0,false);
                else if (name.equals("King"))
                    pieces[i][0] = new King(i,0,false);
                else
                    pieces[i][0] = new Queen(i,0,false);

            }
            
            Collections.addAll(piecesLeft, pieceList);

            for (int j = 0; j < 8; j++)
            {
                index = (int) (Math.random() * (piecesLeft.size() - 1));
                String name = piecesLeft.remove(index);
                System.out.println(index + name);

                if (name.equals("Rook"))
                    pieces[j][7] = new Rook(j,0,true);
                else if (name.equals("Knight"))
                    pieces[j][7] = new Knight(j,0,true);
                else if (name.equals("Bishop"))
                    pieces[j][7] = new Bishop(j,0,true);
                else if (name.equals("King"))
                    pieces[j][7] = new King(j,0,true);
                else
                    pieces[j][7] = new Queen(j,0,true);
            }
            
            for (int i = 0; i < 8; i++)
                pieces[i][1] = new Pawn(i,1,false);

            for (int i = 0; i < 8; i++)
                pieces[i][6] = new Pawn(i,6,true);
        }
    } 

    public boolean isOccupied(int x, int y)
    {
        return x > 7 || x < 0 || y > 7 || y < 0 || pieces[x][y] != null;
    }

    // returns 0 if not occupied, 1 if white, 2 if black
    public int occupation(int x, int y)
    {
        if (pieces[x][y] == null)
            return 0;
        if (pieces[x][y].isWhite())
            return 1;
        return 2;
    }

    public String getPieceAbbr(int x, int y)
    {
        if (pieces[x][y] != null)
        return pieces[x][y].getAbbr();
        return " ";
    }

    public Piece getPiece(int x, int y)
    {
        return pieces[x][y];
    }

    public boolean inCheck(boolean white)
    {
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++)
            {
                Piece onBoard = pieces[x][y];
                if (onBoard != null && onBoard instanceof King)
                        return ((King) onBoard).check(this);
            }
        return false;
    }

    public void movePiece(int x1, int y1, int x2, int y2)
    {
        if (Main.atomic && pieces[x2][y2] != null)
        {
            for (int i = x2 - 1; i < x2 + 2; i++)
                for (int j = y2 - 1; j < y2 + 2; j++)
                    if (i < 8 && i > -1 && j < 8 && j > -1 && !(pieces[i][j] instanceof Pawn))
                        pieces[i][j] = null;
        }
        else
            pieces[x2][y2] = pieces[x1][y1];
        pieces[x1][y1] = null;
        System.out.println(toString());
    }

    public String toString()
    {
        String s = "";
        for (int y = 0; y < 8; y++)
        {
        for (int x = 0; x < 8; x++)
        {
            if (pieces[x][y] != null)
                s += getPieceAbbr(x,y);
            else
                s += " ";
        }
        s += "\n";
        }
        return s;
    }

    public void setPiece(int x, int y, Piece p)
    {
        pieces[x][y] = p;
    }
}