
package core;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Azael Reyes
 */
public class CSVFileFilter extends FileFilter 
{
    @Override
    public boolean accept(File f) 
    {
        if(f.getAbsolutePath().endsWith(".csv")) 
        {
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() 
    {
        return "Solo archivos con extención 'CSV'";
    }  
}
