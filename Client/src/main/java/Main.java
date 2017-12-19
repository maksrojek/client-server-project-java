import org.ejml.simple.SimpleMatrix;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class Main {

    static final int PORT_NUM = 4445;
    static Random rand = new Random();

    public static SimpleMatrix generateSquareMatrix(Integer dimension) {
        return SimpleMatrix.random64(dimension, dimension, 1, 50, rand);
    }

    public static void serverResponse(SimpleMatrix A, SimpleMatrix B, SimpleMatrix resp) {
        System.out.println("Input matrices:");
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
        String line = null;
        BufferedReader br = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            s1 = new Socket(address, PORT_NUM);
            br = new BufferedReader(new InputStreamReader(System.in));
            ois = new ObjectInputStream(s1.getInputStream());
            oos = new ObjectOutputStream(s1.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : " + address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        SimpleMatrix response = null;
        try {
            line = br.readLine();
            //TODO: check if 'line' is number (>0) or "QUIT", refuse others

            while (line.compareTo("QUIT") != 0) {
                //TODO: generate nxm and mxk matrices to increase complexity
                //generate random square matrices based on input
                SimpleMatrix A = generateSquareMatrix(Integer.parseInt(line));
                SimpleMatrix B = generateSquareMatrix(Integer.parseInt(line));

                oos.writeObject(A);
                oos.writeObject(B);
                oos.flush();
                response = (SimpleMatrix) ois.readObject();
                serverResponse(A, B, response);
                line = br.readLine();
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