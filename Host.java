import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Host implements ActionListener 
{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static int port = 1024;

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try 
        {
            port++;
            JOptionPane.showMessageDialog(Main.jFrame, "Hosted on port: " + port);
            Main.multiplayer = true;
            Main.isHost = true;
            Main.isWhite = true;

            // TODO - allow host to cancel joining bc app currently freezes
            
            //Thread cancelThread = new Thread(() -> {
                //Timer cancel = new Timer(5000, new CancelListener());
                //cancel.start();
            //});
            //cancelThread.start();

            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            String greeting = in.readLine();
            if ("Connect".equals(greeting)) {
                out.println("Connected");
                JOptionPane.showMessageDialog(Main.jFrame, "Connected");
                Main.jFrame.remove(Main.joinB);
                Main.jFrame.remove(Main.hostB);
                Main.jFrame.add(Main.disconnectB);
                Main.jFrame.repaint();
            }


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

        } catch(IOException e1) {
            System.out.println("Could not find Client");
            JOptionPane.showMessageDialog(Main.jFrame, "Could not find client");
        }
    }

    public void sendBoard(Board b) throws IOException {
        outputStream = clientSocket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(b);
        System.out.println("Board sent from Host");
    }
 
    public void recieveBoard() throws IOException, ClassNotFoundException {
        inputStream = clientSocket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Main.whiteTurn = !Main.whiteTurn;
        Board b = (Board) objectInputStream.readObject();
        Main.chess = b;
        Main.reloadBoard(Main.buttons, b);
        System.out.println("Board recieved from Client");
    }

    /*

    See Client file for details

    public void sendBoard(Board b) throws IOException {
        outputStream = clientSocket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(new Game());
        System.out.println("Board sent from Host");
    }

    public void recieveBoard() throws IOException, ClassNotFoundException {
        inputStream = clientSocket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Main.whiteTurn = !Main.whiteTurn;
        Game g = (Game) objectInputStream.readObject();
        g.loadGame();
        Main.reloadBoard(Main.buttons, g.getBoard());
        System.out.println("Board recieved from Client");
    }
    */

    public void disconnect() throws IOException
    {
        serverSocket.close();
        clientSocket.close();
        outputStream.close();
        inputStream.close();
        JOptionPane.showMessageDialog(Main.jFrame, "Disconnected");
        Main.jFrame.add(Main.joinB);
        Main.jFrame.add(Main.hostB);
        Main.jFrame.remove(Main.disconnectB);
        Main.multiplayer = false;
        ((JButton) Main.resetB).doClick();
    }

    public class CancelListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Failed");
            try {
                disconnect();
            } catch (IOException e1) {
                System.out.println("Oopsies");
            }
        }
    }
}