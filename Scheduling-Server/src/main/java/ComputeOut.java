import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sends task to remote computing server
 */
public class ComputeOut implements Runnable {
    private ArrayBlockingQueue<UUID> blockingQueue;
    private Socket socket;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private ObjectOutputStream oos = null;

    public ComputeOut(Socket socket, ConcurrentHashMap<UUID, ClientTask> clientTaskMap,
                      ArrayBlockingQueue<UUID> blockingQueue) {
        this.socket = socket;
        this.clientTaskMap = clientTaskMap;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        UUID taskID;
        ArrayList<DMatrixRMaj> data;
        while (true) {
            try {
                taskID = blockingQueue.take();
                System.out.println("ComputeOut: Get task from queue");
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            ClientTask task = clientTaskMap.get(taskID);
            ComputeData computeData = task.getComputeData();
            data = (ArrayList<DMatrixRMaj>) computeData.getData();

            // sending UUID and task data to Computing Server
            try {
                oos.writeObject(taskID);
                System.out.println("ComputeOut: TaskID sent " + taskID);
                oos.writeObject(data);
                System.out.println("ComputeOut: Task data sent " + "->matrices here<-");
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO Error - ComputeOut while sending UUID/data");
            }
        } // end while
    }
}
