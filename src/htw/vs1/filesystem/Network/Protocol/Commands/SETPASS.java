package htw.vs1.filesystem.Network.Protocol.Commands;

import htw.vs1.filesystem.Network.Protocol.Protocol;
import htw.vs1.filesystem.Network.Protocol.Replies.Reply;
import htw.vs1.filesystem.Network.Protocol.Requests.RequestList;

/**
 * Created by markus on 12.06.15.
 */
public class SETPASS extends AbstractCommand {
    public static final String COMMAND_STRING = "SETPASS";



    public Reply execute(Protocol prot, RequestList requestList) {


        if (requestList.getPreviousElement().getCommandString().equals(SETUSER.COMMAND_STRING) && requestList.getPreviousElement().hasArguments()) {
            if (requestList.getCurrentElement().hasArguments()) {

            } else {
                prot.putLine("4xx No Arguments supplied");
            }
        } else {
            prot.putLine("4xx SETPASS must be preceeded by valid SETUSER.");
        }

        return null;
    }
}
