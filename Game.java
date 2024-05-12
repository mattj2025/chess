import java.io.Serializable;

public class Game implements Serializable 
{
    private static Board chess;
    private static boolean whiteTurn;
    private static boolean duck;
    private static boolean atomic;
    private static boolean torpedo;
    private static boolean sideways;
    private static boolean is960;
    private static boolean isCheckers;
    private static boolean isPre;
    private static int placeInt;
    private static boolean blindfold;
    private static boolean multiplayer;
    private static boolean isWhite;

    public Game()
    {
        chess = Main.chess;
        whiteTurn = Main.whiteTurn;
        duck = Main.duck;
        atomic = Main.atomic;
        torpedo = Main.torpedo;
        sideways = Main.sideways;
        is960 = Main.is960;
        isCheckers = Main.isCheckers;
        isPre = Main.isPre;
        placeInt = Main.placeInt;
        blindfold = Main.blindfold;
        multiplayer = Main.multiplayer;
        isWhite = Main.isWhite;
    }

    public void loadGame()
    {
        Main.chess = chess;
        Main.whiteTurn = whiteTurn;
        Main.duck = duck;
        Main.atomic = atomic;
        Main.torpedo = torpedo;
        Main.sideways = sideways;
        Main.is960 = is960;
        Main.isCheckers = isCheckers;
        Main.isPre = isPre;
        Main.placeInt = placeInt;
        Main.blindfold = blindfold;
        Main.multiplayer = multiplayer;
        Main.isWhite = isWhite;
        Main.reloadBoard(Main.buttons, chess);
    }
}
