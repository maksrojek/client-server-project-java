import java.util.concurrent.locks.Condition;

/**
 * Store task to compute, condition to thread connected with that task and result of task
 */
public class ClientTask {
    private ComputeData computeData;
    private Condition condition;
    private ComputeResult computeResult;

    public ClientTask(ComputeData computeData, Condition condition) {
        this.computeData = computeData;
        this.condition = condition;
        computeResult = null;
    }
}
