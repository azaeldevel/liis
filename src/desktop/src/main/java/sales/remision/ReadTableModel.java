
package sales.remision;


import SIIL.Server.Database;
import database.mysql.sales.Remision;
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
    public static final String MYSQL_AVATAR_TABLE = "ServicesTrabajo";    
    
    private static final int COLUMN_FOLIO   = 0;
    private static final int COLUMN_CLIENTE = 1;
    private static final int COLUMN_MONTO  = 2;
    private static final int COLUMN_FECHA  = 3;
    
    private List<String> columnNames;
    private List<Remision> list;
    private String whereSQL;
    private String tableSQL;
    private String lastSearch;
    private boolean activeDate;
    
    public ReadTableModel()
    {
        list = new ArrayList<>();
        columnNames = new ArrayList<>();
        columnNames.add("Folio");
        columnNames.add("Cliente");
        columnNames.add("Monto");
        columnNames.add("Fecha"); 
    }
    
    /**
     * @return the whereSQL
     */
    public String getWhereSQL() 
    {
        return whereSQL;
    }

    /**
     * @return the tableSQL
     */
    public String getTableSQL() 
    {
        return tableSQL;
    }

    /**
     * @return the lastSearch
     */
    public String getLastSearch() 
    {
        return lastSearch;
    }

        
    public enum ReadMode
    {
        LOAD,
        REFRESH,
        RELOAD,
        NOREAD,
        SEARCH
    }
    
    
       
    public ReadTableModel(List<Remision> list)
    {
        this.list = list;
    }
    
    

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_FOLIO:
                if(list.get(rowIndex).toString() != null)
                {
                    return list.get(rowIndex).toString();
                }
                return "";
            case COLUMN_CLIENTE:
                if(list.get(rowIndex).getEnterprise() != null) return list.get(rowIndex).getEnterprise().getName();
                return "";
            case COLUMN_MONTO:
                return list.get(rowIndex).getTotal();
            case COLUMN_FECHA:
                return list.get(rowIndex).getFhFolio();
            default:
                return "";
        }
    }    
    
    public List<Remision> getData()
    {
        return list;
    }
    
    public void download(Database database) throws SQLException 
    {
        String sql = "SELECT id FROM " + Remision.MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 200";
        ResultSet rs = database.query(sql);    
        list = new ArrayList<>();
        while(rs.next())
        {
            list.add(new Remision(rs.getInt(1)));            
        }
        
        refresh(database);
    }
    
    public void refresh(Database database) throws SQLException
    {
        for(Remision q : list)
        {
            q.download(database);
            q.downTotal(database);
            if(q.downCompany(database)) q.getEnterprise().download(database);
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
        return columnNames.size();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {        
        if(columnIndex == COLUMN_MONTO) return Double.class;
        return String.class;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames.get(columnIndex);
    }

    public Remision getValueAt(int row)
    {
        return list.get(row);
    }
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }    
}
