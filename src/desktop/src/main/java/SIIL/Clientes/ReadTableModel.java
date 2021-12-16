
package SIIL.Clientes;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
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
    public static final String MYSQL_AVATAR_TABLE = "companies";
    
    private static final int COLUMN_NUMBER  = 0;
    private static final int COLUMN_NAME    = 1;
    private static final int COLUMN_RFC     = 2;
    
    private List<String> columnNames;
    private List<Enterprise> list;
    private String whereSQL;
    private String tableSQL;

    public ReadTableModel()
    {
        columnNames = new ArrayList<>();
        columnNames.add("Número");
        columnNames.add("Nombre");
        columnNames.add("RFC");
    }
    
    private void reload(Database database) throws SQLException 
    {       
        for(Enterprise empresa : list) 
        {
            empresa.download(database);
        }
        fireTableStructureChanged();
    }
    
    public void search(Database database,String search,int limit,Filter filter) throws SQLException
    {
        String table = MYSQL_AVATAR_TABLE;
        String sql = "SELECT id FROM " + table ;  
        whereSQL = " WHERE ";
        
        if(filter == Filter.SAERCH)
        {
            whereSQL = whereSQL + " number like '%" + search + "%' OR name like '%" + search + "%' OR rfc like '%" + search + "'" ;
        }
        else if(filter == Filter.RFC)
        {
            whereSQL = whereSQL + " rfc = '" + search + "'" ;
        }
        if(limit > 0)
        {
            whereSQL = whereSQL + " ORDER BY number DESC LIMIT " + limit;
        }
        else
        {
            whereSQL = whereSQL + " ORDER BY number DESC";
        }
        
        sql = sql + whereSQL;
        System.out.println(sql);
        download(database, sql);        
    }
    
    private void download(Database database, String sql) throws SQLException 
    {        
        list = new ArrayList<>();
        ResultSet rs;
        rs = database.query(sql);
        while(rs.next())
        {
            Enterprise enterprise = new Enterprise(rs.getInt(1));
            if(enterprise.download(database))
            {                
                list.add(enterprise);
            }
        }
        fireTableStructureChanged();
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
        return String.class;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames.get(columnIndex);
    }

    public Enterprise getValueAt(int row)
    {
        return list.get(row);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_NUMBER:
                if(list.get(rowIndex).getNumber() != null) return "Sin Número";
                return list.get(rowIndex).getNumber();
            case COLUMN_NAME:
                return list.get(rowIndex).getName();
            case COLUMN_RFC:
                return list.get(rowIndex).getRFC();
            default:
                return "-";
        }
    }
}
