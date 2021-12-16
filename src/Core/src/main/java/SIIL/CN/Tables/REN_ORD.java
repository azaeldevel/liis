
package SIIL.CN.Tables;

import SIIL.CN.Sucursal;
import java.io.IOException;

/**
 *
 * @author Azael Reyes
 */
public class REN_ORD extends CN60
{
    public REN_ORD(Sucursal sucursal ) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
