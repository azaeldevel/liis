
package SIIL.sockets.messages;

import core.Instance;
import java.io.Serializable;

/**
 *
 * @author Azael
 */
public class Message implements Serializable
{
    private Instance socketID;
    
    public Message(Instance id)
    {
        socketID = id;
    }

    /**
     * @return the socketID
     */
    public Instance getSocketID() 
    {
        return socketID;
    }
}
