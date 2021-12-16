
package core;

import SIIL.Server.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import process.Module;
import process.State;

/**
 *
 * @author Azael Reyes
 */
public class StatesComboBoxModel extends DefaultComboBoxModel<State>  
{
    public void search(Database db,Module module) throws SQLException
    {
        ArrayList<State> list = State.selectAll(db, module);
        addElement(new State(-1000));
        for(State s : list)
        {
            s.download(db);
            addElement(s);
        }
    }
}
