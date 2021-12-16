
package SIIL.CN.Tables;

import SIIL.CN.Sucursal;
import java.io.IOException;

/**
 *
 * @author Azael Reyes
 */
public class REN_COMP extends CN60
{
    public REN_COMP(Sucursal sucursal,boolean activeDBF,boolean  activeFPT) throws IOException
    {
        Load(getClass(),sucursal,activeDBF,activeFPT);
    }
    
    public REN_COMP(Sucursal sucursal) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
