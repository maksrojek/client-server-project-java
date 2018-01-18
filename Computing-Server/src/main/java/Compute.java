import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Compute implements Runnable {
    private BlockingQueue<UUID> taskQueue;
    private Availability availability;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private BlockingQueue<UUID> resultsQueue;

    public Compute(BlockingQueue<UUID> taskQueue, Availability availability,
                   ConcurrentHashMap<UUID, ClientTask> clientTaskMap,
                   BlockingQueue<UUID> resultsQueue) {
        this.taskQueue = taskQueue;
        this.availability = availability;
        this.clientTaskMap = clientTaskMap;
        this.resultsQueue = resultsQueue;
    }

    @Override
    public void run() {
        UUID taskID;
        ClientTask clientTask;
        ComputeData computeData;
        ArrayList<DMatrixRMaj> matrices;
        DMatrixRMaj A, B, result;
        ComputeResult computeResult;
        int noRows, noCols;
        float complexity;

        while (true) {
            try {
                // get matrices to multiply
                taskID = taskQueue.take();
                clientTask = clientTaskMap.get(taskID);
                computeData = clientTask.getComputeData();
                matrices = (ArrayList<DMatrixRMaj>) computeData.getData();

                // compute
                A = matrices.get(0);
                B = matrices.get(1);
                noRows = A.getNumRows();
                noCols = B.getNumCols();
                complexity = noRows + noCols;

                //compute
                result = new DMatrixRMaj(noRows, noCols);
                CommonOps_DDRM.mult(A, B, result);

                // send result
                computeResult = new ComputeResult(result);
                clientTask.setComputeResult(computeResult);

                // update availability
                availability.decreaseValueBy(complexity);

                resultsQueue.put(taskID);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
