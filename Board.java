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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

public class Board implements Serializable
{
    private final Piece[][] pieces;
    public static final String[] pieceList = {"King","Queen","Rook","Rook","Knight","Knight","Bishop","Bishop"};
    private static final AudioFilePlayer player = new AudioFilePlayer();
    public enum Occupation {
        NULL,
        WHITE,
        BLACK,
        EMPTY,
        DUCK,
        TRAP
    }

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

        switch(variant) 
        {
            case "960" -> {
                ArrayList<String> piecesLeft = new ArrayList<>();
                Collections.addAll(piecesLeft, pieceList);
                int index;

                for (int i = 0; i < 8; i++)
                {
                    index = (int) (Math.random() * (piecesLeft.size() - 1));
                    String name = piecesLeft.remove(index);

                    switch (name) {
                        case "Rook":
                            pieces[i][0] = new Rook(i,0,false);
                            break;
                        case "Knight":
                            pieces[i][0] = new Knight(i,0,false);
                            break;
                        case "Bishop":
                            pieces[i][0] = new Bishop(i,0,false);
                            break;
                        case "King":
                            pieces[i][0] = new King(i,0,false);
                            break;
                        default:
                            pieces[i][0] = new Queen(i,0,false);
                            break;
                    }
                }
                
                Collections.addAll(piecesLeft, pieceList);

                for (int j = 0; j < 8; j++)
                {
                    index = (int) (Math.random() * (piecesLeft.size() - 1));
                    String name = piecesLeft.remove(index);
                    System.out.println(index + name);

                    switch (name) {
                        case "Rook":
                            pieces[j][7] = new Rook(j,0,true);
                            break;
                        case "Knight":
                            pieces[j][7] = new Knight(j,0,true);
                            break;
                        case "Bishop":
                            pieces[j][7] = new Bishop(j,0,true);
                            break;
                        case "King":
                            pieces[j][7] = new King(j,0,true);
                            break;
                        default:
                            pieces[j][7] = new Queen(j,0,true);
                            break;
                    }
                }
                
                for (int i = 0; i < 8; i++)
                    pieces[i][1] = new Pawn(i,1,false);

                for (int i = 0; i < 8; i++)
                    pieces[i][6] = new Pawn(i,6,true);
            }
            case "Checkers" -> {
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
            case "preChess" -> {
                for (int i = 0; i < 8; i++)
                    pieces[i][1] = new Pawn(i,1,false);

                for (int i = 0; i < 8; i++)
                    pieces[i][6] = new Pawn(i,6,true);
            }
            case "bad" -> {
                Boolean white = false;
                int index;
                for (int j = 0; j < 8; j += 6)
                {
                    int multiplier = 1;
                    for (int i = 0; i < 8; i++)
                    {
                        index = (int) (Math.random() * 8);
                        String name = pieceList[index];

                        switch (name) {
                            case "Rook":
                                pieces[i][j] = new Rook(i, j, white);
                                break;
                            case "Knight":
                                pieces[i][j] = new Knight(i, j, white);
                                break;
                            case "Bishop":
                                pieces[i][j] = new Bishop(i, j, white);
                                break;
                            case "King":
                                pieces[i][j] = new Pawn(i, j, white);
                                break;
                            default:
                                pieces[i][j] = new Queen(i, j, white);
                                break;
                        }
                            
                        index = (int) (Math.random() * 8);
                        name = pieceList[index];

                        switch (name) {
                            case "Rook":
                                pieces[i][j + multiplier] = new Rook(i, j + multiplier, white);
                                break;
                            case "Knight":
                                pieces[i][j + multiplier] = new Knight(i, j + multiplier, white);
                                break;
                            case "Bishop":
                                pieces[i][j + multiplier] = new Bishop(i, j + multiplier, white);
                                break;
                            case "King":
                                pieces[i][j + multiplier] = new Pawn(i, j + multiplier, white);
                                break;
                            default:
                                pieces[i][j + multiplier] = new Queen(i, j + multiplier, white);
                                break;
                        }
                    }
                    white = true;
                }
                pieces[4][0] = new King(4,0,false);
                pieces[4][7] = new King(4,7,true);
            }
            case "crossDerby" -> {
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
                pieces[0][7] = new King(0,7,true);
                
                
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
    } 

    public boolean isOccupied(int x, int y)
    {
        return x > 7 || x < 0 || y > 7 || y < 0 || (pieces[x][y] != null || (pieces[x][y] instanceof Trap));
    }

    /**
     * Returns an Occupation enum (<code>BLACK</code>, <code>WHITE</code>, <code>NULL</code>, <code>EMPTY</code>, <code>DUCK</code>)
     * @param x The x coordinate of the piece
     * @param y The y coordinate of the piece
     * @return Occupation enum defining the color of the piece
     */
    public Occupation occupation(int x, int y)
    {
        try
        { 
            if (pieces[x][y] == null)
                return Occupation.NULL;
            return pieces[x][y].getOccupation();
        } 
        catch (ArrayIndexOutOfBoundsException e)
        {
            return Occupation.NULL;
        }
    }

    /**
     * Converts a boolean of the color to an Occupation enum
     * @param b <code>true</code>: <code>WHITE</code>, <code>false</code>: <code>BLACK</code>
     * @return the enum version of the variable
     */
    public static Occupation boolToOccupation(boolean b)
    {
        if (b)
            return Occupation.WHITE;
        return Occupation.BLACK;
    }

    /**
     * Converts <code>WHITE</code> to <code>BLACK</code> and vice versa. Basically the "!" operator
     * @param o Occupation of the piece to negate
     * @return The opposite occupation of the piece
     */
    public static Occupation negate(Occupation o)
    {
        return switch (o) {
            case Occupation.BLACK -> Occupation.WHITE;
            case Occupation.WHITE -> Occupation.BLACK;
            default -> Occupation.NULL;
        };
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
                if (onBoard != null && onBoard instanceof King && onBoard.isWhite() == white)
                    return ((King) onBoard).check(this);
            }
        return false;
    }

    public boolean inCheckMate(boolean white)
    {
        if (!inCheck(white))
            return false;

        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++)
            {
                Piece onBoard = pieces[x][y];
                if (onBoard != null && onBoard instanceof King)
                    return ((King) onBoard).checkmate(this);
            }
        return false;
    }

