/**
 * Store task to compute, countDownLatch to thread connected with that task and result
 * of task
 */
public class ClientTask {
    private ComputeData computeData;
    private ComputeResult computeResult;

    public ClientTask() {
        this.computeData = null;
        this.computeResult = null;
    }

    public ComputeData getComputeData() {
        return computeData;
    }

    public void setComputeData(ComputeData computeData) {
        this.computeData = computeData;
    }

    public ComputeResult getComputeResult() {
        return computeResult;
    }

    public void setComputeResult(ComputeResult result) {
        this.computeResult = result;
    }
}