import java.net.*;
import javax.swing.JButton;
import java.awt.event.*;
import java.io.*;

public class Client implements ActionListener 
{
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private InputStream inputStream;
    private OutputStream outputStream;

    public void actionPerformed(ActionEvent e)
    {
        try 
        {
            Main.jFrame.remove(Main.joinB);
            Main.jFrame.remove(Main.hostB);
            Main.jFrame.add(Main.disconnectB);
            Main.multiplayer = true;
            Main.isHost = false;
            Main.isWhite = false;

            clientSocket = new Socket(InetAddress.getLocalHost(), 6666);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sendMessage("Connect");
            String response = receiveMessage();
            System.out.println("" + response);

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
                            e2.printStackTrace();
                        }
                    }
                }
            });
            loopThread.start();

        } catch (IOException e1) {
            e1.printStackTrace();
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
        Main.reloadBoard(Main.buttons, (Board) objectInputStream.readObject());
        System.out.println("Board recieved from Host");
    }

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