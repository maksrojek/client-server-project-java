import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * Get tasks from queue and puts them to least occupied compute server
 */
public class TaskScheduler implements Runnable{
    private BlockingQueue<UUID> taskQueue;
    private ServerAvailability serverAvailability;
    private TaskManager taskManager;

    public TaskScheduler(BlockingQueue<UUID> taskQueue, ServerAvailability
            serverAvailability, TaskManager taskManager) {
        this.taskQueue = taskQueue;
        this.serverAvailability = serverAvailability;
        this.taskManager = taskManager;
    }

    @Override
    public void run() {
        UUID taksID = UUID.randomUUID();
        while(true) {
            try {
                taksID = taskQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("TaskScheduler - task:"  + taksID);

            // pobierz elemencik z TaskQueue
            // pobierz obciążenie z ServerAvailability
            // przekaż id zadanie do taks manager
            // obsługa błedu gdy
        }
    }

}
