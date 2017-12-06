import java.util.concurrent.locks.Condition;

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
