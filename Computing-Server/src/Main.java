import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

import static java.lang.Thread.sleep;

public class Main {

    static final int PORT_NUM = 4455;

    public static void main(String args[]) throws IOException {

        InetAddress address = InetAddress.getLocalHost();
        Socket s1 = null;
        String line = null;
        BufferedReader is = null;
        PrintWriter os = null;

        UUID ID = null;

        try {
            s1 = new Socket(address, PORT_NUM);
            is = new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os = new PrintWriter(s1.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Computing server address : " + address);

        String response = null;
        try {
            String serverID = is.readLine();
            ID = UUID.fromString(serverID);

            while (true) {
                line = is.readLine();
                sleep(Integer.valueOf(line) * 1000);
                os.println(ID.toString());
                os.flush();
                os.println("slept for " + line + " seconds");
                os.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
            s1.close();
            System.out.println("Connection Closed");

        }

    }
}
