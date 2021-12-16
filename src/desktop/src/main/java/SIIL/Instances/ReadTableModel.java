
package SIIL.Instances;

import SIIL.Server.Database;
import core.FailResultOperationException;
import core.Instance;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class ReadTableModel extends AbstractTableModel
{    
    private final String MYSQL_AVATAR_TABLE = "Instances"; 
    
    private static final int COLUMN_HASH    = 0;
    private static final int COLUMN_IP      = 1;
    private static final int COLUMN_USER    = 2;
    private static final int COLUMN_ESTADO  = 3;
    
    private String[] columnNames = {"Hash", "IP", "Usuaio","Estado"};
    private List<Instance> list;
    
    
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
        Instance inst = list.get(rowIndex);        
        if(rowIndex == -1) return "";
        switch (columnIndex) 
        {
            case COLUMN_HASH:
                return inst.getHasCode();
            case COLUMN_IP:
                return inst.getAddress();
            case COLUMN_USER:
                return inst.getUser();
            case COLUMN_ESTADO:
                return "?";
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
    
    public void load(Database db) throws SQLException 
    {
        String table = MYSQL_AVATAR_TABLE;
        
        String sql = "SELECT id FROM " + table;
        ResultSet rs = db.query(sql);
        list = new ArrayList<>();
        while(rs.next())
        {
            Instance instance = new Instance(rs.getInt(1));
            if(!instance.download(db)) throw new FailResultOperationException("Fallo la descarga de los datos");
            list.add(instance);
        }      
        fireTableDataChanged();
    }    
    

    public Instance getProvider(int selectedRow) 
    {
        return list.get(selectedRow);
    }
}
