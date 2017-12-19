import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Store queue tasks for each computing server.
 */
public class TaskManager {

    private ConcurrentHashMap<String, BlockingQueue<UUID>> map;

    public TaskManager() {
        // TODO max size in constructor
        this.map = new ConcurrentHashMap<>();
    }

    public void addServer(String serverID, ArrayBlockingQueue<UUID> blockingQueue) {
        // TODO synchronization?
        map.put(serverID, blockingQueue);
    }

    public void putTask(String server, UUID task) {
        try {
            map.get(server).put(task);
        } catch (InterruptedException e) {
            System.out.println("TaskManger - error in putting task");
            e.printStackTrace();
        }
    }


    // wyjątki!
    // jeśłi chce dodać do kolejki która nie istnieje
}
