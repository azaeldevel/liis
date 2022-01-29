
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

/**
 *
 * @author Azael
 */
public class GruaMovements 
{
    
    public void generate(File file,Database dbserver) throws SQLException, IOException, Exception
    {
        List<Movements> ls = new ArrayList<>();        
        Movements.list(dbserver, ls, null, " id desc ", 0);         
        FileWriter csv = new FileWriter(file.getAbsolutePath());
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
                else if(charger != null)
                {
                    csv.write(charger.getNumber());
                }
            }
            csv.write(",");
            mov.getUso().getLabel();
            csv.write(",");
            mov.getTmov();
            csv.write("\n");
            //System.out.println("id = " + mov.getID());
        }
        
        csv.close();
    }
}
