
package SIIL.CN.Tables;

import SIIL.CN.Sucursal;
import java.io.IOException;

/**
 *
 * @author Azael Reyes
 */
public class RENGLON extends CN60
{
    
    
    public RENGLON(Sucursal sucursal ) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
