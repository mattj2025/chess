import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    private static final int BUTTON_SIZE = 80;
    private static JButton[][] buttons;
    public static boolean selected = false;
    public static boolean moveDuck = false;
    public static int x = 0;
    public static int y = 0;
    public static boolean whiteTurn = true;
    private static JFrame jFrame;
    public static boolean duck = false;
    public static boolean atomic = false;
    public static boolean torpedo = false;
    public static boolean sideways = false;
    public static boolean is960 = false;
    public static boolean blindfold = false;
    public static boolean multiplayer = false;

    public static void main(String[] args) {
        Board chess = new Board();
        createAndShowGUI(chess);
        System.out.println(chess);
    }

    private static void createAndShowGUI(Board b) {
        jFrame = new JFrame("Chess");
        jFrame.setLayout(new FlowLayout());
        jFrame.setSize(1200, 800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JCheckBox duckBox = new JCheckBox("Duck");
        duckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    duck = true;
                else
                    duck = false;
            }
        });

        JCheckBox atomicBox = new JCheckBox("Atomic");
        atomicBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    atomic = true;
                else
                    atomic = false;
            }
        });

        JCheckBox torpedoBox = new JCheckBox("Torpedo");
        torpedoBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    torpedo = true;
                else
                    torpedo = false;
            }
        });

        JCheckBox sidewaysBox = new JCheckBox("Sideways");
        sidewaysBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    sideways = true;
                else
                    sideways = false;
            }
        });

        JCheckBox is960Box = new JCheckBox("960");
        is960Box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    is960 = true;
                else
                    is960 = false;
            }
        });

        JCheckBox blindfoldBox = new JCheckBox("Blindfold");
        blindfoldBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                
                    blindfold = true;
                else
                    blindfold = false;
                reloadBoard(buttons,b);
            }
        });

        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 10000, 99999, 1);
        JSpinner port = new JSpinner(spinnerModel);
        port.setPreferredSize(new Dimension(100, 30));

        JButton hostButton = new JButton("Host");
        hostButton.setPreferredSize(new Dimension(100,40));
        hostButton.setBackground(Color.white);
        hostButton.addActionListener(new Host((int) port.getValue()));

        JButton joinButton = new JButton("Join");
        joinButton.setPreferredSize(new Dimension(100,40));
        joinButton.setBackground(Color.white);
        joinButton.addActionListener(new Join((int) port.getValue()));

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100,40));
        resetButton.setBackground(Color.white);
        resetButton.addActionListener(new Reset(b));

        
        JPanel panel = new JPanel(new GridLayout(8, 8));
        buttons = new JButton[8][8];

        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++) 
            {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                if (b.getPiece(x,y) != null && !blindfold)
                    button.setIcon(b.getPiece(x,y).getIcon());
                button.addActionListener(new ButtonClickListener(x, y, b));
                if ((y % 2 == 0 && x % 2 == 0) || (x % 2 == 1 && y % 2 == 1))
                    button.setBackground(Color.white);
                else
                    button.setBackground(Color.LIGHT_GRAY);
                panel.add(button);
                buttons[x][y] = button;
            }
    
        jFrame.add(duckBox);
        jFrame.add(atomicBox);
        jFrame.add(torpedoBox);
        jFrame.add(sidewaysBox);
        jFrame.add(is960Box);
        jFrame.add(blindfoldBox);
        jFrame.add(panel);
        jFrame.add(resetButton);
        jFrame.add(port);
        jFrame.add(hostButton);
        jFrame.add(joinButton);
        jFrame.setVisible(true);
    }


    private static class Host implements ActionListener 
    {
        private int port;

        public Host(int port)
        {
            this.port = port;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        { 
            Thread serverThread = new Thread(() -> startServer(port));
            serverThread.start();
        } 

        public static void startServer(int port) 
        {
            try {
                multiplayer = true;
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server is running...");
                JOptionPane.showMessageDialog(null, "Hosting. Waiting for connection");

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);
                JOptionPane.showMessageDialog(null, "Connected");
                
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Client: " + message);
                    System.out.print("You: ");
                    String reply = reader.readLine();
                    out.println(reply);
                }
                
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Join implements ActionListener 
    {
        private int port;
        private static final String ip = "192.168.254.17";

        public Join(int port)
        {
            this.port = port;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        { 
            Thread clientThread = new Thread(() -> startClient(ip, port));
            clientThread.start();
        } 

        public static void startClient(String serverAddress, int port) 
        {
            try {
                multiplayer = true;
                Socket socket = new Socket(serverAddress, port);
                System.out.println("Connected to server.");
                JOptionPane.showMessageDialog(null, "Connected to host");
                
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while (true) {
                    System.out.print("You: ");
                    String input = reader.readLine();
                    out.println(input);
                    
                    message = in.readLine();
                    if (message != null) {
                        System.out.println("Server: " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Reset implements ActionListener 
    {
        private Board chess;

        public Reset(Board b)
        {
            chess = b;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        { 
            if (is960)
                chess = new Board("960");
            else
                chess = new Board();
            selected = false;
            whiteTurn = true;
            createAndShowGUI(chess);
        } 
    }

    private static class ButtonClickListener implements ActionListener 
    {
        private int row;
        private int col;
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
            if (moveDuck)
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
                }
                else
                    System.out.println("Cannot move");
            }
            else if (selected)
            {
                if(chess.getPiece(x,y).move(row,col,chess))
                {
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
                }
                else
                    System.out.println("Cannot Move");
                    
                for (int i = 0; i < 8; i++) 
                    for (int j = 0; j < 8; j++)
                        if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1))
                            buttons[i][j].setBackground(Color.white);
                        else
                            buttons[i][j].setBackground(Color.LIGHT_GRAY);

                selected = false;
            }
            else if (chess.isOccupied(row, col) && chess.getPiece(row,col).isWhite() == whiteTurn && (!chess.inCheck(whiteTurn) || chess.getPiece(row,col) instanceof King || !whiteTurn) && !(chess.getPiece(row, col) instanceof Duck))
            {
                JButton clickedButton = buttons[row][col];
                
                for (int i = 0; i < 8; i++) 
                    for (int j = 0; j < 8; j++)
                        if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1))
                            buttons[i][j].setBackground(Color.white);
                        else
                            buttons[i][j].setBackground(Color.LIGHT_GRAY);

                ArrayList<ArrayList<Integer>> possibleMoves = chess.getPiece(row,col).getPossibleMoves(chess);
                if (chess.getPiece(row,col) instanceof King && possibleMoves.get(0).size() == 0)
                {
                    System.out.println("Game Over");
                    String winner = "White";
                    if (whiteTurn)
                        winner = "Black";
                    JOptionPane.showMessageDialog(null, "Checkmate! " + winner + " won!");
                    chess = new Board();
                    selected = false;
                    whiteTurn = true;
                    createAndShowGUI(chess);
                }
                else
                    for (int i = 0; i < possibleMoves.get(0).size(); i++)
                        buttons[possibleMoves.get(0).get(i)][possibleMoves.get(1).get(i)].setBackground(Color.YELLOW);

                
                clickedButton.setBackground(Color.RED);

                selected = true;
                x = row;
                y = col;
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
        for (int i = 0; i < 8; i++) 
            for (int j = 0; j < 8; j++)
                if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1))
                    buttons[i][j].setBackground(Color.white);
                else
                    buttons[i][j].setBackground(Color.LIGHT_GRAY);

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
                    buttons[i][j].setIcon(null);
    }
}