
package SIIL.purchase.cr;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
class CUTableModel  extends AbstractTableModel
{
    private static final int COLUMN_FOLIO  = 0;
    private static final int COLUMN_MONTO  = 1;

    private String[] columnNames = {"Folio", "Monto"};
    private java.util.List<Factura> list;
    
    CUTableModel()
    {
        list =  new ArrayList<>();
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
        Factura factura = list.get(rowIndex);
        
        switch(columnIndex)
        {
            case COLUMN_FOLIO:
                if(factura.getFolio() != null) return factura.getFolio();
                return "";
            case COLUMN_MONTO:
                return factura.getMonto();
        }
        
        return null;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        switch(columnIndex)
        {
            case COLUMN_FOLIO:
                list.get(rowIndex).setFolio((String) aValue);
            case COLUMN_MONTO:
                list.get(rowIndex).setMonto(Float.valueOf((String)aValue));
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        if (list == null || list.isEmpty()) 
        {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public String getColumnName(int columnIndex)
    { 
        return columnNames[columnIndex];
    }
    
    Factura getFactura(int rowIndex)
    {
        return list.get(rowIndex);
    }

    ArrayList<Factura> getFacturas() {
        return (ArrayList<Factura>) list;
    }

    void clear() 
    {
        list.clear();
    }

    void add(Factura fac) 
    {
        list.add(fac);
    }
}
