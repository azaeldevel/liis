
package SIIL.CN.Tables;

import SIIL.CN.Sucursal;
import java.io.IOException;

/**
 * @version 0.1
 * @author Azael Reyes
 */
public class PROVEEDO extends CN60
{
    public PROVEEDO(Sucursal sucursal ) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
