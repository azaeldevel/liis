
package core;

import SIIL.Server.Database;
import SIIL.services.grua.Uso;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Azael Reyes
 */
public class UsoComboBoxModel extends DefaultComboBoxModel<Uso> 
{
    public void fill(Database db) throws SQLException
    {
        addElement(new Uso(-1000));
        List<Uso> ls = Uso.listing(db);
        for(Uso u: ls)
        {
            addElement(u);
            u.download(db.getConnection());
        }
    }
    
}
