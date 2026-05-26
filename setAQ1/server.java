import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Create server socket on port 12345
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Waiting for client...");

            // Accept client connection
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            // Create input and output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            String message;

            // Chat loop
            while (true) {
                // Receive message from client
                message = input.readLine();
                if (message == null || message.equalsIgnoreCase("quit")) {
                    System.out.println("Client disconnected");
                    break;
                }
                System.out.println("Client: " + message);

                // Send message to client
                System.out.print("Server: ");
                message = keyboard.readLine();
                output.println(message);

                if (message.equalsIgnoreCase("quit")) {
                    break;
                }
            }

            // Close connections
            socket.close();
            serverSocket.close();

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
