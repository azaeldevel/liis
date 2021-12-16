
package core;

import SIIL.Server.Database;
import SIIL.core.Office;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Azael Reyes
 */
public class OfficeComboBoxModel  extends DefaultComboBoxModel<Office>
{
    public void fill(Database db) throws SQLException
    {
        addElement(new Office(-1000));
        List<Office> ls = Office.listing(db);
        for(Office o: ls)
        {
            addElement(o);
            Throwable download = o.download(db.getConnection());
        }
    }
    
}
