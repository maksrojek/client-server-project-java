import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ClientExecutor implements Runnable {

    static final int PORT_NUM = 4445;
    private BlockingQueue<UUID> taskQueue;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;


    public ClientExecutor(BlockingQueue<UUID> taskQueue, ConcurrentHashMap<UUID,
            ClientTask> clientTaskMap) {
        this.taskQueue = taskQueue;
        this.clientTaskMap = clientTaskMap;
    }

    @Override
    public void run() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool
                (10);
        Socket socket = null;
        ServerSocket serverSocket = null;
        int clientID = 0;
        System.out.println("Server Listening.....");
        try {
            serverSocket = new ServerSocket(PORT_NUM);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        while (true) {
            try {
                socket = serverSocket.accept();
                System.out.println("connection established, clientId: " + clientID);

                // create new thread for new client
                // in constructor: socket, taskQueue, clientTaskMap, id
                executor.execute(new ClientThread(socket, taskQueue, clientTaskMap,
                        clientID));

                ++clientID; // new ID TODO UUID
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
        //        TODO add executor close/shutdown
    }
}
