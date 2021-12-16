
package SIIL.CN.Records;

import SIIL.CN.Engine.DBFRecord;

/**
 * 
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class COTIZA_R extends DBFRecord
{
    public short getCantidad()
    {
        return (short) Double.parseDouble(super.getString(2).trim());
    }

    public String getNumber()
    {
        return super.getString(1).trim();
    }
    
    public String getComent()
    {
        return super.getString(12).trim();
    }
    
    public COTIZA_R(DBFRecord record)
    {
        super(record);
    }
}
