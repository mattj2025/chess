import java.io.*;
import java.net.*;

public class Peer {
    public static void main(String[] args) {
        // Define the port for communication
        int port = 12345;
        
        // Start two threads, one acting as a server and the other as a client
        Thread serverThread = new Thread(() -> startServer(port));
        serverThread.start();
        
        //Thread clientThread = new Thread(() -> startClient("localhost", port));
        //clientThread.start();
    }
    
    // Method to start the server
    public static void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running...");
            
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);
            
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
    
    // Method to start the client
    public static void startClient(String serverAddress, int port) {
        try {
            Socket socket = new Socket(serverAddress, port);
            System.out.println("Connected to server.");
            
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
