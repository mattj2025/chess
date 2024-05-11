import java.net.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JButton;

public class Host implements ActionListener 
{
    private ServerSocket serverSocket;
    private Socket clientSocket;
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
            Main.isHost = true;
            Main.isWhite = true;
            serverSocket = new ServerSocket(6666);
            clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            String greeting = in.readLine();
            if ("Connect".equals(greeting)) {
                out.println("Connected");
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
                            e2.printStackTrace();
                        }
                    }
                }
            });
            loopThread.start();

        } catch(IOException e1) {
            e1.printStackTrace();
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
        Main.reloadBoard(Main.buttons, (Board) objectInputStream.readObject());
        System.out.println("Board recieved from Client");
    }

    public void disconnect() throws IOException
    {
        serverSocket.close();
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