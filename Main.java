import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Main {
    private static final boolean SKIP_TITLE = true;
    private static final int BUTTON_SIZE = 80;
    public static final double EVENT_PROBABILITY = .925;
    public static final double EMPTY_REMOVE_PROBABILITY = .5;
    public static int[] emptyCoords = {-1,-1};
    public static Board chess;
    public static Main mainInstance = new Main();
    public static JButton[][] buttons;
    public static JFrame jFrame;
    public static JFrame startFrame;
    public static JLabel blackTime;
    public static JLabel whiteTime;
    public static boolean atTitleScreen = true;
    public static boolean muted = false;
    public static boolean whiteTurn = true;
    public static boolean gameOver = false;
    public static boolean selected = false;
    public static boolean moveDuck = false;
    public static boolean duck = false;  // TODO: when duck mode is played with local multiplayer the second player cannot move
    public static boolean atomic = false;
    public static boolean torpedo = false;
    public static boolean sideways = false;
    public static boolean is960 = false;
    public static boolean isCheckers = false;
    public static boolean isPre = false;
    public static boolean randomEvents = false;
    public static boolean blindfold = false;
    public static boolean fogOfWar = false;
    public static boolean multiplayer = false;
    public static boolean isWhite = false;
    public static boolean isHost = false;
    public static boolean isBad = false;
    public static boolean isDerby = false;
    public static boolean sendBoard = false;
    public static boolean isTimed = false;
    public static boolean isMinimax = false;
    public static Host host = new Host();
    public static Client client = new Client();
    public static Component joinB;
    public static Component hostB;
    public static Component disconnectB;
    public static Component resetB;
    public static Component reloadB;
    public static Component pastMoves;
    public static String piece;
    public static long endTime;
    public static int seconds;
    public static int x = 0;
    public static int y = 0;
    public static int placeInt = 0;
    public static int theme = 0;
    public static int timerLength = 600000;
    public static Color[] background = {Color.WHITE, Color.DARK_GRAY};
    public static Color[] tileA = {Color.WHITE, Color.LIGHT_GRAY};
    public static Color[] tileB = {Color.LIGHT_GRAY, Color.DARK_GRAY};
    public static Color[] movesColors = {Color.YELLOW, Color.YELLOW};
    public static Color[] selectColors = {Color.RED, Color.RED};
    public static Color[] textColors = {Color.DARK_GRAY, Color.LIGHT_GRAY};
    public static Color[] emptyColors = {Color.BLACK, Color.BLACK};
    public static Color titleColor = Color.BLACK;
    static final AudioFilePlayer player = new AudioFilePlayer();
    public static final ActionListener timeout = (ActionEvent e) -> {
        System.out.println("Over on time");
        JOptionPane.showMessageDialog(null, "Times out. Game Over");
        stopAllTimers();
    };
    public static ChessTimer whiteTimer = new ChessTimer(600000, timeout);
    public static ChessTimer blackTimer = new ChessTimer(600000, timeout);

    public static void main(String[] args) throws IOException 
    {
        chess = new Board();
        if (!SKIP_TITLE)
            showTitleScreen();
        else
            createAndShowGUI();
        System.out.println(chess);
    }

    private static void showTitleScreen() throws IOException  // TODO: make title screen beautiful
    {        
        startFrame = new JFrame("Chess");
        startFrame.setLayout(new GridBagLayout());
        startFrame.setSize(700, 840);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.getContentPane().setBackground(background[theme]);

        JLabel title = new JLabel("Chess");
        title.setFont(new Font("Arial", Font.BOLD, 82));
        title.setForeground(titleColor);
        
        JButton start = new JButton("Play");
        start.setSize(110, 70);
        start.setFont(new Font("Arial", Font.BOLD, 60));
        start.setForeground(Color.WHITE);
        start.setBackground(Color.RED);
        start.setFocusable(false);

        start.addActionListener(e -> {
            try {
                player.requestFadeOut();
                createAndShowGUI();
            } catch (IOException ex) {
                System.out.println("Failed to display GUI");
            }
        });

        player.play("sounds\\title.wav");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.insets = new Insets(0, 0, 60, 0);
        gbc.gridy = 0;
        startFrame.add(title, gbc); 
        gbc.gridy = 1; 
        gbc.insets = new Insets(0, 0, 90, 0);
        startFrame.add(start, gbc);
        startFrame.setVisible(true);

        Thread rainbowButton = new Thread(() -> {
            int i = 0;
            int r = 255;
            int g = 0;
            int b = 0;

            while(atTitleScreen)
            {
                start.setBackground(new Color(r,g,b));
                
                if (i < 51)
                    g += 5;
                else if (i < 102)
                    r -= 5;
                else if (i < 153)
                    b += 5;
                else if (i < 204)
                    g -= 5;
                else if (i < 255)
                    r += 5;
                else
                    b -= 5;

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e1) {
                    System.out.println("Failed to sleep");
                }

                i++;
                if (i == 255)
                {
                    i = 0;
                    r = 255;
                    g = 0;
                    b = 0;
                }

            }
        });
        rainbowButton.start();
    }



    private static void createAndShowGUI() throws IOException 
    {
        atTitleScreen = false;

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

        JMenuItem muteAudio = new JMenuItem("Mute/Unmute Audio");
        muteAudio.addActionListener((ActionEvent e) -> {
            muted = !muted;
        });

        JMenuItem changeTime = new JMenuItem("Change Timer Length");
        changeTime.addActionListener((ActionEvent e) -> {
            timerLength = Integer.parseInt(JOptionPane.showInputDialog("How long in seconds? "));
            whiteTimer.setDelay(timerLength);
            blackTimer.setDelay(timerLength);
        });
        
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
            JOptionPane.showMessageDialog(jFrame, "Created by Matt Johnson\nMusic by Matt Johnson, Milt Jackson, Stephen Melillo, Bryant Oden");
        });

        file.add(save);
        file.add(load);
        edit.add(colorPicker);
        edit.add(muteAudio);
        edit.add(changeTime);
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

        JCheckBox randomEventsBox = new JCheckBox("Random Events");
        randomEventsBox.setSelected(randomEvents);
        randomEventsBox.addItemListener((ItemEvent e) -> {
            randomEvents = !randomEvents;
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
            if (blindfold)
                player.play("sounds\\disappear.wav");
            else
                player.play("sounds\\appear.wav");
            reloadBoard(buttons,chess);
        });

        JCheckBox fogOfWarBox = new JCheckBox("Fog of War");
        fogOfWarBox.setSelected(fogOfWar);
        fogOfWarBox.addItemListener((ItemEvent e) -> {
            if (multiplayer)
            {
                fogOfWar = !fogOfWar;
                if (fogOfWar)
                    player.play("sounds\\disappear.wav");
                else
                    player.play("sounds\\appear.wav");
                reloadBoard(buttons,chess);
            }
        });
        
        JCheckBox isMinimaxBox = new JCheckBox("Minimax Bot");
        isMinimaxBox.setSelected(isMinimax);
        isMinimaxBox.addItemListener((ItemEvent e) -> {
        	isMinimax = !isMinimax;
        });

        JCheckBox isTimedBox = new JCheckBox("Timer");
        isTimedBox.setSelected(isTimed);
        isTimedBox.addItemListener((ItemEvent e) -> {
            isTimed = !isTimed;
            if (isTimed)
            {
                player.play("sounds\\timerTick.wav");
                if (whiteTurn)
                    whiteTimer.start();
                else
                    blackTimer.start();
                countdown.start();
            }
            else
            {
                whiteTimer.stop();
                blackTimer.stop();
            }
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

                button.setFocusable(false);
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

        blackTime = new JLabel("10:00");
        whiteTime = new JLabel("10:00");
    
        jFrame.setJMenuBar(menuBar);
        jFrame.add(title);
        jFrame.add(duckBox);
        jFrame.add(atomicBox);
        jFrame.add(torpedoBox);
        jFrame.add(sidewaysBox);
        jFrame.add(is960Box);
        jFrame.add(blindfoldBox);
        jFrame.add(fogOfWarBox);
        jFrame.add(isCheckersBox);
        jFrame.add(isPreBox);
        jFrame.add(isBadBox);
        jFrame.add(randomEventsBox);
        jFrame.add(isDerbyBox);
        jFrame.add(isTimedBox);
        jFrame.add(isMinimaxBox);
        jFrame.add(panel);
        jFrame.add(resetButton);
        jFrame.add(reloadButton);
        jFrame.add(hostButton);
        jFrame.add(joinButton);
        jFrame.add(blackTime);
        jFrame.add(whiteTime);
        //jFrame.add(moves);    // ugly

        if (!SKIP_TITLE)
            startFrame.setVisible(false);
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
            {
                chess = new Board("Checkers");
                player.play("sounds\\checkers.wav");
                System.out.println(chess);
            }
            else if (isPre)
                chess = new Board("preChess");
            else if (isBad)
                chess = new Board("bad");
            else if (isDerby)
            {
                chess = new Board("crossDerby");
                player.play("sounds\\crossDerby.wav");
            }
            else
            {
                chess = new Board();
                if (atomic)
                    player.play("sounds\\atomic.wav");
                if (duck)
                    player.play("sounds\\duck.wav");
            }

            selected = false;
            whiteTurn = true;
            gameOver = false;
            placeInt = 0;

            try {
                createAndShowGUI();
            } catch (IOException e1) {
                System.out.println("Failed to display GUI");
            }
            f.dispose();

            if (isPre)
                JOptionPane.showMessageDialog(jFrame, "Placing Rooks");

            whiteTimer.reset();
            blackTimer.reset();
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
            if (!gameOver)
            {
                System.out.println("Button clicked at row " + row + ", column " + col);
                if (!selected)
                    player.play("sounds\\click.wav");


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

                        player.play("sounds\\quack.wav");
                    }
                    else
                        System.out.println("Cannot move");
                }
                else if (selected)
                {
                    if (chess.getPiece(x,y).move(row,col,chess, false) && (!multiplayer || whiteTurn == isWhite))
                    {
                        player.play("sounds\\move.wav");

                        // System.out.println(chess.getPieceAbbr(row, col) + ((char) (row + 97)) + col);
                        // ((JTextArea) pastMoves).setText(((JTextArea) pastMoves).getText() + "\n" + chess.getPiece(row,col).getAbbr() + ((char) (row + 97)) + col);    // super ugly currently
                        
                        buttons[x][y].setIcon(null);

                        if (chess.getPiece(row,col) != null && !blindfold)
                            buttons[row][col].setIcon(chess.getPiece(row,col).getIcon());

                        if (!isMinimax)
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
                        {
                            if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1))
                                buttons[i][j].setBackground(tileA[theme]);
                            else
                                buttons[i][j].setBackground(tileB[theme]);
                            if (chess.occupation(i,j) == Board.Occupation.EMPTY)
                                buttons[i][j].setBackground(emptyColors[theme]);
                        }

                    selected = false;

                    if (chess.inCheckMate(whiteTurn) || !chess.bothKingsInPlay())
                        checkmateDialog(whiteTurn);
                }
                else if (chess.isOccupied(row, col) && chess.getPiece(row,col).isWhite() == whiteTurn && !(chess.getPiece(row, col) instanceof Duck))
                {
                    JButton clickedButton = buttons[row][col];
                    
                    for (int i = 0; i < 8; i++) 
                        for (int j = 0; j < 8; j++)
                        {
                            if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1))
                                buttons[i][j].setBackground(tileA[theme]);
                            else
                                buttons[i][j].setBackground(tileB[theme]);
                            if (chess.occupation(i,j) == Board.Occupation.EMPTY)
                                buttons[i][j].setBackground(emptyColors[theme]);
                        }

                    ArrayList<ArrayList<Integer>> possibleMoves = chess.getPiece(row,col).getPossibleMoves(chess);
                    
                    if (!multiplayer || (isWhite == whiteTurn))
                    {
                        for (int i = 0; i < possibleMoves.get(0).size(); i++)
                        {
                            Board temp = chess.copy();
                            if (temp.getPiece(row, col).move( possibleMoves.get(0).get(i), possibleMoves.get(1).get(i), temp, true))
                                buttons[possibleMoves.get(0).get(i)][possibleMoves.get(1).get(i)].setBackground(movesColors[theme]);
                        }
                    }

                    if (!multiplayer || (isWhite == whiteTurn))
                        clickedButton.setBackground(selectColors[theme]);

                    selected = true;
                    x = row;
                    y = col;
                }


                if (sendBoard)
                {
                    if (emptyCoords[0] != -1)
                    {
                        System.out.println("Empty/Trap removed!");
                        JOptionPane.showMessageDialog(null, "Blocked/Trapped tile removed!");
                        chess.setPiece(emptyCoords[0], emptyCoords[1], null);
                        emptyCoords[0] = -1;
                    }

                    if (randomEvents && Math.random() < EVENT_PROBABILITY) {
                        // ideas: hidden trap, freeze piece, piece changes types, swap location of all non-pawns, knight added to middle of board that moves randomly every turn
                        enum Event {
                            PAWN,
                            ALLEGIANCE,
                            EMPTY,
                            MISSILE,
                            PROMOTION,
                            REMOVE,
                            ADD,
                            TORPEDO,
                            ATOMIC,
                            TRAP
                        }

                        Event[] events = Event.values();
                        switch (events[((int) (Math.random() * events.length))])
                        {
                            case PAWN -> {
                                // todo: possible moves not updated
                                System.out.println("Pawn Swap!");
                                JOptionPane.showMessageDialog(null, "Pawn Swap!");
                                ArrayList<ArrayList<Integer>> whitePawns = chess.getPawns(Board.Occupation.WHITE);
                                ArrayList<ArrayList<Integer>> blackPawns = chess.getPawns(Board.Occupation.BLACK);

                                int x = (int) (Math.random() * whitePawns.size());
                                Piece temp = chess.getPiece( whitePawns.get(0).get(x), whitePawns.get(1).get(x) );
                                int y = (int) (Math.random() * blackPawns.size());
                                chess.setPiece( whitePawns.get(0).get(x), whitePawns.get(1).get(x),  chess.getPiece( blackPawns.get(0).get(y), blackPawns.get(1).get(y) ));
                                chess.setPiece( blackPawns.get(0).get(y), blackPawns.get(1).get(y), temp);
                                break;
                            }
                            case ALLEGIANCE -> {
                                // todo: color of pieces not updated
                                JOptionPane.showMessageDialog(null, "Allegiance Shift!");
                                System.out.println("Allegiance Shift!");
                                ArrayList<ArrayList<Integer>> whitePieces = chess.getPieces(Board.Occupation.WHITE);
                                ArrayList<ArrayList<Integer>> blackPieces = chess.getPieces(Board.Occupation.BLACK);
                                int r = (int) (Math.random() * whitePieces.size());
                                chess.setPiece( whitePieces.get(0).get(r), whitePieces.get(1).get(r), chess.getPiece(whitePieces.get(0).get(r), whitePieces.get(1).get(r)).switchColors() );
                                r = (int) (Math.random() * blackPieces.size());
                                chess.setPiece( blackPieces.get(0).get(r), blackPieces.get(1).get(r), chess.getPiece(blackPieces.get(0).get(r), blackPieces.get(1).get(r)).switchColors() );
                                break;
                            }
                            case EMPTY -> {
                                // todo: Empty added but screen not updated
                                JOptionPane.showMessageDialog(null, "Blocked tile added!");
                                ArrayList<ArrayList<Integer>> blankTiles = chess.getBlankTiles();
                                int r = (int) (Math.random() * blankTiles.size());
                                int x = blankTiles.get(0).get(r);
                                int y = blankTiles.get(1).get(r);
                                System.out.println("Blocked Tile added at " + x + ", " + y);
                                chess.setPiece(x, y, new Empty(x,y));
                                if (Math.random() < EMPTY_REMOVE_PROBABILITY)
                                {
                                    emptyCoords[0] = x;
                                    emptyCoords[1] = y;
                                }
                                break;
                            }
                            case MISSILE -> {
                                System.out.println("Missiles Launched!");
                                JOptionPane.showMessageDialog(null, "Missiles Launched!");
                                ArrayList<ArrayList<Integer>> whiteNonBlankTiles = chess.getPieces(Board.Occupation.WHITE);
                                ArrayList<ArrayList<Integer>> blackNonBlankTiles = chess.getPieces(Board.Occupation.BLACK);

                                int r = (int) (Math.random() * whiteNonBlankTiles.size());
                                int x = whiteNonBlankTiles.get(0).get(r);
                                int y = whiteNonBlankTiles.get(1).get(r);
                                chess.setPiece(x, y, new Empty(x,y));

                                r = (int) (Math.random() * blackNonBlankTiles.size());
                                x = blackNonBlankTiles.get(0).get(r);
                                y = blackNonBlankTiles.get(1).get(r);
                                chess.setPiece(x, y, new Empty(x,y));

                                player.play("sounds\\bomb.wav");
                                break;
                            }
                            case PROMOTION -> {
                                System.out.println("Promotion!");
                                JOptionPane.showMessageDialog(null, "Promotion!");
                                ArrayList<ArrayList<Integer>> whitePawns = chess.getPawns(Board.Occupation.WHITE);
                                ArrayList<ArrayList<Integer>> blackPawns = chess.getPawns(Board.Occupation.BLACK);

                                int r = (int) (Math.random() * whitePawns.size());
                                int[] whiteCoord = {whitePawns.get(0).get(r), whitePawns.get(1).get(r)};
                                r = (int) (Math.random() * blackPawns.size());
                                int[] blackCoord = {blackPawns.get(0).get(r), blackPawns.get(1).get(r)};

                                double prob = Math.random();
                                if (prob < .45)
                                {
                                    chess.setPiece(whiteCoord[0], whiteCoord[1], new Knight(whiteCoord[0], whiteCoord[1], true));
                                    chess.setPiece(blackCoord[0], blackCoord[1], new Knight(blackCoord[0], blackCoord[1], false));
                                }
                                else if (prob < .65)
                                {
                                    chess.setPiece(whiteCoord[0], whiteCoord[1], new Rook(whiteCoord[0], whiteCoord[1], true));
                                    chess.setPiece(blackCoord[0], blackCoord[1], new Rook(blackCoord[0], blackCoord[1], false));
                                }
                                else if (prob < .90)
                                {
                                    chess.setPiece(whiteCoord[0], whiteCoord[1], new Bishop(whiteCoord[0], whiteCoord[1], true));
                                    chess.setPiece(blackCoord[0], blackCoord[1], new Bishop(blackCoord[0], blackCoord[1], false));
                                }
                                else
                                {
                                    chess.setPiece(whiteCoord[0], whiteCoord[1], new Queen(whiteCoord[0], whiteCoord[1], true));
                                    chess.setPiece(blackCoord[0], blackCoord[1], new Queen(blackCoord[0], blackCoord[1], false));
                                }
                                break;
                            }
                            case REMOVE -> {
                                System.out.println("Removed a Piece!");
                                JOptionPane.showMessageDialog(null, "Removed a Piece!");

                                ArrayList<ArrayList<Integer>> whitePieces = chess.getChessmen(Board.Occupation.WHITE);
                                ArrayList<ArrayList<Integer>> blackPieces = chess.getChessmen(Board.Occupation.BLACK);

                                int r = (int) (Math.random() * whitePieces.size());
                                chess.setPiece(whitePieces.get(0).get(r), whitePieces.get(1).get(r), null);
                                r = (int) (Math.random() * blackPieces.size());
                                chess.setPiece(blackPieces.get(0).get(r), blackPieces.get(1).get(r), null);
                                break;
                            }
                            case ADD -> {
                                System.out.println("Piece added!");
                                JOptionPane.showMessageDialog(null, "Piece added!");

                                ArrayList<ArrayList<Integer>> blankTiles = chess.getBlankTiles();
                                ArrayList<Integer> whiteTileIndexes = new ArrayList<>();
                                ArrayList<Integer> blackTileIndexes = new ArrayList<>();

                                for (int i = 0; i < blankTiles.get(0).size(); i++)
                                {
                                    if (blankTiles.get(1).get(i) > 4)
                                        whiteTileIndexes.add(i);
                                    else
                                        blackTileIndexes.add(i);
                                }

                                int r = (int) (Math.random() * whiteTileIndexes.size());
                                int[] whiteCoord = {blankTiles.get(0).get(r), blankTiles.get(1).get(r)};
                                r = (int) (Math.random() * blackTileIndexes.size());
                                int[] blackCoord = {blankTiles.get(0).get(r), blankTiles.get(1).get(r)};

                                double prob = Math.random();
                                if (prob < .45)
                                {
                                    chess.setPiece(whiteCoord[0], whiteCoord[1], new Knight(whiteCoord[0], whiteCoord[1], true));
                                    chess.setPiece(blackCoord[0], blackCoord[1], new Knight(blackCoord[0], blackCoord[1], false));
                                }
                                else if (prob < .65)
                                {
                                    chess.setPiece(whiteCoord[0], whiteCoord[1], new Rook(whiteCoord[0], whiteCoord[1], true));
                                    chess.setPiece(blackCoord[0], blackCoord[1], new Rook(blackCoord[0], blackCoord[1], false));
                                }
                                else if (prob < .90)
                                {
                                    chess.setPiece(whiteCoord[0], whiteCoord[1], new Bishop(whiteCoord[0], whiteCoord[1], true));
                                    chess.setPiece(blackCoord[0], blackCoord[1], new Bishop(blackCoord[0], blackCoord[1], false));
                                }
                                else
                                {
                                    chess.setPiece(whiteCoord[0], whiteCoord[1], new Queen(whiteCoord[0], whiteCoord[1], true));
                                    chess.setPiece(blackCoord[0], blackCoord[1], new Queen(blackCoord[0], blackCoord[1], false));
                                }
                                break;
                            }
                            case TORPEDO -> {
                                System.out.println("All pawns have torpedo mutation!");
                                JOptionPane.showMessageDialog(null, "All pawns have torpedo mutation!");
                                torpedo = true;
                                break;
                            }
                            case ATOMIC -> {
                                System.out.println("All pieces have bombs!");
                                JOptionPane.showMessageDialog(null, "All pieces have bombs!");
                                atomic = true;
                                break;
                            }
                            case TRAP -> {
                                JOptionPane.showMessageDialog(null, "Trapped tile added!");
                                ArrayList<ArrayList<Integer>> blankTiles = chess.getBlankTiles();
                                int r = (int) (Math.random() * blankTiles.size());
                                int x = blankTiles.get(0).get(r);
                                int y = blankTiles.get(1).get(r);
                                System.out.println("Trapped Tile added at " + x + ", " + y);
                                chess.setPiece(x, y, new Trap(x,y));
                                if (Math.random() < EMPTY_REMOVE_PROBABILITY)
                                {
                                    emptyCoords[0] = x;
                                    emptyCoords[1] = y;
                                }
                                break;
                            }
                        }
                        reloadBoard(buttons, chess);
                    }

                    if (isTimed)
                        swapTimers();
                    if (multiplayer) {
                        try
                        {
                            if (isHost)
                                host.sendBoard(chess);
                            else
                                client.sendBoard(chess);
                        } catch (IOException e1) {
                            System.out.println("Failed to send board");
                        }
                    }
                    if (isMinimax)
                    {
                    	int[] m = Computer.getMinimaxMove(chess, Board.boolToOccupation(isWhite), 3);
                    	chess.movePiece(m[0], m[1], m[2], m[3], false);
                    	reloadBoard(buttons, chess);
                    }
                    sendBoard = false;
                }
            }
        }

        private void checkmateDialog(boolean whiteTurn) {
            System.out.println("Game Over");
            String winner = "White";
            if (whiteTurn)
                winner = "Black";
            JOptionPane.showMessageDialog(null, "Checkmate! " + winner + " won!");
            isMinimax = false;
            chess = new Board();
            selected = false;
            whiteTurn = true;
            try {
                createAndShowGUI();
            } catch (IOException e1) {
                System.out.println("Failed to Display GUI");
            }

            sendBoard = true;

            gameOver = true;
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
        b = chess;

        jFrame.setBackground(background[theme]);
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
            {
                if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1))
                    buttons[i][j].setBackground(tileA[theme]);
                else
                    buttons[i][j].setBackground(tileB[theme]);

                if (b.occupation(i,j) == Board.Occupation.EMPTY)
                    buttons[i][j].setBackground(emptyColors[theme]);

                buttons[i][j].removeActionListener(buttons[i][j].getActionListeners()[0]);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j, b));
            }
        }

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (b.getPiece(i,j) != null && !blindfold && !fogOfWar)
                    buttons[i][j].setIcon(b.getPiece(i,j).getIcon());
                else
                    buttons[i][j].setIcon(null);

        if (fogOfWar)
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (b.occupation(i, j) == Board.boolToOccupation(isWhite)) {
                        buttons[i][j].setIcon(b.getPiece(i,j).getIcon());
                        ArrayList<ArrayList<Integer>> l = b.getPiece(i,j).getPossibleMoves(chess);
                        for (int k = 0; k < l.get(0).size(); k++)
                            if (b.occupation(l.get(0).get(k), l.get(1).get(k)) != Board.Occupation.NULL)
                                buttons[ l.get(0).get(k) ][ l.get(1).get(k) ].setIcon( b.getPiece( l.get(0).get(k), l.get(1).get(k) ).getIcon() );
                    }
    }

    public static void swapTimers()
    {
        if (whiteTimer.isRunning())
        {
            whiteTimer.stop();
            blackTimer.start();
        }
        else
        {
            blackTimer.stop();
            whiteTimer.start();
        }
    }

    public static void stopAllTimers()
    {
        whiteTimer.stop();
        blackTimer.stop();
    }

    public static Thread countdown = new Thread(() -> {
        seconds = 0;
        boolean playingTimer = false;
        while(isTimed)
        {            
            if (whiteTurn)
            {
                endTime = whiteTimer.getEndTime();
                seconds = (int) ((endTime - new Date().getTime()) / 1000);
                if (Math.abs(seconds) < 3601)
                    whiteTime.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            }
            else
            {
                endTime = blackTimer.getEndTime();
                seconds = (int) ((endTime - new Date().getTime()) / 1000);
                if (Math.abs(seconds) < 3601)
                    blackTime.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            }

            if (seconds == 30 || seconds == 15)
            {
                player.play("sounds\\chirp.wav");
            }
            else if (seconds <= 6 && !playingTimer)
            {
                player.play("sounds\\timerBomb.wav");
                playingTimer = true;
            }
            else if (seconds == 0)
                playingTimer = false;

            if (playingTimer && seconds > 6)
            {
                player.cancel();
                playingTimer = false;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                System.out.println("Failed to sleep");
            }
        }
    });
}