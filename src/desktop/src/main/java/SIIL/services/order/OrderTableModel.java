
package SIIL.services.order;

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
public class OrderTableModel extends AbstractTableModel  
{
    public static final String MYSQL_AVATAR_TABLE = "ServicesOrder_Resolved";
    
    /**
     * @return the whereSQL
     */
    public String getWhereSQL() {
        return whereSQL;
    }

    /**
     * @return the tableSQL
     */
    public String getTableSQL() {
        return tableSQL;
    }
    
    public enum Mode
    {
        LISTING,
        SEARCH,
        INITIAL,
        RELOAD
    }
    
    private static final int COLUMN_NUMBER  = 0;
    private static final int COLUMN_MARK    = 1;
    private static final int COLUMN_MODEL   = 2;
    private static final int COLUMN_SERIE   = 3;
    private static final int COLUMN_CLIENT  = 4;
    private static final int COLUMN_HOROS   = 5;
    
    private String[] columnNames = {"Número","Marca","Modelo","Serie","Cliente","Horometro"};
    private List<Order> list;
    private String whereSQL;
    private String tableSQL;
    private Mode modePrevius;
    
    
    
    public Mode getModePrevius()
    {
        return modePrevius;
    }
    
    
    public Boolean search(Database database,Mode mode,String search,int limit) throws SQLException
    {
        modePrevius = mode;        
        String table = MYSQL_AVATAR_TABLE;
        String sql = "SELECT id FROM " + table ;  
        whereSQL = "";
        if(mode  == Mode.INITIAL)
        {
            ;
        }
        else if(mode == Mode.SEARCH)
        {
            whereSQL = whereSQL + " WHERE titemNumber like '%" + search + "%' OR titemMake like '%" + search + "%' OR titemModel like '%" + search + "%' OR titemSerie like '%" + search + "%' OR compNumber = '" + search + "' OR compName like '%" + search + "%' OR sa like '" + search + "%'" ;
        }
        whereSQL = whereSQL + " ORDER BY horometro DESC, fhService DESC";
        if(limit > 0)
        {
            whereSQL = whereSQL + " LIMIT " + limit;
        }
        ResultSet rs;
        list = new ArrayList<>();
        sql = sql + whereSQL;
        tableSQL = table;
        //whereSQL = whereSQL;
        System.out.println(sql);
        rs = database.query(sql);
        Order ord = null;
        while(rs.next())
        {
            ord = new Order(rs.getInt(1));
            ord.downItemFlow(database);
            ord.getItemFlow().downItem(database);
            ord.getItemFlow().getItem().downNumber(database.getConnection());
            ord.getItemFlow().getItem().downMake(database.getConnection());
            ord.getItemFlow().getItem().downModel(database);
            ord.getItemFlow().downSerie(database);
            ord.downCompany(database);
            ord.getCompany().download(database);      
            ord.downHorometro(database);
            list.add(ord);            
        }
        fireTableStructureChanged();
        return true;        
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

    public Order getValueAt(int row)
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
                return list.get(rowIndex).getItemFlow().getItem().getNumber();
            case COLUMN_MARK:
                return list.get(rowIndex).getItemFlow().getItem().getMake();
            case COLUMN_MODEL:
                return list.get(rowIndex).getItemFlow().getItem().getModel();
            case COLUMN_SERIE:
                return list.get(rowIndex).getItemFlow().getSerie();
            case COLUMN_CLIENT:
                return list.get(rowIndex).getCompany().getName();
            case COLUMN_HOROS:
                return list.get(rowIndex).getHorometro();
            default:
                return "-";
        }
    }
}
