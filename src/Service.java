/*
 * This class will be the first class to be started in the network.
 * It will initialize the port 8767, and will wait for nodes to join,
 * once a node will join, it will send it their IP and ID for reference
 * and class will store their value in its local Data Structure or routing
 * table.
 */

/* 
 *   @author anands@tcd.ie
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Service {

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8767);
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("connected ");
                new NodeThread(clientSocket).start();

            }
        } catch (SocketException e) {
            System.err.println("Warn: Socket Connection Reset, some one left the chat network");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

// Class NodeThread
class NodeThread extends Thread {

    private Socket socket = null;
    private DataInputStream is = null;
    private PrintStream os = null;
    static Hashtable<Integer, String> routingTable = new Hashtable<Integer, String>();
    static Iterator<Integer> keySetIterator;
    static String IP = null, input_client;
    private static boolean closed = false;
    static int ID, size, portnumber = 8768;
    

    NodeThread(Socket socket) {
        this.socket = socket;
  }

    public void run() {
        try {
            /*
             * Create input and output streams for this client.
             */
            is = new DataInputStream(socket.getInputStream());
            os = new PrintStream(socket.getOutputStream());

            while (!closed) {

                input_client = is.readLine();

                if (input_client.startsWith("initiating")) {
                    IP = is.readLine().trim();
                    
                    ID = Integer.parseInt(is.readLine().trim());
                    

                    routingTable.put(ID, IP);
                    size = routingTable.size();
                    keySetIterator = routingTable.keySet().iterator();

                    os.println(size);

                    while (keySetIterator.hasNext()) {
                        Integer key = keySetIterator.next();

                        os.println(key);
                        os.println(routingTable.get(key));

                        UpdateRouting.addToLocalNode(key, routingTable.get(key));
                    }
                    System.out.println("Routing Table of current Node");
                    printTable();
                }
                if (input_client.startsWith("bye")) {
                    routingTable.remove(ID);
                    System.out.println("Updated Routing Table of current Node with Size : " + routingTable.size());
                    printTable();
                    this.is.close();
                    this.os.close();
                    this.socket.close();
                } else {
                    sleep(1000);
                }
                new ServiceRead(routingTable, input_client, ID).start();
            }
        } catch (SocketException e) {
            System.err.println("Warn: Socket Connection Reset, some one left the chat network");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static void printTable() {
        size = routingTable.size();
        keySetIterator = routingTable.keySet().iterator();
        while (keySetIterator.hasNext()) {
            Integer key = keySetIterator.next();
            System.out.println("Node ID: " + key + " IP: " + routingTable.get(key) + " Routing Table Size: " + routingTable.size());
        }

    }
}
