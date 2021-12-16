
package SIIL.sales.rows;

import SIIL.Server.Database;
import database.mysql.sales.Quotation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class RowsTableModel extends AbstractTableModel
{   
    private Database database;
    private final String[] columnNames = {"Numero", "Descripción","Cantidad","Cotizada","Comprada"};
    private List<core.Renglon> list;
    private static final int COLUMN_NUMBER = 0;
    private static final int COLUMN_DESCRIP = 1;
    private static final int COLUMN_CANT = 2;
    private static final int COLUMN_SQ = 3;
    private static final int COLUMN_PO = 4;
    //private String serie;
    //private int folio;
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) 
    {
        return false;
    }
    
    public core.Renglon getRenglon(int rowIndex)
    {
        return list.get(rowIndex);
    }
    
    public void setQuotation(Database database,Quotation quotation) throws SQLException
    {
        this.database = database;
        
        List<core.Renglon> ret = core.Renglon.select(database, quotation, null);
        
        if(ret != null) 
        {
            list = ret;
        }
        else
        {
            list = new ArrayList<>();
        }
        
        fireTableDataChanged();
    } 
    
    public RowsTableModel()
    {
        list = new ArrayList<>();
    }
    
    @Override
    public String getColumnName(int columnIndex)
    { 
        return columnNames[columnIndex];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_NUMBER:
            case COLUMN_DESCRIP:
                return String.class;
            case COLUMN_CANT:
                return Short.class;
            case COLUMN_PO:
            case COLUMN_SQ:
                return Boolean.class;
            default:
                return String.class;
        }
    }
    
    @Override
    public int getRowCount() 
    {
        return list.size();
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_NUMBER:
                return list.get(rowIndex).getNumber();
            case COLUMN_DESCRIP:
                return list.get(rowIndex).getDescription();
            case COLUMN_CANT:
                return list.get(rowIndex).getCantidad();
            case COLUMN_SQ:
                if(list.get(rowIndex).getQuotation() != null)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            case COLUMN_PO:
                if(list.get(rowIndex).getPO() != null)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            default:
                return "";
        }
    }
}
