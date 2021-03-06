package htw.vs1.filesystem.Network.Protocol.Replies.Codes;

import htw.vs1.filesystem.Network.Protocol.Replies.Type.SimpleProtocolReplyType;
import htw.vs1.filesystem.Network.Protocol.Replies.Type.Type;

/**
 * Created by Hendrik on 17.06.2015.
 */
public class ReplyCode220 extends ReplyCode {


    public static final Type REPLY_TYPE = SimpleProtocolReplyType.CONFIRMATION;
    public static final int CODE = 220;
    public static final String DESCRIPTION = "";
    public static final String STANDARD_MESSAGE1 = "USER";
    public static final String STANDARD_MESSAGE2 = "LOGGED IN";

    private String user;

    public ReplyCode220() {
        this.message = "";
    }

    public ReplyCode220(String USER) {
        this.message = STANDARD_MESSAGE1 + " " + USER + " " + STANDARD_MESSAGE2;
    }

    @Override
    public void setReplyString(String reply) {
        super.setReplyString(reply);

        this.message = additionalMessage;

        user = message.substring(
                STANDARD_MESSAGE1.length(),
                message.length() - STANDARD_MESSAGE2.length()
        );
    }

    @Override
    public int getCode() { return CODE; }

    @Override
    public String getDescription() { return DESCRIPTION; }

    @Override
    public Type getReplyType() { return REPLY_TYPE; }
}
