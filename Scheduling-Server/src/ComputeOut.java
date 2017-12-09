import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ComputeOut implements Runnable {
    private Socket socket;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private TaskManager taskManager;

    public ComputeOut(Socket socket, ConcurrentHashMap<UUID, ClientTask> clientTaskMap,
                      TaskManager taskManager) {
        this.socket = socket;
        this.clientTaskMap = clientTaskMap;
        this.taskManager = taskManager;
    }

    @Override
    public void run() {
        // sprawdza czy są dane w task manager dla siebie i wysyła na socket
        // w pętli
        // OBsługa errora jeślie dostanie powiadomienei ze zerwało połączenie, tutaj
        // albo gdzieś indziej
    }
}
