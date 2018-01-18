import java.util.UUID;
import java.util.concurrent.*;

public class Main {

    public static void main(String args[]) {

        BlockingQueue<UUID> taskQueue = new ArrayBlockingQueue<>(10);
        ConcurrentHashMap<UUID, ClientTask> clientTaskMap = new ConcurrentHashMap<>();
        TaskManager taskManager = new TaskManager();
        ServerAvailability serverAvailability = new ServerAvailability();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool
                (3);
        executor.execute(new ClientExecutor(taskQueue, clientTaskMap));
        executor.execute(new TaskScheduler(taskQueue, serverAvailability, taskManager));
        executor.execute(new ComputeServerExecutor(clientTaskMap, serverAvailability,
                taskManager));
    }

}
