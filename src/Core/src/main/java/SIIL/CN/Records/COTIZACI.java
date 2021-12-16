
package SIIL.CN.Records;

import SIIL.CN.Engine.Clause;
import SIIL.CN.Engine.Operator;
import SIIL.CN.Engine.DBFRecord;
import java.util.ArrayList;
import java.util.List;
import process.Moneda;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class COTIZACI extends DBFRecord
{
    List<SIIL.CN.Records.COTIZA_R> renglones;
    
    public String getClientNumber()
    {
        return super.getString(4).trim();
    }
    public Moneda getMoneda()
    {
        String mon = super.getString(5).trim();
        if(mon.equals("D"))
        {
            return Moneda.USD;
        }
        else if(mon.equals("P"))
        {
            return Moneda.MXN;
        }
        else
        {
            return null;
        }
    }
    
    public String getFolio()
    {
        return super.getString(0).trim();
    }
    
    public double getTotal()
    {
        double t = Double.parseDouble(super.getString(7).trim());
        return t;
    }
    
    public List<SIIL.CN.Records.COTIZA_R> readWhere(SIIL.CN.Tables.COTIZA_R tbcotiza_r)
    {
        List<DBFRecord> recRenglones = tbcotiza_r.readWhere(new Clause(0,Operator.TRIMEQUAL,String.valueOf(getFolio())),0,'R');
        if(recRenglones.size() > 0)
        {//termina la operacion
            renglones = new ArrayList<>();
            for(DBFRecord rec : recRenglones)
            {
                renglones.add(new SIIL.CN.Records.COTIZA_R(rec));
            }
            return renglones;
        }         
        return null;
    }
    
    public COTIZACI(DBFRecord record)
    {
        super(record);
    }

}
