import java.net.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.*;

public class Client implements ActionListener 
{
    private Socket clientSocket;
    BufferedReader in;
    PrintWriter out;

    public void actionPerformed(ActionEvent e)
    {
        try 
        {
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
                while (true) {
                    try {
                        recieveBoard();
                    } catch (ClassNotFoundException | IOException e1) {
                        e1.printStackTrace();
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
        OutputStream outputStream = clientSocket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(b);
        System.out.println("Board sent from Client");
    }

    public void recieveBoard() throws IOException, ClassNotFoundException {
        InputStream inputStream = clientSocket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Main.whiteTurn = !Main.whiteTurn;
        Main.reloadBoard(Main.buttons, (Board) objectInputStream.readObject());
        System.out.println("Board recieved from Host");
    }
}