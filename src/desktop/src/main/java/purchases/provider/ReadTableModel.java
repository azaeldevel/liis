
package purchases.provider;

import database.mysql.purchases.Provider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class ReadTableModel extends AbstractTableModel
{    
    private final String MYSQL_AVATAR_TABLE = "PurchasesProvider"; 
    
    private static final int COLUMN_NUMBER   = 0;
    private static final int COLUMN_RAZON  = 1;
    private static final int COLUMN_RFC   = 2;
    
    private String[] columnNames = {"Número", "Razon Social", "RFC"};
    private List<Provider> list;
    //private session.Credential credential;
    
    /*public ReadTableModel(session.Credential credential)
    {
        this.credential = credential;
    }*/
    
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
        Provider prov = list.get(rowIndex);        
        if(rowIndex == -1) return "";
        switch (columnIndex) 
        {
            case COLUMN_NUMBER:
                return prov.getNumber();
            case COLUMN_RAZON:
                return prov.getNameRazonSocial();
            case COLUMN_RFC:
                return prov.getRFC();
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
    
    void search(Connection connection,String where, String order, int limit,int offset) throws SQLException 
    {
        list = Provider.search(connection, where, order, limit, offset);
    }    
    
    void search(Connection connection,String search) throws SQLException
    {
        list = Provider.search(connection, search);
    }

    Provider getProvider(int selectedRow) 
    {
        return list.get(selectedRow);
    }
}
