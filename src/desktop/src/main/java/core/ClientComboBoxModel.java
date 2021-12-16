
package core;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

/**
 * 
 * @author Azael Reyes
 */
public class ClientComboBoxModel  extends DefaultComboBoxModel<Enterprise>
{
    public boolean search(Database db, String search,Enterprise selected) throws SQLException
    {
        ArrayList<Enterprise> ls = Enterprise.listing(search, 21, db, SIIL.servApp.cred.getBD());
        while(getSize()> 0)
        {
            removeElementAt(getSize() -1);
        }
        addElement(new Enterprise(-1000));
        for(Enterprise p : ls)
        {
            addElement(p);
            if(p.getID() == selected.getID())
            {
                setSelectedItem(p);//seleccionar el ultimo
            }
        }
        return true;
    }
}
