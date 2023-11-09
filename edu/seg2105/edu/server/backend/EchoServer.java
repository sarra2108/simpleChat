package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;
import edu.seg2105.client.ui.ServerConsole;

import java.io.IOException;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 1234;
  String loginKey = "#login";
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  ChatIF serverConsole;
  public EchoServer(int port, ChatIF serverConsole) {
    super(port);
    this.serverConsole = serverConsole;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException {



// System.out.println("Message received: " + msg + " from " + client);
//  this.sendToAllClients(msg);
//
  String msgstr = (String) msg;
  boolean isloggedin=false;
    if (msgstr.startsWith("#login")||msgstr=="#login") {
      String loginId = msgstr.substring("#login".length()).trim();
      if ( client.getInfo(loginKey) == null&&!isloggedin) {
        isloggedin = true;
        client.setInfo(loginKey, loginId);
        serverConsole.display("Message received from #login " + loginId + " from null");
        serverConsole.display(loginId + " has logged on.");
        client.sendToClient(loginId + " has logged on.");
      }else  {
          serverConsole.display("Error: #login command can only be used as the first command after connecting.");
          client.sendToClient("Error: #login command can only be used as the first command after connecting.");
          client.close();
        }


      }



     else {

      String loginId = (String) client.getInfo(loginKey);


        serverConsole.display("Message received  "+msgstr + " from " + loginId );
        sendToAllClients(loginId + ": " + msgstr);
      }


   }





  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  @Override
  protected void clientConnected(ConnectionToClient client) {

    //System.out.println("client connected " +client);
    System.out.println("A new client has connected to the server.");


  }

  @Override
  synchronized protected void clientDisconnected(
          ConnectionToClient client) {
    String loginId = (String) client.getInfo(loginKey);
    if (loginId != null) {
      System.out.println(loginId + " disconnected");
    } else {
    System.out.println("client disconnected "+ client);
  }}
  public void handleMessageFromServerConsole(String message) {
    if (message.startsWith("#")){
      handleCommande(message);
    }else {
      this.sendToAllClients("SERVER MSG> " + message);
      serverConsole.display(message);
    }
  }

  public void handleCommande(String message) {

      String[] parameters = message.split(" ");
      String command = parameters[0];
      switch (command) {
        case "#quit":

          try {
            this.close();
          } catch (IOException e) {
            System.exit(1);
          }
          System.exit(0);
          break;
        case "#stop":
          this.stopListening();
          break;
        case "#close":
          try {
            this.stopListening(); // Stop listening for new clients
            this.close();
          } catch (IOException e) {
          }
          break;
        case "#setport":
          if (!isListening() && getNumberOfClients() == 0) {
            try {
              int newPort = Integer.parseInt(parameters[1]);
              setPort(newPort);
              serverConsole.display("Server listening for connections on port " + newPort);
            } catch (NumberFormatException e) {
              serverConsole.display("Invalid port number.");
            }
          } else {
            serverConsole.display("Can't set port while the server is listening or clients are connected.");
          }
          break;
        case "#start":
          if (!this.isListening()) {
            try {
              this.listen();
            } catch (IOException e) {

            }
          } else {
            System.out.println("We are already started and listening for clients!.");
          }
          break;
        case "#getport":
          System.out.println("Current port is " + this.getPort());
          break;
        default:
          System.out.println("Invalid command: '" + command+ "'");
          break;
      }
    }


  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   */
  public static void main(String[] args) throws IOException {
    int port = 0; //Port to listen on

    try {
      port = Integer.parseInt(args[0]); //Get port from command line
    } catch (Throwable t) {
      port = DEFAULT_PORT; //Set port to 5555
    }

    ServerConsole chat = new ServerConsole(port);
    try {
      chat.accept();  // Start accepting console input
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  }

//End of EchoServer class
