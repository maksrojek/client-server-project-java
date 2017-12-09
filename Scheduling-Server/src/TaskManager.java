import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class TaskManager {

    private ConcurrentHashMap<String, BlockingQueue<UUID>> map;

    public TaskManager() {
        // TODO max size in constructor
        this.map = new ConcurrentHashMap<>();
    }

    public void addServer(String serverID) {
        // TODO synchronization?
        map.put(serverID, new ArrayBlockingQueue<UUID>(20));
    }


    // wyjątki!
    // jeśłi chce dodać do kolejki która nie istnieje
}
