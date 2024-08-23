import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class Main {
    private static final int BUTTON_SIZE = 80;
    public static Board chess;
    public static Main mainInstance = new Main();
    public static JButton[][] buttons;
    public static JFrame jFrame;
    public static boolean whiteTurn = true;
    public static boolean selected = false;
    public static boolean moveDuck = false;
    public static boolean duck = false;  // TODO: when duck mode is played with local multiplayer the second player cannot move
    public static boolean atomic = false;
    public static boolean torpedo = false;
    public static boolean sideways = false;
    public static boolean is960 = false;
    public static boolean isCheckers = false;
    public static boolean isPre = false;
    public static boolean blindfold = false;
    public static boolean multiplayer = false;
    public static boolean isWhite = false;
    public static boolean isHost = false;
    public static boolean isBad = false;
    public static boolean isDerby = false;
    public static boolean sendBoard = false;
    public static Host host = new Host();
    public static Client client = new Client();
    public static Component joinB;
    public static Component hostB;
    public static Component disconnectB;
    public static Component resetB;
    public static Component reloadB;
    public static Component pastMoves;
    public static String piece;
    public static int x = 0;
    public static int y = 0;
    public static int placeInt = 0;
    public static int theme = 0;
    public static Color[] background = {Color.WHITE, Color.DARK_GRAY};
    public static Color[] tileA = {Color.WHITE, Color.LIGHT_GRAY};
    public static Color[] tileB = {Color.LIGHT_GRAY, Color.DARK_GRAY};
    public static Color[] movesColors = {Color.YELLOW, Color.YELLOW};
    public static Color[] selectColors = {Color.RED, Color.RED};
    public static Color[] textColors = {Color.DARK_GRAY, Color.LIGHT_GRAY};
    public static Color[] emptyColors = {Color.BLACK, Color.BLACK};

    public static void main(String[] args) throws IOException {
        chess = new Board();
        createAndShowGUI();
        System.out.println(chess);
    }

    private static void createAndShowGUI() throws IOException {
        jFrame = new JFrame("Chess");
        jFrame.setLayout(new FlowLayout());
        jFrame.setSize(700, 840);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setBackground(background[theme]);

        JLabel title = new JLabel("Chess");
        title.setFont(new Font("Arial", Font.BOLD, 38));
        title.setForeground(textColors[theme]);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu help = new JMenu("Help");

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(help);

        JMenuItem save = new JMenuItem("Save as");
        save.addActionListener(new Save());
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(new Load());
        JMenu colorPicker = new JMenu("Choose Color Theme");
        
        JMenuItem original = new JMenuItem("Original");
        original.addActionListener((ActionEvent e) -> {
            theme = 0;
            reloadBoard(buttons, chess);
        });

        JMenuItem dark = new JMenuItem("Dark");
        dark.addActionListener((ActionEvent e) -> {
            theme = 1;
            reloadBoard(buttons, chess);
        });

        colorPicker.add(original);
        colorPicker.add(dark);

        JMenuItem tutorial = new JMenuItem("Tutorial");
        tutorial.addActionListener((ActionEvent e) -> {
            File file1 = new File("resources\\chessRules.pdf");
            if (file1.exists() && Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(file1.toURI());
                }catch (IOException e1) {
                    System.out.println("Oopsies");
                }
            }
        });

        JMenuItem credits = new JMenuItem("Credits");
        credits.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(jFrame, "Created by Matt Johnson");
        });

        file.add(save);
        file.add(load);
        edit.add(colorPicker);
        help.add(tutorial);
        help.add(credits);

        JCheckBox duckBox = new JCheckBox("Duck");
        duckBox.setSelected(duck);
        duckBox.addItemListener((ItemEvent e) -> {
            duck = !atomic;
        });

        JCheckBox atomicBox = new JCheckBox("Atomic");
        atomicBox.setSelected(atomic);
        atomicBox.addItemListener((ItemEvent e) -> {
            atomic = !atomic;
        });

        JCheckBox torpedoBox = new JCheckBox("Torpedo");
        torpedoBox.setSelected(torpedo);
        torpedoBox.addItemListener((ItemEvent e) -> {
            torpedo = !torpedo;
        });

        JCheckBox sidewaysBox = new JCheckBox("Sideways");
        sidewaysBox.setSelected(sideways);
        sidewaysBox.addItemListener((ItemEvent e) -> {
            sideways = !sideways;
        });

        JCheckBox is960Box = new JCheckBox("960");
        is960Box.setSelected(is960);
        is960Box.addItemListener((ItemEvent e) -> {
            is960 = !is960;
        });

        JCheckBox isCheckersBox = new JCheckBox("Checkers");
        isCheckersBox.setSelected(isCheckers);
        isCheckersBox.addItemListener((ItemEvent e) -> {
            isCheckers = !isCheckers;
        });

        JCheckBox isPreBox = new JCheckBox("Pre-Chess");
        isPreBox.setSelected(isPre);
        isPreBox.addItemListener((ItemEvent e) -> {
            isPre = !isPre;
        });
        
        JCheckBox isBadBox = new JCheckBox("Really Bad");
        isBadBox.setSelected(isBad);
        isBadBox.addItemListener((ItemEvent e) -> {
            isBad = !isBad;
        });

        JCheckBox isDerbyBox = new JCheckBox("Cross Derby");
        isDerbyBox.setSelected(isDerby);
        isDerbyBox.addItemListener((ItemEvent e) -> {
            isDerby = !isDerby;
        });

        JCheckBox blindfoldBox = new JCheckBox("Blindfold");
        blindfoldBox.setSelected(blindfold);
        blindfoldBox.addItemListener((ItemEvent e) -> {
            blindfold = !blindfold;
            reloadBoard(buttons,chess);
        });

        JButton hostButton = new JButton("Host");
        hostButton.setPreferredSize(new Dimension(100,40));
        hostButton.setBackground(background[theme]);
        hostButton.addActionListener(host);
        hostB = hostButton;

        JButton joinButton = new JButton("Join");
        joinButton.setPreferredSize(new Dimension(100,40));
        joinButton.setBackground(background[theme]);
        joinButton.addActionListener(client);
        joinB = joinButton;

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setPreferredSize(new Dimension(100,40));
        disconnectButton.setBackground(background[theme]);
        disconnectButton.addActionListener(new Disconnect());
        disconnectB = disconnectButton;

        /*JTextArea moves = new JTextArea("");
        moves.setFont(new Font("Arial", Font.PLAIN, 18));
        pastMoves = moves;*/

        JPanel panel = new JPanel(new GridLayout(8, 8));
        buttons = new JButton[8][8];


        for (y = 0; y < 8; y++)
            for (x = 0; x < 8; x++) 
            {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                if (chess.getPiece(x,y) != null && !blindfold)
                    button.setIcon(chess.getPiece(x,y).getIcon());
                button.addActionListener(new ButtonClickListener(x, y, chess));
                if ((y % 2 == 0 && x % 2 == 0) || (x % 2 == 1 && y % 2 == 1))
                    button.setBackground(tileA[theme]);
                else
                    button.setBackground(tileB[theme]);
                panel.add(button);
                buttons[x][y] = button;
            }

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100,40));
        resetButton.setBackground(background[theme]);
        resetButton.addActionListener(new Reset(jFrame,buttons,chess));
        resetB = resetButton;

        JButton reloadButton = new JButton("Reload");
        reloadButton.setPreferredSize(new Dimension(100,40));
        reloadButton.setBackground(background[theme]);
        reloadButton.addActionListener(new Reload(buttons,chess));
        reloadB = reloadButton;
    
        jFrame.setJMenuBar(menuBar);
        jFrame.add(title);
        jFrame.add(duckBox);
        jFrame.add(atomicBox);
        jFrame.add(torpedoBox);
        jFrame.add(sidewaysBox);
        jFrame.add(is960Box);
        jFrame.add(blindfoldBox);
        jFrame.add(isCheckersBox);
        jFrame.add(isPreBox);
        jFrame.add(isBadBox);
        jFrame.add(isDerbyBox);
        jFrame.add(panel);
        jFrame.add(resetButton);
        jFrame.add(reloadButton);
        jFrame.add(hostButton);
        jFrame.add(joinButton);
        //jFrame.add(moves);    // ugly
        jFrame.setVisible(true);
    }

    private static class Reset implements ActionListener 
    {
        private final JFrame f;

        public Reset(JFrame frame, JButton[][] buttons, Board chess)
        {
            f = frame;
            reloadBoard(buttons, chess);
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        { 
            if (is960)
                chess = new Board("960");
            else if (isCheckers)
                chess = new Board("checkers");
            else if (isPre)
                chess = new Board("preChess");
            else if (isBad)
                chess = new Board("bad");
            else if (isDerby)
                chess = new Board("crossDerby");
            else
                chess = new Board();

            selected = false;
            whiteTurn = true;
            placeInt = 0;

            try {
                createAndShowGUI();
            } catch (IOException e1) {
                System.out.println("Failed to display GUI");
            }
            f.dispose();

            if (isPre)
                JOptionPane.showMessageDialog(jFrame, "Placing Rooks");
        } 
    }

    private static class Reload implements ActionListener 
    {
        JButton[][] b;
        Board c;

        public Reload(JButton[][] buttons, Board chess)
        {
            b = buttons;
            c = chess;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        { 
            reloadBoard(b, c);
        } 
    }

    private static class Disconnect implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (multiplayer)
                try {
                    if (isHost)
                        host.disconnect();
                    else
                        client.disconnect();
                    System.out.println("Disconnected");
                } catch(IOException e1) {
                    System.out.println("Failed to Disconnect");
                }
        }
    }

    private static class ButtonClickListener implements ActionListener 
    {
        private final int row;
        private final int col;
        private Board chess;

        public ButtonClickListener(int row, int col, Board chess) 
        {
        this.row = row;
        this.col = col;
        this.chess = chess;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            System.out.println("Button clicked at row " + row + ", column " + col);

            if (isPre && placeInt < 16 && ((whiteTurn && col == 7) || (!whiteTurn && col == 0)))
            {
                if (placeInt < 4)
                    chess.setPiece(row,col, new Rook(row, col, whiteTurn));
                else if (placeInt < 8)
                    chess.setPiece(row,col, new Bishop(row, col, whiteTurn));
                else if (placeInt < 12)
                    chess.setPiece(row,col, new Knight(row, col, whiteTurn));
                else if (placeInt < 14)
                    chess.setPiece(row,col, new Queen(row, col, whiteTurn));
                else
                    chess.setPiece(row,col, new King(row, col, whiteTurn));

                whiteTurn = !whiteTurn;
                placeInt++;

                reloadBoard(buttons, chess);
                if (placeInt == 4)
                    JOptionPane.showMessageDialog(jFrame, "Placing Bishops");
                if (placeInt == 8)
                    JOptionPane.showMessageDialog(jFrame, "Placing Knights");
                if (placeInt == 12)
                    JOptionPane.showMessageDialog(jFrame, "Placing Queens");
                if (placeInt == 14)
                    JOptionPane.showMessageDialog(jFrame, "Placing Kings");
            }
            else if (moveDuck)
            {
                if (chess.getPiece(row,col) == null)
                {
                    for (int i = 0; i < 8; i++)
                        for (int j = 0; j < 8; j++)
                            if (chess.getPiece(i,j) != null && chess.getPiece(i,j).equals(new Duck(i,j)))
                            {
                                chess.setPiece(i,j,null);
                                if (!blindfold)
                                    buttons[i][j].setIcon(null);
                            }

                    chess.setPiece(row,col, new Duck(row,col));

                    if (!blindfold)
                        buttons[row][col].setIcon(chess.getPiece(row,col).getIcon());

                    moveDuck = false;
                    sendBoard = true;
                }
                else
                    System.out.println("Cannot move");
            }
            else if (selected)
            {
                if (chess.getPiece(x,y).move(row,col,chess) && (!multiplayer || whiteTurn == isWhite))
                {
                    // System.out.println(chess.getPieceAbbr(row, col) + ((char) (row + 97)) + col);
                    // ((JTextArea) pastMoves).setText(((JTextArea) pastMoves).getText() + "\n" + chess.getPiece(row,col).getAbbr() + ((char) (row + 97)) + col);    // super ugly currently
                    
                    buttons[x][y].setIcon(null);

                    if (chess.getPiece(row,col) != null && !blindfold)
                        buttons[row][col].setIcon(chess.getPiece(row,col).getIcon());

                    whiteTurn = !whiteTurn;

                    if (duck)
                    {
                        JOptionPane.showMessageDialog(jFrame, "Move the duck");
                        moveDuck = true;
                    }
                    if (atomic)
                    {
                        for (int i = 0; i < 8; i++)
                            for (int j = 0; j < 8; j++)
                            {
                                if (chess.getPiece(i,j) != null && !blindfold)
                                    buttons[i][j].setIcon(chess.getPiece(i,j).getIcon());
                                else
                                    buttons[i][j].setIcon(null);
                            }
                    }

                    if (isCheckers && (Math.abs(row - x) > 1))
                    {
                        chess.setPiece((row + x) / 2, (col + y) / 2 , null);
                        buttons[(row + x) / 2][(col + y) / 2].setIcon(null);
                    }

                    sendBoard = true;
                }
                else
                    System.out.println("Cannot Move");
                    
                for (int i = 0; i < 8; i++) 
                    for (int j = 0; j < 8; j++)
                        if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1))
                            buttons[i][j].setBackground(tileA[theme]);
                        else
                            buttons[i][j].setBackground(tileB[theme]);

                selected = false;

                // TODO - check if checkmate
            }
            else if (chess.isOccupied(row, col) && chess.getPiece(row,col).isWhite() == whiteTurn && !(chess.getPiece(row, col) instanceof Duck))
            {
                JButton clickedButton = buttons[row][col];
                
                for (int i = 0; i < 8; i++) 
                    for (int j = 0; j < 8; j++)
                        if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1))
                            buttons[i][j].setBackground(tileA[theme]);
                        else
                            buttons[i][j].setBackground(tileB[theme]);

                // TODO - clean the look of a finished game
                ArrayList<ArrayList<Integer>> possibleMoves = chess.getPiece(row,col).getPossibleMoves(chess);
                //if (chess.getPiece(row,col) instanceof King && possibleMoves.get(0).isEmpty() && chess.inCheck(whiteTurn))
                if (chess.inCheckMate(whiteTurn))
                {
                    System.out.println("Game Over");
                    String winner = "White";
                    if (whiteTurn)
                        winner = "Black";
                    JOptionPane.showMessageDialog(null, "Checkmate! " + winner + " won!");
                    chess = new Board();
                    selected = false;
                    whiteTurn = true;
                    try {
                        createAndShowGUI();
                    } catch (IOException e1) {
                        System.out.println("Failed to Display GUI");
                    }

                    sendBoard = true;
                }
                else if (!multiplayer || (isWhite == whiteTurn))
                {
                    for (int i = possibleMoves.get(0).size() - 1; i > -1; i--)
                    {
                        Board temp = chess.copy();
                        if (temp.getPiece(row,col).move(possibleMoves.get(0).get(i), possibleMoves.get(1).get(i), temp))
                            buttons[possibleMoves.get(0).get(i)][possibleMoves.get(1).get(i)].setBackground(movesColors[theme]);
                    }
                }

                if (!multiplayer || (isWhite == whiteTurn))
                    clickedButton.setBackground(selectColors[theme]);

                selected = true;
                x = row;
                y = col;
            }

            if (multiplayer && sendBoard)
            {
                try
                {
                    if (isHost)
                        host.sendBoard(chess);
                    else
                        client.sendBoard(chess);
                } catch (IOException e1) {
                    System.out.println("Failed to send board");
                }

                sendBoard = false;
            }
        }
    }

    public static int boolToInt(boolean b)
    {
        if (b)
            return 1;
        return 2;
    }

    public static boolean checkContainsCoordinate(ArrayList<ArrayList<Integer>> check, int x, int y) 
    {
        for (int i = 0; i < check.get(0).size(); i++) {
            if (check.get(0).get(i) == x && check.get(1).get(i) == y) {
                return true;
            }
        }
        return false;
    }

    public static void reloadBoard(JButton[][] buttons, Board b)
    {
        jFrame.setBackground(background[theme]);

        System.out.println(b);
        for (int i = 0; i < 8; i++) 
            for (int j = 0; j < 8; j++)
            {
                if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1))
                    buttons[i][j].setBackground(tileA[theme]);
                else
                    buttons[i][j].setBackground(tileB[theme]);

                if (chess.occupation(i,j) == 3)
                    buttons[i][j].setBackground(emptyColors[theme]);

                buttons[i][j].removeActionListener(buttons[i][j].getActionListeners()[0]);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j, chess));
            }

        if (!blindfold)
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                {
                    if (b.getPiece(i,j) != null)
                        buttons[i][j].setIcon(b.getPiece(i,j).getIcon());
                    else
                        buttons[i][j].setIcon(null);
                }
        else
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (chess.occupation(i, j) != 3)
                        buttons[i][j].setIcon(null);

    }
}