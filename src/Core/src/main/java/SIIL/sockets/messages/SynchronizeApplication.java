
package SIIL.sockets.messages;

import core.Instance;

/**
 *
 * @author Azael Reyes
 */
public class SynchronizeApplication extends Application
{
    /**
     * 
     * @param id indica la instacia a syncronizar, si es null se solicita todo.
     */
    public SynchronizeApplication(Instance id) {
        super(id);
    }
    
}
