
package stock;

import SIIL.Server.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class AlmacenTableModel  extends AbstractTableModel
{
    private static final int COLUMN_NUMBER  = 0;
    private static final int COLUMN_DESCRIPCION = 1;
    private static final int COLUMN_LOCACION    = 2;        
    private List<Allocated> list;
    private final String[] columnNames = {"No. Parte","Locación","Descripción"};
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) 
    {
        if(columnIndex == COLUMN_LOCACION) return true;
        return false;
    }
    
    public void download(Database database,Container container) throws SQLException
    {
        String sql = "SELECT id FROM " + Allocated.MYSQL_AVATAR_TABLE + " WHERE container = " + container.getID();
        ResultSet rs = database.query(sql);
        list = new ArrayList<>();
        while(rs.next())
        {
            Allocated allocated = new Allocated(rs.getInt(1));
            allocated.download(database);
            list.add(allocated);
        }
        
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() 
    {
        if(list != null)
        {
            return list.size();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.length;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        return String.class;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames[columnIndex];
    }

    public Allocated getValueAt(int row)
    {
        return list.get(row);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        if(list.size() == 0) return "";
        switch(columnIndex)
        {
            case COLUMN_NUMBER:
                return list.get(rowIndex).getFlow().getItem().getNumber();
            case COLUMN_LOCACION:
                return list.get(rowIndex).getContainer().getFullName();
            case COLUMN_DESCRIPCION:
                return list.get(rowIndex).getFlow().getItem().getDescription();
            default:
                return "";
        }
    }
    
}
