package htw.vs1.filesystem.Network.Protocol.Commands;


import htw.vs1.filesystem.FileSystem.exceptions.FileSystemException;
import htw.vs1.filesystem.Network.Protocol.Client.ClientProtocol;
import htw.vs1.filesystem.Network.Protocol.Exceptions.SimpleProtocolUnexpectedServerBehaviour;
import htw.vs1.filesystem.Network.Protocol.Replies.ClientReply;
import htw.vs1.filesystem.Network.Protocol.Replies.Codes.ReplyCode;
import htw.vs1.filesystem.Network.Protocol.Replies.Codes.ReplyCode210;
import htw.vs1.filesystem.Network.Protocol.Replies.Codes.ReplyCode219;
import htw.vs1.filesystem.Network.Protocol.Replies.ServerReply;
import htw.vs1.filesystem.Network.Protocol.Replies.SimpleClientProtocolReply;
import htw.vs1.filesystem.Network.Protocol.Replies.SimpleServerProtocolReply;
import htw.vs1.filesystem.Network.Protocol.Requests.RequestList;
import htw.vs1.filesystem.Network.Protocol.Server.ServerProtocol;

import java.util.Set;

/**
 * Created by markus on 12.06.15.
 */
public class GETFEAT extends AbstractCommand {
    public static String COMMAND_STRING = "GETFEAT";


    public ServerReply execute(ServerProtocol prot, RequestList requestList) {

        Set<String> set = new CommandFactory().getRegisteredCommands();

        StringBuffer buf = new StringBuffer();

        for (String obj : set) {
            buf.append("[" + obj + "]\n");
        }

        new SimpleServerProtocolReply(new ReplyCode210(), this).putReply(prot);

        prot.putLine(buf.toString());

        return new SimpleServerProtocolReply(new ReplyCode219(), this);
    }

    @Override
    public ClientReply invoke(ClientProtocol prot, String... parameters) throws FileSystemException {
        prot.putLine(COMMAND_STRING);
        ReplyCode reply = prot.analyzeReply();


        //Antwort analysieren. Nein IntelliJ ich will hier checken, ob es sich um eine korrekte Excpetion handelt.
        if (reply.getException() != null) {
            throw reply.getException();
        }

        //Start-Zeichen wird nicht empfangen
        if(reply.getCode() != ReplyCode210.CODE) {
            //Irgendetwas stimmt nicht... -> Abbruch
            throw new SimpleProtocolUnexpectedServerBehaviour();
        }

        SimpleClientProtocolReply result = new SimpleClientProtocolReply();
        return LS.getReplyData(prot, result);
    }
}
