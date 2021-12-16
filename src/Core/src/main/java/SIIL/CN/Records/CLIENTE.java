
package SIIL.CN.Records;

import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Engine.Table;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class CLIENTE extends DBFRecord
{    
    public CLIENTE(Table table, int offset)
    {
        super(table,offset);
    }
    
    public CLIENTE(DBFRecord record) 
    {
        super(record);
    }

    public String getNUM_CLI()
    {
        return super.getString(0).trim();
    }
    
    public String getNOM_CLI()
    {
        return super.getString(1).trim();
    }
    
    public String getGUARDIANEM()
    {
        return super.getString(82).trim();
    }
    
    public String getRFC()
    {
        return super.getString(5).replaceAll("\\s+","");
    }
}
