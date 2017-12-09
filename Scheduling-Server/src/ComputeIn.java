import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ComputeIn implements Runnable {
    private Socket socket;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;

    public ComputeIn(Socket socket, ConcurrentHashMap<UUID, ClientTask> clientTaskMap) {
        this.socket = socket;
        this.clientTaskMap = clientTaskMap;
    }

    @Override
    public void run() {
        // czeka na jakieś dane od ComputeServera i updatuje Availability lub wysyła
        // wyniki (odczytanie z mapy Tasku i wpisanie do niego result i sygnał do
        // Condition
    }
}
