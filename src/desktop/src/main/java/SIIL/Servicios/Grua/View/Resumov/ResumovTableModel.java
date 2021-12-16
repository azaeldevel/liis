
package SIIL.Servicios.Grua.View.Resumov;

import SIIL.services.grua.Resumov;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael
 */
public class ResumovTableModel  extends AbstractTableModel
{   
    
    private String[] columnNames = {"ID", "Num. Eco.", "Marca", "Modelo","Serie","Num. Cliente","Cliente", "Sucursal","Uso","Bateria","Cargador","Fecha","Nota","PO"};
    List<String[]> list;
    
    ResumovTableModel()
    {
        list = new ArrayList<>();
    }
    
    public void clear()
    {
        list.clear();
        fireTableDataChanged();
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
        String[] row = list.get(rowIndex);
        
        if(row[columnIndex] != null ) 
        {
            return row[columnIndex];
        }
        else
        {
            return "";
        }
    }
    
    public Resumov getValueAt(int rowIndex) 
    {
        String[] row = list.get(rowIndex);
        return new Resumov(Integer.parseInt(row[0]));
    }
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) 
    {
        list.set(rowIndex, (String[]) value);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        return String.class;
    }
    
    public void addRow(String[] rowData) 
    {
        list.add(rowData);
    }
    
    @Override
    public String getColumnName(int columnIndex)
    { 
        return columnNames[columnIndex];
    }
}
