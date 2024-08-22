import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client implements ActionListener 
{
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int port;

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try 
        {
            port = Integer.parseInt(JOptionPane.showInputDialog("Enter the Port Number"));
            Main.multiplayer = true;
            Main.isHost = false;
            Main.isWhite = false;

            clientSocket = new Socket(InetAddress.getLocalHost(), port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sendMessage("Connect");
            String response = receiveMessage();
            System.out.println("" + response);
            JOptionPane.showMessageDialog(Main.jFrame, "Connected");

            Main.jFrame.remove(Main.joinB);
            Main.jFrame.remove(Main.hostB);
            Main.jFrame.add(Main.disconnectB);
            Main.jFrame.repaint();
            

            Thread loopThread = new Thread(() -> {
                boolean cont = true;
                while (cont) {
                    try {
                        recieveBoard();
                    } catch (ClassNotFoundException | IOException e1) {
                        System.out.println("Connection ended");
                        cont = false;
                        try {
                            disconnect();
                        } catch (IOException e2) {
                            System.out.println("Oopsies");
                        }
                    }
                }
            });
            loopThread.start();

        } catch (IOException e1) {
            System.out.println("Could not find Host");
            JOptionPane.showMessageDialog(Main.jFrame, "Could not find Host");
        }
    }

    private void sendMessage(String msg) throws IOException {
        out.println(msg);
    }

    private String receiveMessage() throws IOException {
        return in.readLine();
    }

    public void sendBoard(Board b) throws IOException {
        outputStream = clientSocket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(b);
        System.out.println("Board sent from Client");
    }

    public void recieveBoard() throws IOException, ClassNotFoundException {
        inputStream = clientSocket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Main.whiteTurn = !Main.whiteTurn;
        Board b = (Board) objectInputStream.readObject();
        Main.chess = b;
        Main.reloadBoard(Main.buttons, b);
        System.out.println("Board recieved from Host");
    }

    /* 

    Version using game instead of only the board
    Will keep variants consistent, however it currently does not work

    public void sendBoard(Board b) throws IOException {
        outputStream = clientSocket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(new Game());
        System.out.println("Board sent from Client");
    }

    public void recieveBoard() throws IOException, ClassNotFoundException {
        inputStream = clientSocket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Main.whiteTurn = !Main.whiteTurn;
        Game g = (Game) objectInputStream.readObject();
        g.loadGame();
        Main.reloadBoard(Main.buttons, g.getBoard());
        System.out.println("Board recieved from Host");
    }
    */

    public void disconnect() throws IOException
    {
        clientSocket.close();
        outputStream.close();
        inputStream.close();
        Main.jFrame.add(Main.joinB);
        Main.jFrame.add(Main.hostB);
        Main.jFrame.remove(Main.disconnectB);
        Main.multiplayer = false;
        ((JButton) Main.resetB).doClick();
    }
}