
package SIIL.COMPAQ;

import SIIL.Server.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author areyes
 */
public class ClientsTableModel extends AbstractTableModel 
{
    private static final int COLUMN_NUMBER  = 0;
    private static final int COLUMN_NAME    = 1;
    private ArrayList<String> columnNames;
    private ArrayList<Client> list;
    
    
    public boolean search(Database db, String text, int limit) throws SQLException
    {
        list = Client.search(db, text, limit);        
        if(list.isEmpty()) return false;
        fireTableDataChanged();
        return true;
    }
    
    public void set(ArrayList<Client> lis)
    {
        this.list = list;
        fireTableDataChanged();
    }
    
    public ClientsTableModel()
    {
        columnNames = new ArrayList<>();
        columnNames.add("Número");
        columnNames.add("Nombre");        
    }
    
    @Override
    public int getRowCount() 
    {
        if(list != null) return list.size();
        return 0;
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.size();
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        return String.class;
    }
    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames.get(columnIndex);
    }
    
    public Client get(int r)
    {
        return list.get(r);
    }
    
    @Override
    public Object getValueAt(int r, int c) 
    {
        switch(c)
        {
            case COLUMN_NUMBER:
                if(list.get(r).getNumber() == null) return "Sin Número";
                    return list.get(r).getNumber();
            case COLUMN_NAME:
                return list.get(r).getName();
            default:
                throw new ArrayIndexOutOfBoundsException ("La columna numero " + c + " esta fuera del modelo de la tabla.");
        }
    }
    
}
