import org.ejml.data.DMatrixRMaj;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulerIn implements Runnable {

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
        try {
            ois = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<DMatrixRMaj> data;
        ClientTask clientTask;
        ComputeData computeData;
        int noRows, noCols;
        float complexity;
        UUID taskID = null;

        while (true) {

            try {
                // retrieve data from scheduler
                taskID = (UUID) ois.readObject();
                data = (ArrayList<DMatrixRMaj>) ois.readObject();

                computeData = new ComputeData(data);
                clientTask = new ClientTask();
                clientTask.setComputeData(computeData);

                // calculate complexity
                noRows = data.get(0).getNumRows();
                noCols = data.get(1).getNumCols();
                complexity = noRows + noCols;

                // update Availability
                availability.increaseValueBy(complexity);

                // send data for computing
                clientTaskMap.put(taskID, clientTask);
                taskQueue.put(taskID);

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

    }
}
