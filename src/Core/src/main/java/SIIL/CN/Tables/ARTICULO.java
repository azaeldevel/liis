
package SIIL.CN.Tables;

import SIIL.CN.Engine.Clause;
import SIIL.CN.Engine.Operator;
import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Sucursal;
import java.io.IOException;
import java.util.List;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class ARTICULO extends CN60
{    
    public SIIL.CN.Records.ARTICULO readWhere(String number) throws IOException
    {
        List<DBFRecord> list = readWhere(new Clause(0,Operator.TRIMEQUAL,number),1,'R');
        if(list.size() == 1)
        {
            SIIL.CN.Records.ARTICULO articulo = new SIIL.CN.Records.ARTICULO(list.get(0));
            return articulo;
        }
        return null;
    }
    
    public ARTICULO(Sucursal sucursal ) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
