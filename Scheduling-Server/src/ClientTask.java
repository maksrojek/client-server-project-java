import java.util.concurrent.CountDownLatch;

/**
 * Store task to compute, countDownLatch to thread connected with that task and result
 * of task
 */
public class ClientTask {
    private ComputeData computeData;
    private CountDownLatch countDownLatch;
    private ComputeResult computeResult;

    public ClientTask(ComputeData computeData, CountDownLatch countDownLatch) {
        this.computeData = computeData;
        this.countDownLatch = countDownLatch;
        computeResult = null;
    }

    public ComputeData getComputeData() {
        return computeData;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public ComputeResult getComputeResult() {
        return computeResult;
    }

    public void setComputeResult(ComputeResult result) {
        this.computeResult = result;
    }
}
