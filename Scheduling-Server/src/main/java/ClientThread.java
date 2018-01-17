import org.ejml.data.DMatrixRMaj;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * That class is responsible of communication with client. Receives task from client
 * and sends results.
 */
public class ClientThread implements Runnable {

    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private Socket s;

    private BlockingQueue<UUID> taskQueue;
    private ConcurrentHashMap<UUID, ClientTask> clientTaskMap;
    private int clientID;

    public ClientThread(Socket s, BlockingQueue<UUID> taskQueue,
                        ConcurrentHashMap<UUID, ClientTask> clientTaskMap, int clientID) {
        this.s = s;
        this.taskQueue = taskQueue;
        this.clientTaskMap = clientTaskMap;
        this.clientID = clientID;
    }

    /**
     * It works in endless loop:
     * - receives task,
     * - sends that task to task queue,
     * - wait for result,
     * - sends result to client.
     * end loop
     */
    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(s.getInputStream());
            oos = new ObjectOutputStream(s.getOutputStream());


        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        DMatrixRMaj A, B;

        try {
            A = (DMatrixRMaj) ois.readObject();
            B = (DMatrixRMaj) ois.readObject();

            while (true) {
                // lock.lock();
                // TODO data validation (A, B)
                ArrayList<DMatrixRMaj> matrices = new ArrayList<>(2);
                matrices.add(A);
                matrices.add(B);
                ComputeData computeData = new ComputeData(matrices);
                ClientTask clientTask = new ClientTask(computeData, new CountDownLatch
                        (1));
                UUID taskID = UUID.randomUUID();
                clientTaskMap.put(taskID, clientTask);
                taskQueue.put(taskID);

                System.out.println("Sleep -> ClientID: " + clientID + " taskID: " +
                        taskID); // for debug

                clientTask.getCountDownLatch().await();

//                System.out.println("Wakeup -> ClientID: " + clientID + " result: " +
//                        clientTask.getComputeResult().getResult() + ", taskID: " +
//                        taskID); //
                // for debug

                // send response to client
                oos.writeObject(clientTask.getComputeResult().getResult());
                // send response
                oos.flush();
                // wait for next message
                A = (DMatrixRMaj) ois.readObject();
                B = (DMatrixRMaj) ois.readObject();
            }
        } catch (IOException e) {

            //            line = this.getName(); //reused String line for getting
            // thread name
            System.out.println("IO Error/ Client " + clientID + " terminated " +
                    "abruptly");
        } catch (NullPointerException e) {
            //            line = this.getName(); //reused String line for getting
            // thread name
            System.out.println("Client " + clientID + " Closed");
        } catch (InterruptedException e) {
            System.out.println("Error in putting task into taskQueue");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error while reading objects from ois");
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (ois != null) {
                    ois.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (oos != null) {
                    oos.close();
                    System.out.println("Socket Out Closed");
                }
                if (s != null) {
                    s.close();
                    System.out.println("Socket Closed");
                }

            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }//end finally
    }
}
