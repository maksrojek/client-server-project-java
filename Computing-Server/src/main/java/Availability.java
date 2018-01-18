import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.*;

public class Availability {
    private volatile Float value;
    ReentrantLock lock = new ReentrantLock();

    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    public Availability(UUID serverID) {
        this.value = 0f;
        createLogFile(serverID);
    }

    public Availability(Float value) {
        this.value = value;
    }

    public Float getValue() {
        lock.lock();
        try {
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void increaseValueBy(Float value) {
        lock.lock();
        try {
            this.value += value;
            LOGGER.log(Level.INFO, String.valueOf(this.value));
        } finally {
            lock.unlock();
        }
    }

    public void decreaseValueBy(Float value) {
        lock.lock();
        try {
            this.value -= value;
            if (this.value < 0)
                this.value = 0f;
            LOGGER.log(Level.INFO, String.valueOf(this.value));
        } finally {
            lock.unlock();
        }
    }

    private void createLogFile(UUID serverID) {
        try {
            fileTxt = new FileHandler("Logging");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        LOGGER.addHandler(fileTxt);
        LOGGER.log(Level.INFO, "Server started\nID: " + serverID);
    }
}
