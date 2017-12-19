import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ComputeIn implements Runnable {
    private String serverID;
    private Socket socket;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private ServerAvailability serverAvailability;
    private BufferedReader is = null;

    public ComputeIn(String serverID, Socket socket, ConcurrentHashMap<UUID,
            ClientTask> clientTaskMap,
                     ServerAvailability serverAvailability) {
        this.serverID = serverID;
        this.socket = socket;
        this.clientTaskMap = clientTaskMap;
        this.serverAvailability = serverAvailability;
    }

    @Override
    public void run() {
        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line1 = null;
        String line2 = null;
        while (true) {
            try {
                System.out.println("ComputeIn Waiting: ");
                line1 = is.readLine();
                System.out.println("ComputeIn line1: " + line1);
                line2 = is.readLine();
                System.out.println("ComputeIn line2: " + line2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line1.equals("load")) {
                serverAvailability.addServer(serverID, Float.parseFloat(line2));
            } else {
                ClientTask clientTask = clientTaskMap.get(UUID.fromString(line1));
                ComputeResult<String> computeResult = new ComputeResult(line2);
                clientTask.setComputeResult(computeResult);
                clientTask.getCountDownLatch().countDown();
            }
        }
        // czeka na jakieś dane od ComputeServera i updatuje Availability lub wysyła
        // wyniki (odczytanie z mapy Tasku i wpisanie do niego result i sygnał do
        // Condition
    }
}
