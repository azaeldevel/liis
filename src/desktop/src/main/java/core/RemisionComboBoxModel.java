
package core;

import SIIL.Server.Database;
import SIIL.core.Office;
import database.mysql.sales.Remision;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Azael Reyes
 */
public class RemisionComboBoxModel extends DefaultComboBoxModel<Remision> 
{   
    public boolean search(Database db,Office office,String text,int limit) throws SQLException
    {
        while(getSize() > 0)
        {
            removeElementAt(getSize() -1);
        }
        List<Remision> ls = Remision.search(db,office,text,limit);  
        
        for(Remision r : ls)
        {
            r.downSerie(db);
            r.downFolio(db);
            addElement(r);
        }
        
        return true;
    }
}
