
package SIIL.CN.Tables;

import SIIL.CN.Sucursal;
import java.io.IOException;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class CLIENTE extends CN60
{
    /**
     * 
     * @param sucursal
     * @throws IOException 
     */
    public CLIENTE(Sucursal sucursal) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
