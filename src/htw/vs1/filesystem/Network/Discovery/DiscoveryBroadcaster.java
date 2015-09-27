package htw.vs1.filesystem.Network.Discovery;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * Created by Felix on 20.09.2015.
 */
public class DiscoveryBroadcaster {

    protected static final long BROADCAST_INTERVAL = 10000; // 10 seconds

    private int serverPort;

    private DatagramSocket socket;

    public DiscoveryBroadcaster(int port) {
        this.serverPort = port;
    }

    public void discovery() throws InterruptedException {
        try {
            sendBroadcastToAll(getDatagramSocket());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        socket.close();
        socket = null;
    }

    protected DatagramSocket getDatagramSocket() throws SocketException {
        if (socket == null) {
            socket = new DatagramSocket();
        }
        return socket;
    }

    private void sendBroadcastToAll(DatagramSocket socket) throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                if (address.getBroadcast() == null) continue;
                sendBroadcast(address.getBroadcast(), socket);
            }

        }
    }

    private void sendBroadcast(InetAddress address, DatagramSocket socket) throws SocketException {
        socket.setBroadcast(true);

        byte[] data = String.valueOf(serverPort).getBytes();

        try {
            DatagramPacket packet = new DatagramPacket(data, data.length, address, DiscoveryManager.DISCOVERY_PORT);
            socket.send(packet);
            //System.out.println("Packet gesendet: " + data.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
