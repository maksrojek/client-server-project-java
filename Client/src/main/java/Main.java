import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.RandomMatrices_DDRM;

public class Main {

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
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        String line;
        DMatrixRMaj A, B, response;
        int counter = 0;
        try {
            line = String.valueOf(1000);
//            line = br.readLine();
            //TODO: check if 'line' is number (>0) or "QUIT", refuse others

            while (line.compareTo("QUIT") != 0) {
                //TODO:import or generate nxm, mxk matrices to increase complexity
                //generating random square matrices based on input
                A = generateRandomSquareMatrix(Integer.parseInt(line), 1, 50);
                B = generateRandomSquareMatrix(Integer.parseInt(line), 1, 50);

                oos.writeObject(A);
                oos.writeObject(B);
                oos.flush();
                response = (DMatrixRMaj) ois.readObject();
                //printServerResponse(A, B, response);
                //System.out.println("Result is 'correct': " + compareResult(A, B,
                    //    response));

                //line = br.readLine();
                if (counter >= 10) break;
                counter++;
                line = String.valueOf(1000);
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

    }


}