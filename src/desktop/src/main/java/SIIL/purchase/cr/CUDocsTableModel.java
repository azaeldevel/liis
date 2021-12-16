
package SIIL.purchase.cr;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Azael
 */
class CUDocsTableModel extends AbstractTableModel
{
    private static final int COLUMN_PO  = 0;
    private static final int COLUMN_SA  = 1;

    private String[] columnNames = {"P.O.", "S.A."};
    private java.util.List<Document> listValues;
    
    java.util.List<Document> getList()
    {
        return listValues;
    }
    
    CUDocsTableModel()
    {
        listValues = new ArrayList<>();
        fireTableDataChanged();
    }
    
    public CUDocsTableModel(ArrayList<Document> list)
    {
        listValues = list;
    }
    
    @Override
    public int getRowCount() 
    {
        return listValues.size();
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        Document doc = listValues.get(rowIndex);
        Object returnValue = null;
        
        switch (columnIndex) 
        {
            case COLUMN_PO:
                if(doc.getPO() == null)
                {
                    return "";
                }
                else
                {
                    returnValue = doc.getPO();
                }
                break;
            case COLUMN_SA: 
                if(doc.getSA() == null)
                {
                    return "";
                }
                else
                {
                    returnValue = doc.getSA();
                }
                break;
        }
        
        return returnValue;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {        
        switch (columnIndex) 
        {
            case COLUMN_PO:
                listValues.get(rowIndex).setPO((String) aValue);
                break;
            case COLUMN_SA: 
                listValues.get(rowIndex).setSA((String) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) 
    {
        if (listValues == null || listValues.isEmpty()) 
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
    
    public Document getDocument(int rowIndex)
    {
        return listValues.get(rowIndex);
    }

    void clear() 
    {
        listValues.clear();
    }

    void setList(ArrayList<Document> documents) 
    {
        listValues = documents;
    }
    
}
