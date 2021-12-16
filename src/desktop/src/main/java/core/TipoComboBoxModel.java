
package core;

import SIIL.Server.Database;
import SIIL.services.grua.Tipo;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Azael Reyes
 */
public class TipoComboBoxModel extends DefaultComboBoxModel<Tipo> 
{
    public void fill(Database db) throws SQLException
    {
        addElement(new Tipo(-1000));
        List<Tipo> ls = Tipo.listing(db);
        for(Tipo t: ls)
        {
            addElement(t);
            t.download(db.getConnection());
        }
    }
}
