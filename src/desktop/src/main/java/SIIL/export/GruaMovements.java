
package SIIL.export;

import SIIL.Server.Database;
import SIIL.services.grua.Battery;
import SIIL.services.grua.Charger;
import SIIL.services.grua.Forklift;
import SIIL.services.grua.Movements;
import SIIL.services.grua.Movitems;
import database.mysql.stock.Titem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Azael
 */
public class GruaMovements 
{
    private List<Movements> ls;
    private Database dbserver;
    private int progress;
    private FileWriter csv;
    private String actual;
    
    public int getProgress()
    {
        return progress;
    }
    public int getSize()
    {
        return ls.size();
    }
    public String getActual()
    {
        return actual;
    }
    
    GruaMovements(Database server,File file) throws SQLException, IOException
    {
        progress = 0;
        dbserver = server;
        ls = new ArrayList<>();        
        Movements.list(dbserver, ls, null, " id desc ", 0);
        
        
        String ext = FilenameUtils.getExtension(file.getAbsolutePath().toString());
        String filename = file.getAbsolutePath();
        if(ext.isEmpty())
        {
            filename = file.getAbsolutePath() + ".csv";
        }
        csv = new FileWriter(filename);
    } 
    
    public void generate() throws SQLException, IOException, Exception
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Movitems> items;        
        Battery battery;
        Charger charger;
        Forklift forklift;
        for(Movements mov : ls)
        {
            forklift = null;
            charger = null;
            battery = null;            
            mov.downloadExport(dbserver);
            System.out.println("Movement : " + mov.getID());
            items = Movitems.select(dbserver,mov);  
            System.out.println("Items count : " + items.size());
            for(Movitems item : items)
            {
                item.download(dbserver);
                actual = item.getNumber();
                switch(Titem.checkType(item.getNumber()))
                {
                    case FORKLIFT:
                    {
                        forklift = new Forklift(-1);
                        forklift.searchForklift(dbserver, item.getNumber());
                        if(!forklift.download(dbserver)) 
                        {
                            System.out.println("No se encontro item ID : " + item.getID());
                            forklift = null;
                        }
                        break;
                    }
                    case BATTERY:
                    {
                        battery = new Battery(-1);
                        battery.search(dbserver, item.getNumber());
                        if(!battery.download(dbserver))
                        {
                            System.out.println("No se encontro item ID : " + item.getID());
                            battery = null;
                        }
                        break;
                    }
                    case CHARGER:
                    {
                        charger = new Charger(-1);
                        charger.search(dbserver, item.getNumber());
                        if(!charger.download(dbserver))
                        {
                            System.out.println("No se encontro item ID : " + item.getID());
                            charger = null;
                        }
                        break;
                    }
                }
            }
            csv.write(Integer.toString(mov.getID()));    
            csv.write(",");
            csv.write(mov.getFolio());        
            csv.write(",");
            csv.write(dateFormat.format(mov.getFhMov()));
            csv.write(",");
            csv.write(mov.getCompany().getName());
            csv.write(",");
            if(forklift != null)
            {
                csv.write(forklift.getNumber());
            }
            else if(battery != null)
            {
                csv.write(battery.getNumber());
            }
            else if(charger != null)
            {
                csv.write(charger.getNumber());
            }
            csv.write(",");
            if(forklift == null)
            {
                if(battery != null)
                {
                    csv.write(battery.getNumber());
                }
            }
            csv.write(",");            
            if(forklift == null)
            {
                if(charger != null)
                {
                    csv.write(charger.getNumber());
                }
            }
            csv.write(",");
            mov.getUso().getLabel();
            csv.write(",");
            mov.getTmov();
            csv.write("\n");
            progress++;
        }
        
        csv.close();
    }
}
