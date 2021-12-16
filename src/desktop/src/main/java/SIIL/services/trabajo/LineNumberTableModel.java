
package SIIL.services.trabajo;

import SIIL.services.Trabajo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael Reyes
 */
public class LineNumberTableModel extends AbstractTableModel  
{
    private static final int COLUMN_COUNT   = 0;    
    
    private ArrayList<String> columnNames;
    private List<Trabajo> list;
    private String whereSQL;
    private String tableSQL;
    private String lastSearch;
    private boolean activeDate;
    
    public LineNumberTableModel()
    {
        list = new ArrayList<>();
        columnNames = new ArrayList<>();
        columnNames.add("Número");           
    }
    
    public void setData(List<Trabajo> list)
    {
        this.list = list;
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
        return Integer.class;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames.get(columnIndex);
    }

    public Trabajo getValueAt(int row)
    {
        return list.get(row);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        switch(columnIndex)
        {
            case COLUMN_COUNT:
                    return String.valueOf(rowIndex+1);
        }
        return null;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
}

