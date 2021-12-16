
package SIIL.services.order;

import java.io.File;

/**
 *
 * @author Azael Reyes
 */
public class Association<T> 
{
    private File file;
    private boolean actionable;
    private T type;

    public Association(File file,boolean process,T type)
    {
        this.file = file;
        this.actionable = process;
        this.type = type;
    }
        
        /**
         * @return the file
         */
    public File getFile() 
    {
        return file;
    }

    /**
    * @return the process
    */
    public boolean isActionable() 
    {
       return actionable;
    }

    /**
     * @return the order
     */
    public T getObject() {
            return type;
    }    
}
