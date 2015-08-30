package htw.vs1.filesystem.Network.Protocol.Replies.Codes;

import htw.vs1.filesystem.Network.Protocol.Replies.Type.Type;

/**
 * Created by markus on 16.06.15.
 */
public abstract class ReplyCode {

    protected String message;

    /**
     * Returns a textual, human-readable explanation of the issued reply
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Reply code, of the reply as per our convention.
     * @return
     */
    public abstract int getCode();

    /**
     * Get a human-readble of the Reply for debugging purposes and educational use.
     * @return
     */
    public abstract String getDescription();

    /**
     * Get the type of reply as per our convention.
     * @return
     */
    public abstract Type getReplyType();

    public void setReplyString(String reply) {
        this.message = reply.substring(4);
    }

    @Override
    public String toString() {
        return getCode() + " " + getMessage();
    }
}

