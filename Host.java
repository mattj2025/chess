import java.net.*;
import java.awt.event.*;
import java.io.*;

public class Host implements ActionListener 
{
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void actionPerformed(ActionEvent e)
    {
        try 
        {
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
                while (true) {
                    try {
                        recieveBoard();
                    } catch (ClassNotFoundException | IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            loopThread.start();

        } catch(IOException e1) {
            e1.printStackTrace();
        }
    }

    public void sendBoard(Board b) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(b);
        System.out.println("Board sent from Host");
    }
 
    public void recieveBoard() throws IOException, ClassNotFoundException {
        InputStream inputStream = clientSocket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Main.whiteTurn = !Main.whiteTurn;
        Main.reloadBoard(Main.buttons, (Board) objectInputStream.readObject());
        System.out.println("Board recieved from Client");
    }
}