
package SIIL.CN.Tables;

import SIIL.CN.Sucursal;
import java.io.IOException;

/**
 *
 * @author Azael Reyes
 */
public class ORDENES extends CN60
{
    public ORDENES(Sucursal sucursal ) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
