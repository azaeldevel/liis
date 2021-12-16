
package SIIL.panel.notif;

import java.util.Date;

/**
 *
 * @author Azael
 */
public class User 
{
    private int msgID;
    private String sender;
    private String receiver;
    private String status;
    private Date readed;

    /**
     * @return the msgID
     */
    public int getMsgID() {
        return msgID;
    }

    /**
     * @param msgID the msgID to set
     */
    public void setMsgID(int msgID) {
        this.msgID = msgID;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the readed
     */
    public Date getReaded() {
        return readed;
    }

    /**
     * @param readed the readed to set
     */
    public void setReaded(Date readed) {
        this.readed = readed;
    }
    
}
