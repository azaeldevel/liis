
package purchases.order;

import SIIL.Server.Database;
import core.Renglon;
import database.mysql.purchases.order.PO;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class FacturaReadTableModel extends AbstractTableModel
{
    private static final int COLUMN_NUMBER   = 0;
    private static final int COLUMN_DESCRIPTION  = 1;
    private static final int COLUMN_CANT  = 2;
    public static final int COLUMN_COSTO = 3;
    public static final int COLUMN_QUO = 4;
    public static final int COLUMN_PEDIMENTO = 5;
    
    private final String[] columnNames = {"No. Parte","Descripcion","Cantidad","Costo(u)","Cotizacion","Pedimento"};
    private java.util.List<core.Renglon> list;
        
    public java.util.List<core.Renglon> getList()
    {
        return list;
    }
    
    public core.Renglon getValueAt(int rowIndex)
    {
        return list.get(rowIndex);
    }
    
    public Renglon remove(int index)
    {
        Renglon ret = list.remove(index);    
        fireTableDataChanged();
        return ret;
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
    
    public boolean addAll(java.util.List<core.Renglon> sel)
    {
        boolean res = list.addAll(sel);
        fireTableDataChanged();
        return res;
    }
    
    public Boolean download(Database database,PO po) throws SQLException
    {
        list = Renglon.selectStored(database, po, false, null);
        for(core.Renglon renglon : list)
        {
            renglon.download(database, po, false);
        }
                
        fireTableDataChanged();
        return true;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_CANT:
                return Double.class;
            default:
                return String.class;
        }
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
            case COLUMN_CANT:
                return list.get(rowIndex).getCantidad();
            case COLUMN_COSTO:
                String value = "";
                if(list.get(rowIndex).getCostPurchaseMoney() != null)
                {
                    value = list.get(rowIndex).getCostPurchase()+ " " + list.get(rowIndex).getCostPurchaseMoney();
                    return value;
                }
                else
                {
                    return "$";
                }
            case COLUMN_QUO:
                if(list.get(rowIndex).getQuotation() != null)
                {
                    return list.get(rowIndex).getQuotation().toString();
                }
                else
                {
                    return "";
                }         
            case COLUMN_PEDIMENTO:
                if(list.get(rowIndex).getPedimentoNumero() != null && list.get(rowIndex).getPedimentoFecha() != null)
                {
                    return list.get(rowIndex).getPedimentoNumero() + " / " + list.get(rowIndex).getPedimentoFecha();
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