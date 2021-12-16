
package sales.quotation;

import SIIL.Server.Database;
import database.mysql.sales.Quotation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class CRUDTableModel extends AbstractTableModel 
{
    private static final int COL_FOLIO  = 0;
    private static final int COL_CLIENT = 1;
    private static final int COL_NAME   = 2;
    private static final int COL_TOTAL  = 3;
    //private static final int COL_COUNT  = 4;
    
    private ArrayList<String> columnNames;
    private List<Quotation> list;
    private core.Mode mode;
    private String sql;
    
    public void search(Database dbserver,core.Mode mode,String text,int lenght) throws SQLException
    {
        String sql = "SELECT id FROM " + Quotation.MYSQL_AVATAR_TABLE;
        
        
        
        
        sql = sql + " ORDER BY id desc LIMIT " + lenght;
        this.mode = mode;
        
        download(dbserver, sql);
        
    }
    
    public void reload(Database database) throws SQLException
    {
        ResultSet rs = database.query(this.sql);    
        list = new ArrayList<>();
        while(rs.next())
        {
            Quotation q = new Quotation(rs.getInt(1));
            list.add(q);            
        }
        refresh(database);
    }
    
    private void download(Database database, String sql) throws SQLException 
    {
        ResultSet rs = database.query(sql);    
        list = new ArrayList<>();
        while(rs.next())
        {
            Quotation q = new Quotation(rs.getInt(1));
            list.add(q);            
        }
        refresh(database);
    }

    public void refresh(Database database) throws SQLException 
    {
        for(Quotation q : list)
        {
            if(q.download(database).isFlag())
            {
                if(q.downCompany(database))
                {                    
                    if(q.getEnterprise().download(database))
                    {

                    }
                }
            } 
            q.downTotal(database);
        }
        fireTableDataChanged();
    }
    
    public CRUDTableModel()
    {
        columnNames = new ArrayList<>();
        columnNames.add("Folio");
        columnNames.add("Cliente");
        columnNames.add("Nombre");
        columnNames.add("Total");     
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
    
    public Quotation getValueAt(int rowIndex)
    {
        return list.get(rowIndex);
    }

    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COL_FOLIO:
                return list.get(rowIndex).getFolio();
            case COL_CLIENT:
                return list.get(rowIndex).getEnterprise().getNumber();
            case COL_NAME:
                return list.get(rowIndex).getEnterprise().getName();
            case COL_TOTAL:
                return list.get(rowIndex).getTotal();
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
        return columnNames.get(columnIndex);
    }
}
