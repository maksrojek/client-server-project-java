import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.mult.MatrixMatrixMult_DDRM;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Compute implements Runnable {

    // get task from TaskQueue (blocking)
    // compute
    // send result to ResultsQueue
    // update Availability object

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
        // TODO add logs (by console println)

        UUID taskID;
        ClientTask clientTask;
        ComputeData computeData;
        ArrayList<DMatrixRMaj> matrices;
        DMatrixRMaj A, B, result;
//        DMatrixRMaj result = new DMatrixRMaj();
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
                complexity = noRows * noCols / 2f;

                //compute
                result = new DMatrixRMaj(noRows, noCols);
                CommonOps_DDRM.mult(A,B, result);

                // send result
                computeResult = new ComputeResult(result);
                clientTask.setComputeResult(computeResult);
                resultsQueue.put(taskID);

                // update availability
                availability.decreaseValueBy(complexity);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        // TODO yield here?
        }
    }
}
