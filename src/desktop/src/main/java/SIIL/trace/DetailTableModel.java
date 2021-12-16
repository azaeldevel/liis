
package SIIL.trace;

import SIIL.Server.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael
 */
public class DetailTableModel extends AbstractTableModel
{    
    private static final int COLUMN_BRIEF   = 0;
    private static final int COLUMN_BEFORE  = 1;
    private static final int COLUMN_AFTER   = 2;
    private static final int COLUMN_KEY     = 3;
    
    private String[] columnNames = {"Dato", "Antes", "Despues", "Llave"};
    private List<Value> listValues;
    
    DetailTableModel()
    {
        listValues = new ArrayList<Value>();
    }
    
    @Override
    public int getRowCount() 
    {
        if(listValues != null)
        {
            return listValues.size();
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
        Value value = listValues.get(rowIndex);
        Object returnValue = null;
        
        switch (columnIndex) 
        {
            case COLUMN_AFTER:
                returnValue = value.getAfter();
                break;
            case COLUMN_BEFORE:
                returnValue = value.getBefore();
                break;
            case COLUMN_BRIEF:
                returnValue = value.getBrief();
                break;
            case COLUMN_KEY:
                returnValue =  value.getLlave();
                break;
        }
        
        return returnValue;
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

    void fill(Database conn, Trace trace) throws SQLException 
    {
        listValues = Trace.downDetail(conn,trace);
    }
}
