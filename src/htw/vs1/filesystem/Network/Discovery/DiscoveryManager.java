package htw.vs1.filesystem.Network.Discovery;

import com.sun.istack.internal.NotNull;
import htw.vs1.filesystem.Network.TCPParallelServer;

import java.util.*;

/**
 * Created by Felix on 22.09.2015.
 */
public class DiscoveryManager {

    protected static final int DISCOVERY_PORT = TCPParallelServer.DEFAULT_PORT;

    private static DiscoveryManager mInstance = new DiscoveryManager();

    public static DiscoveryManager getInstance() {
        return mInstance;
    }

    private Set<FileSystemServer> discoveredServerInstances = new HashSet<>();

    private DiscoveryBroadcaster broadcaster;

    private DiscoveryListener listener;

    private List<DiscoveredServersObserver> observers = new LinkedList<>();

    private DiscoveryManager() {
    }

    public void add(@NotNull String host, int port, @NotNull String hostName) {
        notifyDataSetChanged();
        discoveredServerInstances.add(new FileSystemServer(host, port, hostName));
    }

    public Collection<FileSystemServer> getDiscoveredServers() {
        return discoveredServerInstances;
    }

    public void startAnnouncement(int serverPort) {
        if (broadcaster != null) {
            throw new IllegalStateException("DiscoveryBroadcaster already running");
        }

        broadcaster = new DiscoveryBroadcaster(serverPort);
        broadcaster.start();
    }

    public void stopAnnouncement(int serverPort) {
        if (broadcaster == null) {
            return;
        }
        broadcaster.stopDiscoveryThread();
        broadcaster = null;
    }

    public void startListener() {
        if (listener != null) {
            throw new IllegalStateException("DiscoveryListener already running");
        }

        listener = new DiscoveryListener();
        listener.start();
    }

    public void stopListener() {
        if (listener == null) {
            return;
        }
        listener.stopDiscoveryThread();
        listener = null;
    }

    public void deleteOutdatedEntries() {
        for (FileSystemServer server : discoveredServerInstances) {
            if (server.isOutdated()) {
                notifyDataSetChanged();
                discoveredServerInstances.remove(server);
            }
        }
    }

    protected void notifyDataSetChanged() {
        for (DiscoveredServersObserver observer : observers) {
            observer.discoveredServersUpdated();
        }
    }

    public void attachObserver(DiscoveredServersObserver observer) {
        observers.add(observer);
    }

    public void detachObserver(DiscoveredServersObserver observer) {
        observers.remove(observer);
    }
}
