import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.RandomMatrices_DDRM;
import org.ejml.ops.MatrixIO;

public class Main implements Serializable {

    static final int PORT_NUM = 4445;
    static Random rand = new Random();

    public static DMatrixRMaj generateRandomSquareMatrix(int dimension, double
            minValue, double maxValue) {
        return RandomMatrices_DDRM.symmetric(dimension, minValue, maxValue, rand);
    }


    public static boolean compareResult(DMatrixRMaj A, DMatrixRMaj B, DMatrixRMaj C) {
        int noRows = A.getNumRows();
        int noCols = B.getNumCols();
        DMatrixRMaj multResult = new DMatrixRMaj(noRows, noCols);
        CommonOps_DDRM.mult(A, B, multResult);
        System.out.println("Internal A and B multiplication result: ");
        System.out.println(multResult);
        return C.toString().equals(multResult.toString());
    }

    public static void printServerResponse(Object A, Object B, Object resp) {
        System.out.println("Input:");
        System.out.println("A:");
        System.out.println(A);
        System.out.println("B:");
        System.out.println(B);
        System.out.println("Server Response :");
        System.out.println(resp);
    }

    public static void main(String args[]) throws IOException {

        InetAddress address = InetAddress.getLocalHost();
        Socket s1 = null;
        BufferedReader br = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            s1 = new Socket(address, PORT_NUM);
            br = new BufferedReader(new InputStreamReader(System.in));
            oos = new ObjectOutputStream(s1.getOutputStream());
            ois = new ObjectInputStream(s1.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : " + address);

        // Oficial version
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");
        DMatrixRMaj A, B, response;
        String file1, file2, result;

        try {
            System.out.println("Enter the path to the first file: ");
            file1 = br.readLine();
            System.out.println("Enter the path to the second file: ");
            file2 = br.readLine();

            while (file1.compareTo("QUIT") != 0 && file2.compareTo("QUIT") != 0) {

                A = MatrixIO.loadCSV(file1);
                B = MatrixIO.loadCSV(file2);
                oos.writeObject(A);
                oos.writeObject(B);
                oos.flush();
                response = (DMatrixRMaj) ois.readObject();

                printServerResponse(A, B, response);

                System.out.println("Enter the path to the result file: ");
                result = br.readLine();
                MatrixIO.saveCSV(response, result);

                System.out.println("Enter the path to the first file: ");
                file1 = br.readLine();
                System.out.println("Enter the path to the second file: ");
                file2 = br.readLine();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
        } finally {
            ois.close();
            oos.close();
            br.close();
            s1.close();
            System.out.println("Connection Closed");
        }

        // Test version
//        System.out.println("Working...");
//
//        String line;
//        DMatrixRMaj A, B, response;
//        int counter = 0;
//        Random randomGenerator = new Random();
//
//        try {
//            line = String.valueOf(randomGenerator.nextInt(1000));
//
//            while (line.compareTo("QUIT") != 0) {
//                A = generateRandomSquareMatrix(Integer.parseInt(line), 1, 50);
//                B = generateRandomSquareMatrix(Integer.parseInt(line), 1, 50);
//
//                oos.writeObject(A);
//                oos.writeObject(B);
//                oos.flush();
//
//                response = (DMatrixRMaj) ois.readObject();
//
//                line = String.valueOf(randomGenerator.nextInt(1000));
//                if (counter >= 10) break;
//                counter++;
//            }
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//            System.out.println("Socket read Error");
//        } finally {
//            ois.close();
//            oos.close();
//            br.close();
//            s1.close();
//            System.out.println("Connection Closed");
//
//        }
    }
}