
package core;

import SIIL.Server.Database;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.core.Office;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Azael Reyes
 */
public class QuotedServiceComboBoxModel  extends DefaultComboBoxModel<ServiceQuotation>
{
    public boolean search(Database db,Office office,String text,int limit) throws SQLException
    {        
        while(getSize() > 0)
        {
            removeElementAt(getSize() -1);
        }
        List<ServiceQuotation> ls = ServiceQuotation.searchByFolio(db,office,text,limit);  
        //addElement(new Person(-1000));        
        for(ServiceQuotation p : ls)
        {
            p.downSerie(db.getConnection());
            p.downFolio(db.getConnection());
            addElement(p);
        }
        return true;
    }
}
