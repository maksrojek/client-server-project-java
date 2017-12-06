import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ClientExecutor implements Runnable{

    static final int PORT_NUM = 4445;
    private BlockingQueue<Integer> taskQueue;
    private ConcurrentHashMap<Integer, ClientTask> clientTaskMap;


    public ClientExecutor(BlockingQueue<Integer> taskQueue, ConcurrentHashMap<Integer,
            ClientTask> clientTaskMap) {
        this.taskQueue = taskQueue;
        this.clientTaskMap = clientTaskMap;
    }

    @Override
    public void run() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        Socket s = null;
        ServerSocket ss2 = null;
        System.out.println("Server Listening.....");
        try {
            ss2 = new ServerSocket(PORT_NUM);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        while (true) {
            try {
                s = ss2.accept();
                System.out.println("connection Established");
                // zrobienie nowego ID
                // unique UUID
                // stowrzenie nowego Zadanaka Runnable z ID i mapą (clientThread)
                // Dodanie tego do executora
                // to pnieżęj wywalić
                ServerThreadTemplate st = new ServerThreadTemplate(s);
                st.start();
//                executor.execute(new ClientExecutor(taskQueue, clientTaskMap));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
//        dopisać zamknięcie executora
//        executor.shutdown();
    }
}
