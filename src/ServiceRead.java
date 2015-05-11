import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anands@tcd.ie
 */
public class ServiceRead extends Thread {

    static Hashtable<Integer, String> routingTable = new Hashtable<Integer, String>();
    static Iterator<Integer> keySetIterator;
    static int ID, portnumber = 8768;
    static private Socket socket = null;
    static private DataInputStream is = null;
    static private PrintStream os = null;
    static String input_client, stgChr = "\\^";
    private static boolean closed = false;
    private String[] input;
    private int hop_count, incoming_ID, key;

    ServiceRead(Hashtable<Integer, String> routingTable, String input, int ID) {
        this.routingTable = routingTable;
        this.input_client = input;
        this.ID = ID;
    }

    public void run() throws ArrayIndexOutOfBoundsException {
        try {
            input = input_client.split(stgChr);

            hop_count = Integer.parseInt(input[1].trim());
            incoming_ID = Integer.parseInt(input[2].trim());
            if (hop_count > 4) {
                System.out.println("Message dies here, hop count value more than 4, Message:  " + input[0]);
            } else {
                hop_count++;

                input_client = input[0] + "^" + hop_count + "^" + incoming_ID;
                keySetIterator = routingTable.keySet().iterator();
                while (keySetIterator.hasNext()) {
                    key = (Integer) keySetIterator.next();
                    System.out.println("from sendRead : " + " Node ID: " + key + " IP: " + routingTable.get(key) + " Routing Table Size: " + routingTable.size());
                    {
                        if (key == incoming_ID) {
                            System.out.println("Message arrived from node ID: " + incoming_ID);
                        } else {
                            socket = new Socket(routingTable.get(key), portnumber);
                            System.err.println("connected to node" + "::" + "  hashkey  " + key + "  IDkey ::  " + incoming_ID);
                            os = new PrintStream(socket.getOutputStream());
                            os.println(input_client);
                        }
                    }
                }
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Warn: Array Out of Bound. No real issue occured, array has hit the last  index");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
