import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * create BlockingQueue x2 - taskQueue and resultsQueue
 * create synchronized object Availability
 * create executor
 * execute 3 threads
 */
public class Main {

    static final int PORT_NUM = 4455;

    public static void main(String args[]) throws IOException {

        InetAddress address = InetAddress.getLocalHost();
        Socket s1 = null;
        BufferedReader is = null;

        UUID ID = null;

        try {
            s1 = new Socket(address, PORT_NUM);
            is = new BufferedReader(new InputStreamReader(s1.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Computing server address : " + address);

        try {
            String serverID = is.readLine();
            ID = UUID.fromString(serverID);
            System.out.println("Server registered, ID: " + ID);

            ConcurrentHashMap<UUID, ClientTask> clientTaskMap = new ConcurrentHashMap<>();
            BlockingQueue<UUID> taskQueue = new ArrayBlockingQueue<>(10);
            BlockingQueue<UUID> resultsQueue = new ArrayBlockingQueue<>(10);
            Availability availability = new Availability();

            ExecutorService executor = Executors.newFixedThreadPool(3);
            executor.execute(new SchedulerIn(s1, ID, taskQueue, availability,
                    clientTaskMap));
            executor.execute(new SchedulerOut(s1, availability, clientTaskMap,
                    resultsQueue));
            executor.execute(new Compute(taskQueue, availability, clientTaskMap,
                    resultsQueue));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
        }

        // TODO when to close connection? cannot now because threads needs the socket
//        is.close();
//        s1.close();
//        System.out.println("Connection Closed");
    }
}
