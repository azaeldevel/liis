
package SIIL.export;

import SIIL.Server.Database;
import SIIL.services.grua.Movements;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Azael
 */
public class GruaMovements 
{
    
    public void generate(File file,Database dbserver) throws SQLException, IOException
    {
        List<Movements> ls = new ArrayList<>();        
        Movements.list(dbserver, ls, null, " id desc ", 0);         
        FileWriter csv = new FileWriter(file.getAbsolutePath());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        for(Movements mov : ls)
        {
            mov.downloadExport(dbserver);
            csv.write(Integer.toString(mov.getID()));
            csv.write(",");
            csv.write(dateFormat.format(mov.getFhMov()));
            csv.write("\n");
            //System.out.println("id = " + mov.getID());
        }
        csv.close();
    }
}
