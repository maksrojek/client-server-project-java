import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulerIn implements Runnable {

    // get task
    // add to clientTaskMap
    // send to TaskQueue
    // update Availability object

    private Socket socket;
    private UUID serverID;
    private BlockingQueue<UUID> taskQueue;
    private Availability availability;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private ObjectInputStream ois = null;


    public SchedulerIn(Socket socket, UUID serverID, BlockingQueue<UUID> taskQueue,
                       Availability availability, ConcurrentHashMap<UUID, ClientTask>
                               clientTaskMap) {
        this.socket = socket;
        this.serverID = serverID;
        this.taskQueue = taskQueue;
        this.availability = availability;
        this.clientTaskMap = clientTaskMap;
    }

    @Override
    public void run() {
        // TODO add logs (by console println)

        try {
            ois = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<DMatrixRMaj> data;
        ClientTask clientTask = new ClientTask();
        ComputeData computeData;
        int noRows, noCols;
        float complexity;
        UUID taskID = null;

        while (true) {

            try {
                // retrieve data from scheduler
                taskID = (UUID) ois.readObject();
                System.out.println("SchedulerIn: got TaskID: " + taskID);
                data = (ArrayList<DMatrixRMaj>) ois.readObject();

                computeData = new ComputeData(data);
                clientTask.setComputeData(computeData);

                // send data for computing
                clientTaskMap.put(taskID, clientTask);
                taskQueue.put(taskID);

                // calculate complexity
                noRows = data.get(0).getNumRows();
                noCols = data.get(1).getNumCols();
                complexity = noRows * noCols / 2f;

                // update Availability
                availability.increaseValueBy(complexity);

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

    }
}
