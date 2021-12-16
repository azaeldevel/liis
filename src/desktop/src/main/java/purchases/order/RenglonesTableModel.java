
package purchases.order;

import SIIL.Server.Database;
import database.mysql.purchases.order.PO;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import process.Return;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class RenglonesTableModel  extends AbstractTableModel
{
    private static final int COLUMN_NUMBER   = 0;
    private static final int COLUMN_DESCRIPTION  = 1;
    private static final int COLUMN_QUOTATION    = 2;
    private static final int COLUMN_CANT  = 3;
    private static final int COLUMN_COSTO = 4;
    private static final int COLUMN_TOTAL = 5;
    private static final int COLUMN_ETA = 6;
    
    private final String[] columnNames = {"No. Parte", "Descripción","Cotización", "Cantidad","Costo(u)","Total","E.T.A"};
    private List<core.Renglon> list;
    
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
    
    /**
     * 
     * @param row
     * @return 
     */
    public boolean add(core.Renglon row)
    {
        return list.add(row);
    }
    
    public boolean addAll(List<core.Renglon> sel)
    {
        boolean res = list.addAll(sel);
        fireTableDataChanged();
        return res;
    }
    
    public Boolean download(Database database,PO po) throws SQLException
    {
        list = core.Renglon.select(database, po, null);
        for(core.Renglon renglon : list)
        {
            renglon.download(database, po, false);
        }
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
            return null;
        }
        switch(columnIndex)
        {
            case COLUMN_NUMBER:
                return list.get(rowIndex).getNumber();
            case COLUMN_DESCRIPTION:
                return list.get(rowIndex).getDescription();
            case COLUMN_QUOTATION:
                if(list.get(rowIndex).getQuotation() != null)
                {
                    return list.get(rowIndex).getQuotation().getFullFolio();
                }
                else
                {
                    return "";
                }
            case COLUMN_CANT:
                return list.get(rowIndex).getCantidad();
            case COLUMN_ETA:
                if(list.get(rowIndex).getPurchaseETA() != null)
                {
                    SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yy");                    
                    return formateador.format(list.get(rowIndex).getPurchaseETA());
                }
                else
                {
                    return "";
                }
            default:
                return "";
        }
    }
}
