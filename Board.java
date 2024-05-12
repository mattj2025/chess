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
        else if (variant.equals("checkers"))
        {
            for (int j = 0; j < 3; j++)
            {
                for (int i = 0; i < 8; i++)
                {
                    if (i % 2 == 1 && j % 2 == 1)
                        pieces[i][j] = new CheckersPiece(i,j,false);
                    else if (i % 2 == 0 && j % 2 == 0)
                        pieces[i][j] = new CheckersPiece(i,j,false);
                }
            }

            for (int j = 5; j < 8; j++)
            {
                for (int i = 0; i < 8; i++)
                {
                    if (i % 2 == 1 && j % 2 == 1)
                        pieces[i][j] = new CheckersPiece(i,j,true);
                    else if (i % 2 == 0 && j % 2 == 0)
                        pieces[i][j] = new CheckersPiece(i,j,true);
                }
            }
        }
        else if(variant.equals("preChess"))
        {
            for (int i = 0; i < 8; i++)
                pieces[i][1] = new Pawn(i,1,false);

            for (int i = 0; i < 8; i++)
                pieces[i][6] = new Pawn(i,6,true);
        }
        else if (variant.equals("bad"))
        {
            Boolean white = false;

            for (int j = 0; j < 8; j += 6)
            {
                int multiplier = 1;
                for (int i = 0; i < 8; i++)
                {
                    int index = (int) (Math.random() * 8);
                    String name = pieceList[index];

                    if (name.equals("Rook"))
                        pieces[i][j] = new Rook(i, j, white);
                    else if (name.equals("Knight"))
                        pieces[i][j] = new Knight(i, j, white);
                    else if (name.equals("Bishop"))
                        pieces[i][j] = new Bishop(i, j, white);
                    else if (name.equals("King"))
                        pieces[i][j] = new Pawn(i, j, white);
                    else
                        pieces[i][j] = new Queen(i, j, white);
                        
                    index = (int) (Math.random() * 8);
                    name = pieceList[index];

                    if (name.equals("Rook"))
                        pieces[i][j + multiplier] = new Rook(i, j + multiplier, white);
                    else if (name.equals("Knight"))
                        pieces[i][j + multiplier] = new Knight(i, j + multiplier, white);
                    else if (name.equals("Bishop"))
                        pieces[i][j + multiplier] = new Bishop(i, j + multiplier, white);
                    else if (name.equals("King"))
                        pieces[i][j + multiplier] = new Pawn(i, j + multiplier, white);
                    else
                        pieces[i][j + multiplier] = new Queen(i, j + multiplier, white);

                    System.out.println(toString());
                }
                multiplier = -1;
                white = true;
            }

            pieces[4][0] = new King(4,0,false);
            pieces[4][7] = new King(4,7,true);
        }
        else if (variant.equals("crossDerby"))
        {
            pieces[0][0] = new Knight(0,0,false);
            pieces[7][7] = new Knight(7,7,true);

            for (int i = 2; i < 6; i++)
            {
                pieces[i][0] = new Knight(i,0,false);
                pieces[i][1] = new Pawn(i,1,false);
                pieces[i][7] = new Knight(i,7,true);
                pieces[i][6] = new Pawn(i,6,true);
            }
            pieces[7][0] = new King(7,0,false);
            pieces[0][7] = new Knight(0,7,true);


            pieces[0][2] = new Pawn(0,2,false);
            pieces[1][2] = new Pawn(1,2,false);
            pieces[6][2] = new Pawn(6,2,false);
            pieces[7][2] = new Pawn(7,2,false);
            pieces[0][5] = new Pawn(0,5,true);
            pieces[1][5] = new Pawn(1,5,true);
            pieces[6][5] = new Pawn(6,5,true);
            pieces[7][5] = new Pawn(7,5,true);

            pieces[0][1] = new Empty(0,1);
            pieces[1][0] = new Empty(1,0);
            pieces[1][1] = new Empty(1,1);
            pieces[0][6] = new Empty(0,6);
            pieces[1][6] = new Empty(1,6);
            pieces[1][7] = new Empty(1,7);
            pieces[6][0] = new Empty(6,0);
            pieces[6][1] = new Empty(6,1);
            pieces[7][1] = new Empty(7,1);
            pieces[6][6] = new Empty(6,6);
            pieces[7][6] = new Empty(7,6);
            pieces[6][7] = new Empty(6,7);
        }
    } 

    public boolean isOccupied(int x, int y)
    {
        return x > 7 || x < 0 || y > 7 || y < 0 || pieces[x][y] != null;
    }

    // returns 0 if not occupied, 1 if white, 2 if black, 3 if empty
    public int occupation(int x, int y)
    {
        if (pieces[x][y] == null)
            return 0;
        if (pieces[x][y].isWhite())
            return 1;
        if (pieces[x][y] instanceof Empty)
            return 3;
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