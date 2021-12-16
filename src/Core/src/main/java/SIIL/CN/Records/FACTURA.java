
package SIIL.CN.Records;

import SIIL.CN.Engine.DBFRecord;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Azael Reyes
 */
public class FACTURA  extends DBFRecord
{
    
    public String getClientNumber()
    {
        return super.getString(7).trim();
    }
    
    public Date getFecha() throws ParseException
    {
        String strDate = super.getString(4);
        DateFormat df = new SimpleDateFormat("yyyyMMdd"); 
        Date date = df.parse(strDate);
        return date;
    }
    
    public FACTURA(DBFRecord record)
    {
        super(record);
    }
}
