package htw.vs1.filesystem.Network.Protocol.Client;

import htw.vs1.filesystem.FileSystemManger;
import htw.vs1.filesystem.Network.Log.LogType;
import htw.vs1.filesystem.Network.Protocol.Exceptions.SimpleProtocolFatalError;
import htw.vs1.filesystem.Network.Protocol.Exceptions.SimpleProtocolInitializationErrorException;
import htw.vs1.filesystem.Network.Protocol.Replies.Codes.ReplyCode;
import htw.vs1.filesystem.Network.Protocol.Replies.ReplyAnalyzer;
import htw.vs1.filesystem.Network.Protocol.SimpleProtocol;

import java.net.Socket;

/**
 * Created by markus on 24.06.15.
 */
public class SimpleClientProtocol extends SimpleProtocol implements ClientProtocol {

    private ReplyAnalyzer analyzer;

    /**
     * Creates a new SimpleProtocol instance.
     *
     * @param socket Reference to a ServerSocket
     * @throws SimpleProtocolInitializationErrorException
     */
    public SimpleClientProtocol(Socket socket) throws SimpleProtocolInitializationErrorException {
        super(socket);
        this.analyzer = new ReplyAnalyzer();
    }

    public ReplyCode analyzeReply() throws SimpleProtocolFatalError {
        return analyzer.parseServerReply(this);
    }

    @Override
    public void putLine(String line) {
        FileSystemManger.getInstance().putNetworkLog(
                line,
                LogType.SERVER_LOG
        );

        super.putLine(line);
    }

    @Override
    public void readLine() throws SimpleProtocolFatalError {
        super.readLine();

        FileSystemManger.getInstance().putNetworkLog(
                getCurrentLine(),
                LogType.CLIENT_LOG
        );
    }
}
