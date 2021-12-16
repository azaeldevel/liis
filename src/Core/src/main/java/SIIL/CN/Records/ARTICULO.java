
package SIIL.CN.Records;

import SIIL.CN.Engine.DBFRecord;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class ARTICULO extends DBFRecord
{     
    public String getReciben()
    {
        return super.getString(11).trim();
    }
    
    public String getUnidad()
    {
        return super.getString(13).trim();
    }
    
    public String getDescription()
    {
        return super.getString(1).trim();
    }
    
    public String getNumber()
    {
        return super.getString(0).trim();
    }
    
    public ARTICULO(DBFRecord record)
    {
        super(record);
    }
}
