
package core;

import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.core.Office;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Azael Reyes
 */
public class OwnresComboBoxModel extends DefaultComboBoxModel<Person>
{
    public void select(Person person)
    {
        setSelectedItem(person);
    }
    
    public boolean fill(Database db,Office office) throws SQLException
    {
        List<Person> ls = Person.listing(db, Person.Type.MANAGER,office,Person.OrderBy.N1AP);  
        addElement(new Person(-1000));
        for(Person p : ls)
        {
            addElement(p);
        }
        return true;
    }
    
    public boolean search(Database db,Office office,int limit,String[] text) throws SQLException
    {
        while(getSize() > 0)
        {
            removeElementAt(getSize() - 1);
        }
        List<Person> ls = Person.search(db, Person.Type.MANAGER,office,Person.OrderBy.N1AP,limit,text);  
        //addElement(new Person(-1000));
        for(Person p : ls)
        {
            addElement(p);
        }
        return true;
    }
    
    public boolean search(Database db,Office office,int limit,String text) throws SQLException
    {
        while(getSize() > 0)
        {
            removeElementAt(getSize() -1);
        }
        List<Person> ls = Person.search(db, Person.Type.MANAGER,office,Person.OrderBy.N1AP,limit,text);  
        //addElement(new Person(-1000));
        for(Person p : ls)
        {
            addElement(p);
        }
        return true;
    }
}
