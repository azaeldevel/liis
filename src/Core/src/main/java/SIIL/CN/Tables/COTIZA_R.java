
package SIIL.CN.Tables;

import SIIL.CN.Sucursal;
import java.io.IOException;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class COTIZA_R extends CN60
{
    public COTIZA_R(Sucursal sucursal,boolean activeDBF,boolean  activeFPT) throws IOException
    {
        Load(getClass(),sucursal,activeDBF,activeFPT);
    }
    public COTIZA_R(Sucursal sucursal) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
