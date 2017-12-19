import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * That class is responsible of communication with client. Receives task from client
 * and sends results.
 */
public class ClientThread implements Runnable {

    private String line = null;
    private BufferedReader is = null;
    private PrintWriter os = null;
    private Socket s = null;

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
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {
            line = is.readLine();
            while (line.compareTo("QUIT") != 0) {
                // lock.lock();
                // TODO data validation (line)
                ComputeData computeData = new ComputeData(line);
                ClientTask clientTask = new ClientTask(computeData, new CountDownLatch
                        (1));
                UUID taskID = UUID.randomUUID();
                clientTaskMap.put(taskID, clientTask);
                taskQueue.put(taskID);

                System.out.println("Sleep -> ClientID: " + clientID + ", message: " +
                        line + ", " +
                        "taskID: " + taskID); // for debug

                clientTask.getCountDownLatch().await();

                System.out.println("Wakeup -> ClientID: " + clientID + ", message: "
                        + line + " result: " + clientTask.getComputeResult().getResult()
                        + ", taskID: " + taskID); //
                // for debug

                os.println(clientTask.getComputeResult().getResult()); // response to
                // client
                os.flush(); // send response
                line = is.readLine(); // wait for next message
            }
        } catch (IOException e) {

            //            line = this.getName(); //reused String line for getting
            // thread name
            System.out.println("IO Error/ Client " + line + " terminated " +
                    "abruptly");
        } catch (NullPointerException e) {
            //            line = this.getName(); //reused String line for getting
            // thread name
            System.out.println("Client " + line + " Closed");
        } catch (InterruptedException e) {
            System.out.println("Error in putting task into taksQueue");
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (is != null) {
                    is.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (os != null) {
                    os.close();
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
