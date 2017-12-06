import java.util.concurrent.*;

public class Main {



    public static void main(String args[]) {
        // server
        /*
        stwórz kolejkę blockingQueue1 KolejkaZadań do przekazywania zadań
        hashmapa dostępności (obłożenie serverów)
        hashmapę kolejek dla każdego servera obliczeniowego
            - jeśli dołącza się server obliczeniowy tworzone są 2 wątki (in out), nowa
            kolejaka (dodana do hashmapy)
            oraz nowy wpis do hashmapy dostępności


        Stwórz executor z 3 wątkami
            - komunikacja z klientem
                - potrzebuje (blockinQueue1)
            - odbieranie danych z kolejki
                - potrzebuje BlockingQueue1
                - potrzebuje Hashmapę kolejek
                - potrzebuje hashmapę dostępności
            - komunikacja z obliczeniowymi
                - hashmapę dostępności
                - hashmapę kolejek
        czekaj na zakończenie tych wątków
         */

        BlockingQueue<Integer> taskQueue = new ArrayBlockingQueue<>(10);
        ConcurrentHashMap<Integer, ClientTask> clientTaskMap = new ConcurrentHashMap<>();
        TaskManager taskManager = new TaskManager();
        ServerAvailability serverAvailability = new ServerAvailability();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        executor.execute(new ClientExecutor(taskQueue, clientTaskMap));
        executor.execute(new TaskScheduler(taskQueue, serverAvailability, taskManager));
        executor.execute(new ComputeServerExecutor(clientTaskMap, serverAvailability, taskManager));

//        executor.shutdown();
        // tutaj powinno byc czekanie na cośtam od admina? wyłącz?
    }

}
