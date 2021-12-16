
package SIIL.CN.Tables;

import SIIL.CN.Engine.Clause;
import SIIL.CN.Engine.Operator;
import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Sucursal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Azael Reyes
 */
public class FACTURA  extends CN60
{
    enum Document
    {
        VENTA,
        REMISION        
    }
    
    /*public SIIL.CN.Records.FACTURA readWhere(String folio) throws IOException
    {
        List<Clause> wheres = new ArrayList<Clause>();
        wheres.add(new Clause(0,Operator.TRIMEQUAL,String.valueOf(folio)));
        wheres.add(new Clause(0,Operator.TRIMEQUAL,null));
        List<DBFRecord> list = super.readWhere(new Clause(0,Operator.AND,wheres),1,'R');
        if(list.size() > 0)
        {
            SIIL.CN.Records.FACTURA remision = new SIIL.CN.Records.FACTURA(list.get(0));
            return remision;
        }
        return null;
    }*/
        
    public SIIL.CN.Records.FACTURA readWhere(String folio) throws IOException
    {
        List<Clause> wheres = new ArrayList<Clause>();
        wheres.add(new Clause(0,Operator.TRIMEQUAL,String.valueOf(folio)));
        wheres.add(new Clause(0,Operator.TRIMEQUAL,null));
        List<DBFRecord> list = super.readWhere(new Clause(0,Operator.AND,wheres),1,'R');
        if(list.size() > 0)
        {
            SIIL.CN.Records.FACTURA remision = new SIIL.CN.Records.FACTURA(list.get(0));
            return remision;
        }
        return null;
    }
    
    public FACTURA(Sucursal sucursal) throws IOException
    {
        Load(getClass(),sucursal);
    }
}
