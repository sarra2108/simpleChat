// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
Boolean isLoggedIn=false;
 String loginID;
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID,String host, int port, ChatIF clientUI)
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID=loginID;
    openConnection();

  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());


    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {

     if(message.startsWith("#")){
        handleCommande(message);
     }
      else{
      sendToServer(message);}}

    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  private void handleCommande(String command) throws IOException {
    String[] args = command.split(" ");
    if (command.equals("#quit")){quit();}
    else if (command.equals("#logoff")){

      if (isConnected()) {

        closeConnection();
      }

    }

    else if (command.equals("#login")){
      if (!isConnected() ) {
        openConnection();

      }
      else{

        clientUI.display("ERROR_Already connected to the server.");
      }
    }
    else if(command.equals("#sethost") ){
      if (!isConnected()) {
        if (args.length >= 2) {
          String newHost = args[1];
          setHost(newHost);
        } else {
          clientUI.display("Usage: #sethost <host>");
        }
      } else {
        clientUI.display("Cannot set host while connected to the server.");
      }}
       else if(command.equals("#setport") ){
        if (!isConnected()) {
          if (args.length >= 2) {
            try {
              int newPort = Integer.parseInt(args[1]);
              setPort(newPort);
            } catch (NumberFormatException e) {
              clientUI.display("Invalid port number.");
            }
          } else {
            clientUI.display("Usage: #setport <port>");
          }
        } else {
          clientUI.display("Cannot set port while connected to the server.");
        }}
       else if (command.equals("#gethost")){clientUI.display("Current host: " + getHost());}
    else if (command.equals("#getport")){clientUI.display("Current port: " + getPort());}
    }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  @Override
  protected void connectionException(Exception exception) {
    clientUI.display("The server is shut down");
    quit();

  }
    @Override
  protected void connectionEstablished()  {
        try {
            sendToServer("#login" + loginID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




  
  @Override
  protected void connectionClosed() {

      clientUI.display("Connection closed");



  }
}
//End of ChatClient class