    public boolean bothKingsInPlay()
    {
        int ct = 0;

        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++)
                if (pieces[x][y] != null && pieces[x][y] instanceof King)
                    ct++;
                
        return ct == 2;
    }

    public ArrayList<ArrayList<Integer>> getPawns(Occupation o)
    {
        ArrayList<ArrayList<Integer>> pawns = new ArrayList<>();
        pawns.add(new ArrayList<>());
        pawns.add(new ArrayList<>());

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (pieces[i][j] != null && pieces[i][j] instanceof Pawn && pieces[i][j].getOccupation() == o) {
                    pawns.get(0).add(i);
                    pawns.get(1).add(j);
                }

        return pawns;
    }

    public ArrayList<ArrayList<Integer>> getPieces(Occupation o)
    {
        ArrayList<ArrayList<Integer>> p = new ArrayList<>();
        p.add(new ArrayList<>());
        p.add(new ArrayList<>());

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (pieces[i][j] != null && pieces[i][j].getOccupation() == o && !(pieces[i][j] instanceof King)) {
                    p.get(0).add(i);
                    p.get(1).add(j);
                }

        return p;
    }

    public ArrayList<ArrayList<Integer>> getChessmen(Occupation o)
    {
        ArrayList<ArrayList<Integer>> p = new ArrayList<>();
        p.add(new ArrayList<>());
        p.add(new ArrayList<>());

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (pieces[i][j] != null && pieces[i][j].getOccupation() == o && !(pieces[i][j] instanceof King) && !(pieces[i][j] instanceof Pawn)) {
                    p.get(0).add(i);
                    p.get(1).add(j);
                }

        return p;
    }

    public ArrayList<ArrayList<Integer>> getBlankTiles()
    {
        ArrayList<ArrayList<Integer>> p = new ArrayList<>();
        p.add(new ArrayList<>());
        p.add(new ArrayList<>());

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (pieces[i][j] == null) {
                    p.get(0).add(i);
                    p.get(1).add(j);
                }

        return p;
    }

    /**
     * Moves a piece
     * @param x1 the current x coord
     * @param y1 the current y coord
     * @param x2 the x coord to move to
     * @param y2 the y coord to move to
     * @param test if it is a test move(don't play sounds)
     */
    public void movePiece(int x1, int y1, int x2, int y2, boolean test)
    {
        if (pieces[x2][y2] instanceof Trap)
        {
            Main.player.play("sounds\\trap.wav");
        }
        else if (Main.atomic && pieces[x2][y2] != null) // Blow up all non-pawns in surrounding squares for atomic
        {
            int ct = 0;
            for (int i = x2 - 1; i < x2 + 2; i++)
                for (int j = y2 - 1; j < y2 + 2; j++)
                    if (i < 8 && i > -1 && j < 8 && j > -1 && !(pieces[i][j] instanceof Pawn) && pieces[i][j] != null)
                    {
                        ct++;
                        pieces[i][j] = null;
                    }
            if (ct > 2 && !test)
                player.play("sounds\\bomb.wav");

            pieces[x2][y2] = null;
        }
        else
            pieces[x2][y2] = pieces[x1][y1];  // move piece

        pieces[x1][y1] = null;

        if (!test)
            System.out.println(String.format("%s moved from (%d, %d) to (%d, %d)\nBoard:\n%s", pieces[x2][y2],x1,y1,x2,y2,toString()));
    }

    @Override
    public String toString()
    {
        toNN();
        String s = "";
        for (int y = 0; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                s += getPieceAbbr(x,y);
            }
            s += "\n";
        }
        return s;
    }

    public void setPiece(int x, int y, Piece p)
    {
        pieces[x][y] = p;
    }

    public Board copy() 
    {
        Board temp = new Board("");
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++)
                if (pieces[x][y] != null)
                    temp.setPiece(x, y, pieces[x][y].copy());

        return temp;
    }

    public int[] toNN()
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++)
                if (pieces[x][y] != null)
                    list.add(pieces[x][y].getPieceCode());
                else
                    list.add(0);

        try{ 
            PrintWriter p = new PrintWriter("s.txt");
            p.println(list); 
            p.close();
        } catch(FileNotFoundException e) {}
        

        return list.stream().mapToInt(i -> i).toArray();
    }
}