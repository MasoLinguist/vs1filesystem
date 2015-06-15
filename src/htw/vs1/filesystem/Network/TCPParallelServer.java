package htw.vs1.filesystem.Network;

import com.sun.corba.se.spi.activation.Server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by markus on 05.06.15.
 */
public class TCPParallelServer implements ServerInterface {

    private static final int STANDARD_PORT = 4711;
    private static final int STANDARD_TIMEOUT = 10000;

    private int port = STANDARD_PORT;
    private int timeout = STANDARD_TIMEOUT;


    public static void main(String[] args) {
        TCPParallelServer server = new TCPParallelServer(4322);
        server.run();

    }



    public TCPParallelServer() {
    }

    public TCPParallelServer(int port) {
        this.port = port;
    }


    public void run() {

        try
        {
            // Erzeugen der Socket/binden an Port/Wartestellung
            ServerSocket socket = new ServerSocket(port);

            while (true)
            {
                System.out.println("Warten auf Verbindungen ...");
                Socket client = socket.accept();


                (new TCPParallelWorker(client)).start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }






}