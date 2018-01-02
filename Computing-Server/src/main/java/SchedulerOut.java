import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SchedulerOut implements Runnable {

    // check id Availability object has been updated
    // send availability if had been changed
    // get result from ResultsQueue
    // send result if exist

    private Socket socket;
    private Availability availability;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private BlockingQueue<UUID> resultsQueue;
    private ObjectOutputStream oos = null;


    public SchedulerOut(Socket socket, Availability availability,
                        ConcurrentHashMap<UUID, ClientTask> clientTaskMap,
                        BlockingQueue<UUID> resultsQueue) {
        this.socket = socket;
        this.availability = availability;
        this.clientTaskMap = clientTaskMap;
        this.resultsQueue = resultsQueue;
    }

    @Override
    public void run() {
        // TODO add logs (by console println)

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mode;
        Float lastKnownValue = availability.getValue();
        UUID taskID;
        ClientTask clientTask;
        ComputeResult computeResult;

        while (true) {
            if (!lastKnownValue.equals(availability.getValue())) {
                // Availability had been updated, send new value to scheduler
                mode = "load";
                lastKnownValue = availability.getValue();
                try {
                    oos.writeObject(mode);
                    oos.writeFloat(lastKnownValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // send results if any
            try {
                taskID = resultsQueue.poll(100, TimeUnit.MILLISECONDS);
                if (taskID != null) {
                    // got result, send to scheduler
                    clientTask = clientTaskMap.get(taskID);
                    computeResult = clientTask.getComputeResult();
                    mode = "result";

                    oos.writeObject(mode);
                    oos.writeObject(taskID);
                    oos.writeObject(computeResult.getResult());
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                break;
            }
        } // end while
    }
}
