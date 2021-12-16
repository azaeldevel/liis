
package stock;

import SIIL.Server.Database;
import database.mysql.stock.Item;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Azael Reyes
 */
public class NumberComboModel<E> extends AbstractListModel<E> implements ComboBoxModel<E>
{
    private ArrayList<E> list;
    private E selection;
    
    public void search(Database conDatabase, String search) throws SQLException
    {
        list = (ArrayList<E>) database.mysql.stock.Item.search(conDatabase, search);
        for(E element : list)
        {
            ((Item)element).downNumber(conDatabase.getConnection());
        }
    }
    
    public NumberComboModel() 
    {
        list = new ArrayList<>();
    }
    
    @Override
    public int getSize() 
    {
        return list.size();
    }

    @Override
    public E getElementAt(int index) 
    {
        return list.get(index);
    }

    @Override
    public void setSelectedItem(Object obj) 
    {
        selection = (E) obj;
    }

    @Override
    public Object getSelectedItem() 
    {
        return selection;
    }
}
