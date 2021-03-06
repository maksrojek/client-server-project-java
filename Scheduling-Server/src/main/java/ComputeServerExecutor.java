import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Establish a connection with computing servers. Makes two threads for each server.
 * One for input messages and one for output.
 */
public class ComputeServerExecutor implements Runnable {

    static final int PORT_NUM = 4455;
    private PrintWriter os = null;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private ServerAvailability serverAvailability;
    private TaskManager taskManager;

    public ComputeServerExecutor(ConcurrentHashMap<UUID, ClientTask> clientTaskMap,
                                 ServerAvailability serverAvailability, TaskManager
                                         taskManager) {
        this.clientTaskMap = clientTaskMap;
        this.serverAvailability = serverAvailability;
        this.taskManager = taskManager;
    }

    @Override
    public void run() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool
                (10);
        Socket socket;
        ServerSocket serverSocket = null;
        System.out.println("Listening for computing servers...");
        try {
            serverSocket = new ServerSocket(PORT_NUM);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        String serverID;
        while (true) {
            try {
                socket = serverSocket.accept();
                os = new PrintWriter(socket.getOutputStream());
                serverID = UUID.randomUUID().toString();
                os.println(serverID);
                os.flush();
                System.out.println("connection established, serverID: " + serverID);
                ArrayBlockingQueue<UUID> blockingQueue = new ArrayBlockingQueue<UUID>(20);
                taskManager.addServer(serverID, blockingQueue);
                serverAvailability.addServer(serverID, 0.0f);
                // create 2 threads: for input and for output
                executor.execute(new ComputeOut(socket, clientTaskMap, blockingQueue));
                executor.execute(new ComputeIn(serverID, socket, clientTaskMap,
                        serverAvailability));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
    }
}

