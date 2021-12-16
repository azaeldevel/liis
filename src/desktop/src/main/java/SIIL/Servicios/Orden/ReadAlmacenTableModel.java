
package SIIL.Servicios.Orden;

import SIIL.Server.Database;
import SIIL.core.Office;
import SIIL.service.quotation.ServiceQuotation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class ReadAlmacenTableModel  extends AbstractTableModel
{
    public static final String MYSQL_AVATAR_TABLE = "Orcom";
    private static final int COLUMN_FOLIO   = 0;
    private static final int COLUMN_MANAGER = 1;
    private static final int COLUMN_CLIENT  = 2;
    private static final int COLUMN_ETA = 3;
    private static final int COLUMN_SA  = 4;
    private static final int COLUMN_PO  = 5;
    
    private List<String> columnNames;
    private List<ServiceQuotation> list;

    
    
    public void listing(Database database, Date date, Office office) throws SQLException
    {
        String sql = "SELECT * FROM " + MYSQL_AVATAR_TABLE ;
        Calendar calendar = new GregorianCalendar();        
        if(date != null)
        {       
            calendar.setTime(date);            
        }
        else
        {
            Date dtHoy = database.getDateToday();
        }
        sql += " WHERE estado = 'pedArrb' AND YEAR(fhArribo) = " + calendar.get(Calendar.YEAR) + " AND MONTH(fhArribo) = " + (calendar.get(Calendar.MONTH) + 1) + " AND DAY(fhArribo) = " + calendar.get(Calendar.DAY_OF_MONTH);
        if(office != null)
        {
            sql += " AND suc = '" + office.getCode() + "'";
        }
        //System.out.println(sql);
        download(database, sql);
    }
    
    public ReadAlmacenTableModel()
    {
        list = new ArrayList<>();
        columnNames = new ArrayList<>();
        columnNames.add("Folio");
        columnNames.add("Encargado");
        columnNames.add("Cliente");
        columnNames.add("E.T.A.");
        columnNames.add("S.A.");
        columnNames.add("PO");        
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_FOLIO:
                return list.get(rowIndex).getFolio();
            case COLUMN_MANAGER:
                    return list.get(rowIndex).getOwner().toString();
            case COLUMN_CLIENT:
                return list.get(rowIndex).getEntreprise().toString();
            case COLUMN_ETA:
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
                    return sdf.format(list.get(rowIndex).getFhETA());                
                }
            case COLUMN_SA:
                return list.get(rowIndex).getSA();
            case COLUMN_PO:
                    if(list.get(rowIndex).getPOFile() != null) return list.get(rowIndex).getPOFile().getFolio();
                    return "";
            default:
                return "-";                
        }
    }    
    
    public List<ServiceQuotation> getData()
    {
        return list;
    }
    
    public void download(Database database, String sql) throws SQLException 
    {
        ResultSet rs = database.query(sql);    
        list = new ArrayList<>();
        while(rs.next())
        {
            list.add(new ServiceQuotation(rs.getInt(1)));            
        }
        refresh(database);
    }
    
    public void refresh(Database database) throws SQLException
    {
        for(ServiceQuotation ord : list)
        {
            if(ord.download(database))
            {
                ord.downFolio(database.getConnection());
                ord.downCompany(database);
                if(ord.downPOFile(database).isFlag())
                {
                    ord.getPOFile().download(database);
                }
                ord.downOwner(database);
                ord.downFhETA(database);
            }
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
        return String.class;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames.get(columnIndex);
    }

    public ServiceQuotation getValueAt(int row)
    {
        return list.get(row);
    }
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
}
