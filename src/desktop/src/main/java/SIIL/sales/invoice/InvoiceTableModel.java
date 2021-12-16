
package SIIL.sales.invoice;

import SIIL.Server.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import process.Return;

/**
 *
 * @author Azael Reyes
 */
public class InvoiceTableModel  extends AbstractTableModel 
{
    public static final String MYSQL_AVATAR_TABLE = "ServicesTrabajo";    
    
    private static final int COLUMN_NOMEBER = 0;
    private static final int COLUMN_BRIEF   = 1;
    private static final int COLUMN_CANTIDAD    = 2;
    private static final int COLUMN_PU      = 3;
    private static final int COLUMN_IMPORT  = 4;
    
    private List<String> columnNames;
    private List<core.Renglon> list;
    private sales.Invoice invoice;
       
    public Return delete(Database database,int row) throws SQLException
    {
        Return ret = list.get(row).delete(database);
        if(ret.isFail())
        {
            return ret;
        }
        list.remove(row);
        fireTableDataChanged();
        return new Return(true);
    }
    
    public InvoiceTableModel(Database database,sales.Invoice invoice) throws SQLException
    {
        this.invoice = invoice;        
        header();
        list = core.Renglon.select(database,invoice, null);
        refresh(database, false);
    }
    
    public void add(Database database,core.Renglon renglon) throws SQLException
    {
        list.add(renglon);
        refresh(database,false);
    }
    
    public InvoiceTableModel()
    {
        header();
    }

    private void header() 
    {
        list = new ArrayList<>();
        columnNames = new ArrayList<>();
        columnNames.add("Número");
        columnNames.add("Descripción");
        columnNames.add("Cantidad");
        columnNames.add("P/U");
        columnNames.add("Importe");
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_NOMEBER:
                return list.get(rowIndex).getNumber();
            case COLUMN_BRIEF:
                return list.get(rowIndex).getDescripcion();
            case COLUMN_CANTIDAD:
                return list.get(rowIndex).getCantidad() + " " + list.get(rowIndex).getUnidad();
            case COLUMN_PU:
                return String.valueOf(list.get(rowIndex).getCostSales()) + " " + list.get(rowIndex).getCostSalesMoney();
            case COLUMN_IMPORT:
                return String.valueOf(list.get(rowIndex).getTotal()) + " " + list.get(rowIndex).getCostSalesMoney(); 
            default:
                return " - ";
        }
    }

    public List<core.Renglon> getData()
    {
        return list;
    }
    
    public void download(Database database, String sql) throws SQLException 
    {
        //ResultSet rs = database.query(sql);    
        list = core.Renglon.select(database,invoice, null);        
        refresh(database,true);
    }
    
    public void refresh(Database database,boolean reload) throws SQLException
    {
        for(core.Renglon rg : list)
        {
            if(rg.download(database,invoice,reload).isFlag())
            {
                rg.downCostSale(database);
            }
        }
        fireTableDataChanged();
    }
        
    @Override
    public int getRowCount() 
    {
        if(getList() != null)
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

    public core.Renglon getValueAt(int row)
    {
        return getList().get(row);
    }
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /**
     * @return the list
     */
    public List<core.Renglon> getList() 
    {
        return list;
    }
}
