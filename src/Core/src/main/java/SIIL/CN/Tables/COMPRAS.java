
package SIIL.CN.Tables;

import SIIL.CN.Sucursal;
import java.io.IOException;

/**
 *
 * @author Azael Reyes
 */
public class COMPRAS  extends CN60
{
    public COMPRAS(Sucursal sucursal,boolean activeDBF,boolean  activeFPT) throws IOException
    {
        Load(getClass(),sucursal,activeDBF,activeFPT);
    }
    public COMPRAS(Sucursal sucursal) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
