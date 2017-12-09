import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ComputeServerExecutor implements Runnable {
    public ComputeServerExecutor(ConcurrentHashMap<UUID, ClientTask> clientTaskMap,
                                 ServerAvailability serverAvailability, TaskManager
                                         taskManager) {

    }

    @Override
    public void run() {
        // w pętli robi to samo co ClientExecutor ale tworzy 2 wątki dla jednego
        // servera i podaje im socket
        // ComputeServera In oraz Out - ComputeIn, ComputeOut. tworzy dla każdego nową listę w TaskManager
        // i wystąpnie w ServerAvailability
        // i czeka na kolejne servery
    }
}
