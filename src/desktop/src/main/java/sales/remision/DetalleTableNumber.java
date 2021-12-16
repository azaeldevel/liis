
package sales.remision;

import SIIL.Server.Database;
import core.Renglon;
import database.mysql.sales.Remision;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import process.Return;

/**
 *
 * @author Azael Reyes
 */
public class DetalleTableNumber extends AbstractTableModel
{
    private static final int COLUMN_NUMBER   = 0;
    private static final int COLUMN_DESCRIPTION  = 1;
    private static final int COLUMN_CANT  = 2;
    private static final int COLUMN_COSTO = 3;
    private static final int COLUMN_MONTO = 4;
    
    private final String[] columnNames = {"No. Parte", "Descripción", "Cantidad","Costo(u)","Monto"};
    private List<core.Renglon> list;
    
    public Renglon getValueAt(int row)
    {
        return list.get(row);
    }
    
    public double monto(int rowIndex)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        double val = list.get(rowIndex).getCostSales() * list.get(rowIndex).getCantidad();
        if(val == 0.0) return val;
        String formate = df.format(val); 
        double finalValue = 0.0;
        
        try 
        {
            finalValue =(Double) df.parse(formate);
        }
        catch (ParseException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        return finalValue;
    }
        
    public Return remove(Database dbserver,int index) throws SQLException
    {
        Return ret = list.get(index).delete(dbserver);
        if(ret.isFail()) return ret;
        core.Renglon del = list.remove(index);
        if(del == null)
        {
            return new Return(false,"Falló la eliminación de la lista");
        }
        else
        {
            fireTableDataChanged();
            return new Return(true);
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) 
    {
        return false;
    }
    
    /**
     * Actualiza las vista
     */
    public void reload()
    {
        fireTableDataChanged();
    }
        
    
    public Boolean download(Database database, Remision remision) throws SQLException
    {
        list = Renglon.select(database, remision, null);
        for(Renglon renglon : list)
        {
            renglon.download(database, remision, false);
            renglon.downCostSale(database);
        }
        fireTableDataChanged();
        return true;
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
        if(list.size() < 1)
        {
            return "";
        }
        
        switch(columnIndex)
        {
            case COLUMN_NUMBER:
                return list.get(rowIndex).getNumber();
            case COLUMN_DESCRIPTION:
                return list.get(rowIndex).getDescription();
            case COLUMN_CANT:
                return list.get(rowIndex).getCantidad();
            case COLUMN_COSTO:
                if(list.get(rowIndex).getCostSales() > 0  && list.get(rowIndex).getCostSalesMoney() != null)
                {
                    return "$ " + list.get(rowIndex).getCostSales() + " " + list.get(rowIndex).getCostSalesMoney();
                }
                else
                {
                    return "$";
                }
            case COLUMN_MONTO:
                if(list.get(rowIndex).getCostSales() > 0  && list.get(rowIndex).getCostSalesMoney() != null)
                {
                    return "$ " + monto(rowIndex) + " " + list.get(rowIndex).getCostSalesMoney();
                }
                else
                {
                    return "$";
                }
            default:
                return "";
        }
    }    
}
