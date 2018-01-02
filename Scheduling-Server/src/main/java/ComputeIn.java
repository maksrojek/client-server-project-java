import org.ejml.data.DMatrixRMaj;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ComputeIn implements Runnable {
    private String serverID;
    private Socket socket;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private ServerAvailability serverAvailability;
    private ObjectInputStream ois = null;

    public ComputeIn(String serverID, Socket socket, ConcurrentHashMap<UUID,
            ClientTask> clientTaskMap,
                     ServerAvailability serverAvailability) {
        this.serverID = serverID;
        this.socket = socket;
        this.clientTaskMap = clientTaskMap;
        this.serverAvailability = serverAvailability;
    }

    // czeka na jakieś dane od ComputeServera i updatuje Availability lub wysyła
    // wyniki (odczytanie z mapy Tasku i wpisanie do niego result i sygnał do
    // Condition
    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            // TODO
        }
        UUID taskID;
        DMatrixRMaj result;
        String mode; //possible modes: result, load
        Float workload;
        while (true) {
            try {
                System.out.println("ComputeIn Waiting: ");
                mode = (String) ois.readObject();
                System.out.println("ComputeIn mode: " + mode);

                if (mode.equals("result")) {
                    taskID = (UUID) ois.readObject();
                    System.out.println("ComputeIn taskID : " + taskID);
                    result = (DMatrixRMaj) ois.readObject();
                    System.out.println("ComputeIn result: " + "->result matrix<-");

                    ClientTask clientTask = clientTaskMap.get(taskID);
                    clientTask.setComputeResult(new ComputeResult<>(result));
                    clientTask.getCountDownLatch().countDown();

                } else if (mode.equals("load")) {
                    workload = ois.readFloat();
                    serverAvailability.addServer(serverID, workload);
                } else {
                    System.out.println("something went wrong when reading ComputeIn " +
                            "mode variable");
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                System.out.println("Error while reading objects from ois");
                e.printStackTrace();
                break;
            }
        } // end while


    }
}
