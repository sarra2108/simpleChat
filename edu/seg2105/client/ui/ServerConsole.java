package edu.seg2105.client.ui;

import java.io.*;
import java.util.Scanner;
import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.common.ChatIF;
import edu.seg2105.client.backend.ChatClient;

/**
 * This class constructs the UI for the server console. It implements the
 * ChatIF interface to handle messages sent from the server.
 *
 * @author [Your Name]
 */
public class ServerConsole implements ChatIF {
    // Class variables
    final public static int DEFAULT_PORT = 5555;


    // Instance variables
     EchoServer server;
    BufferedReader fromConsole;
    // Constructors
    public ServerConsole( int port) throws IOException {

        try {
            server = new EchoServer( port,this);
            server.listen();
        } catch (Exception ex) {
            System.out.println("Unexpected error while creating the server!");
            System.exit(1);
        }
        // Create reader for console input
        fromConsole = new BufferedReader(new InputStreamReader(System.in));
    }


    // Instance methods

    /**
     * This method waits for input from the console. Once it is received, it sends it
     * to the server.
     */
    public void accept() {
        try {
            String message;

            while (true) {
                message = fromConsole.readLine();
                server.handleMessageFromServerConsole(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    /**
     * This method overrides the method in the ChatIF interface. It displays a
     * message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message) {

            System.out.println("> " + message);
        }

    // Class methods


        public static void main (String[]args) throws IOException {
            int port = 0; // Port to listen on

            try {
                port = Integer.parseInt(args[0]);
            } catch (Throwable t) {
                port = DEFAULT_PORT; // Set port to 5555
            }

            ServerConsole serv = new ServerConsole(port);

            EchoServer server = new EchoServer(port, serv);
            try {
                server.listen(); // Start listening for connections
            } catch (IOException e) {
                System.out.println("ERROR - Could not listen for clients!");
                System.exit(1);
            }
            serv.accept(); // Start accepting console input
        }
    }

