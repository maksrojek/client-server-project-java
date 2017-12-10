import java.util.concurrent.ConcurrentHashMap;

/**
 * Store computing server availability
 */
public class ServerAvailability {
    private ConcurrentHashMap<String, Float> map;

    public ServerAvailability() {
        map = new ConcurrentHashMap<>();
    }

    /**
     * Add or update server availability
     *
     * @param UUID  Computing server ID
     * @param value Current computing server load
     */
    public void addServer(String UUID, Float value) {
        map.put(UUID, value);
        System.out.println("Added or updated server: " + UUID + " load " + value);
    }

    /**
     * Remove server from available server list
     *
     * @param UUID Computing server ID
     */
    public void removeServer(String UUID) {
        map.remove(UUID);
    }

    /**
     * Return leas loaded server
     *
     * @return Least loaded server
     */
    public String getMinServer() {
        String UUID = null;
        for (String current : map.keySet()) {
            if (UUID == null) {
                UUID = current;
            }
            if (map.get(current) < map.get(UUID)) {
                UUID = current;
            }
        }
        return UUID;
    }


}
