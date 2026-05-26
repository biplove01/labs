import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            // Connect to server on localhost port 12345
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Connected to server!");

            // Create input and output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            String message;

            // Chat loop
            while (true) {
                // Send message to server
                System.out.print("Client: ");
                message = keyboard.readLine();
                output.println(message);

                if (message.equalsIgnoreCase("quit")) {
                    break;
                }

                // Receive message from server
                message = input.readLine();
                if (message == null || message.equalsIgnoreCase("quit")) {
                    System.out.println("Server disconnected");
                    break;
                }
                System.out.println("Server: " + message);
            }

            // Close connection
            socket.close();

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
