import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sends task to remote computing server
 */
public class ComputeOut implements Runnable {
    ArrayBlockingQueue<UUID> blockingQueue;
    private Socket socket;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private PrintWriter os = null;

    public ComputeOut(Socket socket, ConcurrentHashMap<UUID, ClientTask> clientTaskMap,
                      ArrayBlockingQueue<UUID> blockingQueue) {
        this.socket = socket;
        this.clientTaskMap = clientTaskMap;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {

        try {
            os = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        UUID taskID = null;
        while (true) {
            try {
                taskID = blockingQueue.take();
                System.out.println("ComputeOut: Get task from queue");
                // get next task
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ClientTask task = clientTaskMap.get(taskID);
            ComputeData<String> computeData = task.getComputeData();
            String data = computeData.getData();
            os.println(taskID);
            os.flush();
            System.out.println("ComputeOut: TaskID send" + taskID);
            os.println(data);
            os.flush();
            System.out.println("ComputeOut: Task data send" + task);
        }


        // OBsługa errora jeślie dostanie powiadomienei ze zerwało połączenie, tutaj
        // albo gdzieś indziej
    }
}
