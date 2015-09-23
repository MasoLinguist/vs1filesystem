package htw.vs1.filesystem.Network.Discovery;

import com.sun.istack.internal.NotNull;

import java.util.Date;
import java.util.Objects;

/**
 * A FileSystemServer represents a remote server
 * running a file system instance.
 * This class contains all necessary information
 *  to connect with this server.
 *
 *  Host and Port is unique for a FileSystemServer
 *  instance.
 *
 * Created by Felix on 22.09.2015.
 */
public class FileSystemServer {

    // Livetime is 4 times the broadcast interval
    private static final long MAX_LIVE_TIME_MILLIS = DiscoveryBroadcaster.BROADCAST_INTERVAL * 4;
    /**
     * The IP-Address of the file system
     * server.
     */
    private String host;

    /**
     * The Port of the file system
     * instance.
     */
    private int port;

    /**
     * The time when the server was
     * discovered.
     */
    private Date discoveryTime;

    public FileSystemServer(@NotNull String host, int port) {
        this.host = host;
        this.port = port;
        this.discoveryTime = new Date();
    }

    public int getPort() {
        return port;
    }

    public @NotNull String getHost() {
        return host;
    }

    public Date getDiscoveryTime() {
        return discoveryTime;
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }

    public boolean isOutdated() {
        long currentTimeMillis = System.currentTimeMillis();
        long discoverMillis = discoveryTime.getTime();
        long diff = currentTimeMillis - discoverMillis;

        return (diff > MAX_LIVE_TIME_MILLIS);
    }

    @Override
    public int hashCode() {
        return (host + String.valueOf(port)).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FileSystemServer
                && Objects.equals(host, ((FileSystemServer) obj).getHost())
                && port == ((FileSystemServer) obj).getPort();

    }
}
