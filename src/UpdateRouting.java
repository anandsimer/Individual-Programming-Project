import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class is generalised class and use to update the
 * Routing Table like, adding new node and removing new node.
 */

/*
 * @author anands@tcd.ie
 */
public class UpdateRouting {

    static Hashtable<Integer, String> routingTable = new Hashtable<Integer, String>();
    static Iterator<Integer> keySetIterator = null;
    static Socket socket = null;
    // Output Stream
    private static PrintStream os = null;
    // The input stream
    private static DataInputStream is = null;
    private static int ID;
    private static String name = null;

    public static void add(Socket socket, PrintStream os, DataInputStream is) throws IOException {
        socket = socket;
        os = os;
        is = is;
        int size = Integer.parseInt(is.readLine());
        while (size > 0) {
            ID = Integer.parseInt(is.readLine().trim());
            name = is.readLine().trim();
            routingTable.put(ID, name);
            size--;
        }
        printTable();
    }

    static void remove() throws Exception {
        routingTable.remove(ID);

        printTable();
        sleep(2000);
        System.err.println("You opted to leave the Chat Network");
        System.exit(1);
    }

    static void printTable() throws IOException {
        
        keySetIterator = routingTable.keySet().iterator();

        while (keySetIterator.hasNext()) {
            Integer key = keySetIterator.next();
            System.out.println("key: " + key + " value: " + routingTable.get(key));
        }

    }

    static Hashtable<Integer, String> getTabled() {
        return routingTable;
    }

    static void addToLocalNode(Integer key, String name) {
        routingTable.put(key, name);
        System.out.println("Update addtolocal Routing Class :" + routingTable);
    }

    static void sendAll() {
        System.out.println("Update sendAll Routing Class :" + routingTable);
    }
}
