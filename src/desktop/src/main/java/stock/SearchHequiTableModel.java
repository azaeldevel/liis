
package stock;

import SIIL.Server.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class SearchHequiTableModel extends AbstractTableModel 
{
    private final String MYSQL_AVATAR_TABLE = "Titem";
    private final String MYSQL_AVATAR_TABLE_ITEM = "Item";
    
    public enum Mode
    {
        ListEquipos,
        SelectTitem,
        SelectForklift,
        SelectItem,
        VentaItem
    }
    
    private static final int COLUMN_NUMBER = 0;
    private static final int COLUMN_MARK = 1;
    private static final int COLUMN_MODEL = 2;
    private static final int COLUMN_SERIE = 3;
    
    private String[] columnNames = {"Número", "Marca", "Modelo", "Serie"};
    private List<Flow> list;
    
    public Boolean search(Database database,Mode mode,String search)
    {
        if(database == null) return false;
        
        String table = "";
        if(mode == Mode.ListEquipos)
        {
            table = MYSQL_AVATAR_TABLE + "_Resolved";
        }
        else if(mode == Mode.SelectForklift)
        {
            table = "Forklift_Resolved";
        }
        else if(mode == Mode.SelectTitem)
        {
            table = MYSQL_AVATAR_TABLE + "_Resolved";
        }
        else if(mode == Mode.SelectItem)
        {
            table = MYSQL_AVATAR_TABLE_ITEM + "_Resolved";
        }
        else
        {
            table = MYSQL_AVATAR_TABLE + "_Resolved";
        }
        
        String sql = "SELECT idFlow FROM " + table;        
        if(search != null)
        {
            if(mode == Mode.VentaItem)
            {
                sql = sql + " WHERE idT IS NOT NULL AND number like '%" + search + "%' OR make like '%" + search + "%' OR model like '%" + search + "%' OR serie like '%" + search + "%' AND estado = 'L'";
            }
            else if(mode != Mode.SelectItem)
            {
                sql = sql + " WHERE idT IS NOT NULL AND number like '%" + search + "%' OR make like '%" + search + "%' OR model like '%" + search + "%' OR serie like '%" + search + "%'";
            }  
        }
        else
        {
            if(mode == Mode.VentaItem)
            {
                sql = sql + " WHERE idT IS NOT NULL AND estado = 'L'";
            }
            else if(mode != Mode.SelectItem)
            {
                sql = sql + " WHERE idT IS NOT NULL ";                
            }
        }
        sql = sql + " ORDER BY number DESC LIMIT 50";
        ResultSet rs;
        list = new ArrayList<>();
        try
        {
            System.out.println(sql);
            rs = database.query(sql);
            while(rs.next())
            {
                Flow flow = new Flow(rs.getInt(1));
                flow.downItem(database);
                flow.getItem().downNumber(database.getConnection());
                flow.getItem().downMake(database.getConnection());
                flow.getItem().downModel(database);
                //flow.getItem().downSerie(database);
                flow.getItem().downDescription(database.getConnection());
                flow.downSerie(database);
                list.add(flow);
            }
            fireTableStructureChanged();
            return true;
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            list = new ArrayList<>();
            return false;
        }
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

    public Flow getValueAt(int row)
    {
        return list.get(row);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_NUMBER:
                return list.get(rowIndex).getItem().getNumber();
            case COLUMN_MARK:
                return list.get(rowIndex).getItem().getMake();
            case COLUMN_MODEL:
                return list.get(rowIndex).getItem().getModel();
            case COLUMN_SERIE:
                if(list.get(rowIndex).isActiveSerie()) 
                {
                    return list.get(rowIndex).getSerie();
                }
                else
                {
                    return "";  
                }
            default:
                return "-";
        }
    }   
}
