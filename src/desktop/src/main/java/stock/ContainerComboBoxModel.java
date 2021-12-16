
package stock;

import SIIL.Server.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;


/**
 * 
 * @author Azael Reyes
 */
public class ContainerComboBoxModel  extends DefaultComboBoxModel<Container>
{
    public boolean fill(Database db) throws SQLException
    {
        ArrayList<Container> ls = Container.listing(db);
        while(getSize()> 0)
        {
            removeElementAt(getSize()-1);
        }
        addElement(new Container(-1000));
        for(Container container : ls)
        {
            addElement(container);
        }
        return true;
    }
}
