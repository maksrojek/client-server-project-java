import java.util.concurrent.BlockingQueue;

public class TaskScheduler implements Runnable{
    private BlockingQueue<Integer> taskQueue;
    private ServerAvailability serverAvailability;
    private TaskManager taskManager;

    public TaskScheduler(BlockingQueue<Integer> taskQueue, ServerAvailability
            serverAvailability, TaskManager taskManager) {
        this.taskQueue = taskQueue;
        this.serverAvailability = serverAvailability;
        this.taskManager = taskManager;
    }

    @Override
    public void run() {
        while(true) {
            // pobierz elemencik z TaskQueue
            // pobierz obciążenie z ServerAvailability
            // przekaż id zadanie do taks manager
            // obsługa błedu gdy
        }
    }

}
