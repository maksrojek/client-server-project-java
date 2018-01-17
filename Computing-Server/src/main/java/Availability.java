import java.io.IOException;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Availability {
    private Float value;

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

    public synchronized Float getValue() {
        return value;
    }

    public synchronized void increaseValueBy(Float value) {
        this.value += value;
        LOGGER.log(Level.INFO, String.valueOf(this.value));
    }

    public synchronized void decreaseValueBy(Float value){
        this.value -= value;
        if (this.value < 0)
            this.value = 0f;
        LOGGER.log(Level.INFO, String.valueOf(this.value));
    }

    private void createLogFile(UUID serverID) {
        try {
            fileTxt = new FileHandler("Logging.txt");
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
