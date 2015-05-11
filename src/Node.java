

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anands@tcd.ie
 */
public class Node {

    // Boot node portnumber
    static int portnumber = 8767;
    // Output Stream
    private static PrintStream os = null;
    // The input stream
    private static DataInputStream is = null;
    //current node's ID
    static int ID;
    // Boot node IP
    static String IP, initial_message, clientIP, interest;
    // Socket connection to boot node
    static Socket socket, socket2;
    // input from commandLine
    private static BufferedReader inputLine = null;
    private static boolean closed = false;
    static  String nodeInput;

    public static void main(String[] args) {

        UpdateRouting ur = null;
        try {
            clientIP = InetAddress.getLocalHost().getHostAddress();

            System.out.println("Your Machine IP: " + clientIP +"\n");

            // Initialize stream to read from command Line
            inputLine = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Please enter the IP of the known Node that is connected  \n"
                    + "to the Network, please type 'bye' without single qoutes \n"
                    + "to leave the network of chat, you will have to rerun the \n"
                    + "Node application to join again, once you leave.\n"
                    + "\nIP : ");

            IP = inputLine.readLine().trim();

            System.out.println("Give some ID (numerical) to your node for example '232' ");

            ID = Integer.parseInt(inputLine.readLine().trim());

            System.out.println("Give me some String to identify your interest in chat messages");

            interest = inputLine.readLine().trim();

            System.out.println("Please wait while we are connecting you to Server ");
            sleep(1000);

            // create connection to boot node.
            socket = new Socket(IP, portnumber);
            System.out.println("Connected to P2P Chat service, lets get to connected to peers ");
            // initialize the input and output streams.

            os = new PrintStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());

            //Now we need to send the information the Boot Node,
            //i.e my IP and my ID.
            {
                os.println("initiating");
                os.println(clientIP);
                os.println(ID);
            }

            UpdateRouting.add(socket, os, is);

            if (socket != null && os != null && is != null) {

                /* Create a thread to read from the server. */
                new ListenNode(interest).start();

                while (!closed) {
                    int hop_count=1;
                    nodeInput=inputLine.readLine().trim();
                    
                    nodeInput=nodeInput+"^"+hop_count+"^"+ID;
                    System.err.println(nodeInput);
                    os.println(nodeInput);
                    ListenNode.sendToNodeService(nodeInput);
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}

/*
 *  Listen Node class is going to Listen in continouos loop,  it will keep track of the messages coming to this node and
 *   place check if message belongs to current node, if not forward  the message to other node.
 */
class ListenNode extends Thread {

    // Output Stream
    private static PrintStream os = null;
    // The input stream
    private static DataInputStream is = null;
    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket, socket2 = null;
    private static boolean closed = true;
    private static String interest;

    ListenNode(String interest) {
        this.interest = interest;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(8768);

            while (true) {
                String input;
                clientSocket = serverSocket.accept();

                is = new DataInputStream(clientSocket.getInputStream());
                os = new PrintStream(clientSocket.getOutputStream());

                input = is.readLine().trim();

                if (input.startsWith("initiating")) {
                } else if (input.startsWith(interest)) {
                    System.out.println("Happy to find my interest, I m consuming this message");
                } else {
                    sendToNodeService(input);
                    System.err.println("Message of not interest, forwarding to other nodes.");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    static void sendToNodeService(String input) {
        try {
            socket2 = new Socket(InetAddress.getLocalHost().getHostAddress(), 8767);
            os = new PrintStream(socket2.getOutputStream());
            is = new DataInputStream(socket2.getInputStream());
            os.println(input);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
