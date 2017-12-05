import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    static final int PORT_NUM = 4445;

    public static void main(String args[]) {


        Socket s = null;
        ServerSocket ss2 = null;
        System.out.println("Server Listening......");
        try {
            ss2 = new ServerSocket(PORT_NUM);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        while (true) {
            try {
                s = ss2.accept();
                System.out.println("connection Established");
                ServerThread st = new ServerThread(s);
                st.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }

    }

}
