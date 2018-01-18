import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * Get tasks from queue and puts them to least occupied compute server
 */
public class TaskScheduler implements Runnable {
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
        UUID taskID = UUID.randomUUID();
        while (true) {
            try {
                // get task from taskQueue
                taskID = taskQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("TaskScheduler - task:" + taskID);

            // get server with minimal load
            String serverID = serverAvailability.getMinServer();
            System.out.println("TaskScheduler: send task to server: " + serverID);

            // send task to least occupied server
            taskManager.putTask(serverID, taskID);
        }
    }
}
