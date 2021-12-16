
package sales.clients;

import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class ReadTableModel  extends AbstractTableModel
{
    private final String MYSQL_AVATAR_TABLE = "Companies";    
    private static final int COLUMN_NUMBER   = 0;
    private static final int COLUMN_RAZON  = 1;
    private static final int COLUMN_RFC   = 2;
    
    private String[] columnNames = {"Número", "Razon Social", "RFC"};
    private List<Enterprise> list;
    
    public enum Mode
    {
        DISPLAY,
        SELECT
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
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        Company comp = list.get(rowIndex);        
        //if(rowIndex == -1) return "";
        switch (columnIndex) 
        {
            case COLUMN_NUMBER:
                return comp.getNumber();
            case COLUMN_RAZON:
                return comp.getName();
            case COLUMN_RFC:
                return "#";
            default:
                return null;
        }
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
    
    public void search(Database connection,String where, String order, int limit,int offset) throws SQLException 
    {
        list = Company.search(where, limit, connection, "bc.tj");
    }
    
    void search(Database connection,String search) throws SQLException
    {
        list = Company.search(search, 10, connection, "bc.tj");
    }

    public Company getValueAt(int selectedRow) 
    {
        return list.get(selectedRow);
    }    
}
