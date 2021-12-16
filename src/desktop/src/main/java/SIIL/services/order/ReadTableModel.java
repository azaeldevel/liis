
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
public class ReadTableModel extends AbstractTableModel  
{
    public static final String MYSQL_AVATAR_TABLE = Resumen.MYSQL_AVATAR_TABLE;
    
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
        INITIAL
    }
    
    private static final int COLUMN_NUMBER  = 0;
    private static final int COLUMN_MARK    = 1;
    private static final int COLUMN_MODEL   = 2;
    private static final int COLUMN_SERIE   = 3;
    private static final int COLUMN_CLIENT  = 4;
    private static final int COLUMN_HOROS   = 5;
    
    private String[] columnNames = {"Número","Marca","Modelo","Serie","Cliente","Horometro"};
    private List<Resumen> list;
    private String whereSQL;
    private String tableSQL;

    public void load(Database database,int limit) throws SQLException
    {
        String table = MYSQL_AVATAR_TABLE;
        String sql = "SELECT id FROM " + table ;  
        whereSQL = "";
        
        if(limit > 0)
        {
            whereSQL = whereSQL + " ORDER BY id DESC LIMIT " + limit;
        }
        else
        {
            whereSQL = whereSQL + " ORDER BY id DESC";
        }
        sql = sql + whereSQL;
        System.out.println(sql);
        download(database, sql);        
    }
    
    public void search(Database database,String search,int limit) throws SQLException
    {
        String table = MYSQL_AVATAR_TABLE + "_Resolved";
        String sql = "SELECT id FROM " + table ;  
        whereSQL = "";
        
        if(search != null)
        {
            whereSQL = whereSQL + " titemNumber like '%" + search + "%' OR titemMake like '%" + search + "%' OR titemModel like '%" + search + "%' OR titemSerie like '%" + search + "%' OR compNumber = " + search + " OR compName like '%" + search + "%'" ;
        }
        if(limit > 0)
        {
            whereSQL = whereSQL + " ORDER BY id DESC LIMIT " + limit;
        }
        else
        {
            whereSQL = whereSQL + " ORDER BY id DESC";
        }
        ResultSet rs;
        list = new ArrayList<>();
        sql = sql + whereSQL;
        tableSQL = table;
        whereSQL = whereSQL;
        //System.out.println(sql);
        download(database, sql);        
    }

    private void download(Database database, String sql) throws SQLException 
    {        
        list = new ArrayList<>();
        ResultSet rs;
        rs = database.query(sql);
        while(rs.next())
        {
            Resumen resum = new Resumen(rs.getInt(1));
            if(resum.downFlow(database))
            {
                resum.getFlow().downItem(database);
                resum.getFlow().getItem().downNumber(database.getConnection());
                resum.getFlow().getItem().downMake(database.getConnection());
                resum.getFlow().getItem().downModel(database);
                resum.getFlow().downSerie(database);
                resum.downOrder(database);
                resum.getOrder().downCompany(database);
                resum.getOrder().getCompany().download(database);      
                resum.getOrder().downHorometro(database);
                list.add(resum);
            }
        }
        fireTableStructureChanged();
    }
    
    public Boolean search(Database database,Mode mode,Module module,String search,int limit) throws SQLException
    {
        String table = null;
        if(search != null)
        {
            table = MYSQL_AVATAR_TABLE + "_Resolved";
        }
        else
        {
            table = MYSQL_AVATAR_TABLE;
        }
        String sql = "SELECT id FROM " + table ;  
        whereSQL = "";
        
        if(search != null || module != null) // si hay algun patron de busqueda activo.
        {
            whereSQL = whereSQL + " WHERE ";
        }
        if(module != null && search == null )//la clausula de modulo se desctiva durante la busqueda rapida.
        {
            whereSQL = whereSQL + " module = " + module.getID();
        }
        if(search != null)
        {
            whereSQL = whereSQL + " titemNumber like '%" + search + "%' OR titemMake like '%" + search + "%' OR titemModel like '%" + search + "%' OR titemSerie like '%" + search + "%' OR compNumber = '" + search + "' OR compName like '%" + search + "%'" ;
        }
        if(limit > 0)
        {
            whereSQL = whereSQL + " ORDER BY id DESC LIMIT " + limit;
        }
        else
        {
            whereSQL = whereSQL + " ORDER BY id DESC";
        }
        ResultSet rs;
        list = new ArrayList<>();
        sql = sql + whereSQL;
        tableSQL = table;
        whereSQL = whereSQL;
        System.out.println(sql);
        rs = database.query(sql);
        while(rs.next())
        {
            Resumen resum = new Resumen(rs.getInt(1));
            if(resum.downFlow(database))
            {
                resum.getFlow().downItem(database);
                resum.getFlow().getItem().downNumber(database.getConnection());
                resum.getFlow().getItem().downMake(database.getConnection());
                resum.getFlow().getItem().downModel(database);
                resum.getFlow().downSerie(database);
                resum.downOrder(database);
                resum.getOrder().downCompany(database);
                if(resum.getOrder().getCompany() != null) resum.getOrder().getCompany().download(database);      
                resum.getOrder().downHorometro(database);
                list.add(resum);
            }
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

    public Resumen getValueAt(int row)
    {
        return list.get(row);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_NUMBER:
                return list.get(rowIndex).getFlow().getItem().getNumber();
            case COLUMN_MARK:
                return list.get(rowIndex).getFlow().getItem().getMake();
            case COLUMN_MODEL:
                return list.get(rowIndex).getFlow().getItem().getModel();
            case COLUMN_SERIE:
                return list.get(rowIndex).getFlow().getSerie();
            case COLUMN_CLIENT:
                if(list.get(rowIndex).getOrder().getCompany() != null)
                {
                    return list.get(rowIndex).getOrder().getCompany().getName();
                }
                else
                {
                    return "";
                }
            case COLUMN_HOROS:
                return list.get(rowIndex).getOrder().getHorometro();
            default:
                return "-";
        }
    }
}
