
package SIIL.sockets.messages;

import core.Instance;

/**
 *
 * @author Azael Reyes
 */
public class BatMessage extends Message
{    
    private String message;
    
    public BatMessage(Instance id, String message) 
    {
        super(id);
        this.message = message;
    }    

    /**
     * @return the cause
     */
    public String getMessage() {
        return message;
    }
}
