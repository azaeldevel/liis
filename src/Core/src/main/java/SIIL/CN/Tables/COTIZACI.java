
package SIIL.CN.Tables;

import SIIL.CN.Engine.Clause;
import SIIL.CN.Engine.Operator;
import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Sucursal;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Azael Reyes
 */
public class COTIZACI extends CN60
{
    /**
     * 
     * @param folio
     * @param tb_cotiza_r
     * @return
     * @throws IOException 
     */
    public SIIL.CN.Records.COTIZACI readWhere(Integer folio) throws IOException
    {
        List<DBFRecord> list = readWhere(new Clause(0,Operator.TRIMEQUAL,String.valueOf(folio)),1,'R');
        if(list.size() == 1)
        {
            SIIL.CN.Records.COTIZACI cotiza = new SIIL.CN.Records.COTIZACI(list.get(0));
            //cotiza.Load(tb_cotiza_r);            
            return cotiza;
        }
        return null;
    }
    
    /**
     * 
     * @param folio
     * @return 
     */
    public boolean existFolio(String folio)
    {
        List<DBFRecord> list = readWhere(new Clause(0,Operator.TRIMEQUAL,folio),1,'R');
        if(list.size() == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 
     * @param sucursal
     * @throws IOException 
     */
    public COTIZACI(Sucursal sucursal) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
