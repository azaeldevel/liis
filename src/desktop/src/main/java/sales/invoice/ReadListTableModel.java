
package sales.invoice;

import SIIL.Server.Company;
import SIIL.Server.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import process.State;
import sales.Invoice;

/**
 *
 * @author Azael Reyes
 */
public class ReadListTableModel extends AbstractTableModel 
{   
    private static final int COLUMN_NUMBER   = 0;
    private static final int COLUMN_NAME  = 1;
    private static final int COLUMN_FECHA   = 2;
    private static final int COLUMN_FOLIO   = 3;
    public static final String MYSQL_AVATAR_TABLE = "SalesInvoice_Resolved"; 
    
    
    private String[] columnNames = {"Número", "Nombre", "Fecha", "Folio"};
    private List<Invoice> list;
    
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
        Invoice invoice = list.get(rowIndex);
        switch (columnIndex) 
        {
            case COLUMN_NUMBER:
                return invoice.getEnterprise().getNumber();
            case COLUMN_NAME:
                return invoice.getEnterprise().getName();
            case COLUMN_FECHA:
                return invoice.getFhFolio();
            case COLUMN_FOLIO:
                if(invoice.getState().getOrdinal() > 3) return invoice.getFolio();
                else return "";
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
    
    public Invoice getValueAt(int selectedRow) 
    {
        return list.get(selectedRow);
    }
    

    public void download(Database database, String sql) throws SQLException 
    {
        ResultSet rs = database.query(sql);    
        list = new ArrayList<>();
        while(rs.next())
        {
            list.add(new Invoice(rs.getInt(1)));            
        }
        
        refresh(database);
    }
    
    public void refresh(Database database) throws SQLException
    {
        for(Invoice in : list)
        {
            if(in.download(database).isFlag())
            {
                if(in.downCompany(database))
                {
                    in.getEnterprise().download(database);
                }
            }
        }
        
        fireTableDataChanged();
    }
    
    public void load(Database database,State step) throws SQLException
    {
        if(step == null) return;        
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE state = " + step.getID();
        //System.out.println(sql);
        download(database, sql);        
    }
}
