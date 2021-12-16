
package SIIL.Servicios.Grua;

import core.PlainTitem;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import stock.Flow;


/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class HequisTableModel<Type> extends AbstractTableModel 
{
    private final String MYSQL_AVATAR_TABLE = "Titem";
        
    private static final int COLUMN_NUMBER = 0;
    private static final int COLUMN_MARK = 1;
    private static final int COLUMN_MODEL = 2;
    private static final int COLUMN_SERIE = 3;
    private static final int COLUMN_ADIC = 4;
    
    private String[] columnNames = {"Número", "Marca", "Modelo", "Serie","Aditamentos"};
    private List<Type> list;
    private Type type;
    
    public Type delete(int row)
    {
        Type ret = list.remove(row);
        fireTableDataChanged();        
        return ret;
    }
    
    public void reload()
    {
        fireTableDataChanged();
    }
    @Override
    public int getRowCount() 
    {
        if(getList() != null)
        {
            return getList().size();
        }
        else
        {
            return 0;
        }
    }
    
    public HequisTableModel(Type type)
    {
        list = new ArrayList<>();
        this.type = type;
    }
    
    public boolean add(Type flow)
    {
        boolean fl = list.add(flow);        
        fireTableDataChanged();        
        return fl; 
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.length;
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

    public Type getValueAt(int row)
    {
        return list.get(row);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {
        if(type instanceof Flow)
        {
            switch(columnIndex)
            {
                case COLUMN_NUMBER:
                    return ((Flow)list.get(rowIndex)).getItem().getNumber();
                case COLUMN_MARK:
                    return ((Flow)list.get(rowIndex)).getItem().getMake();
                case COLUMN_MODEL:
                    return ((Flow)list.get(rowIndex)).getItem().getModel();
                case COLUMN_SERIE:
                    return ((Flow)list.get(rowIndex)).getSerie();
                case COLUMN_ADIC:
                {
                    String str = "";
                    if(((Flow)list.get(rowIndex)).getItem() instanceof SIIL.services.grua.Forklift)
                    {
                        SIIL.services.grua.Forklift forklift = (SIIL.services.grua.Forklift) ((Flow)list.get(rowIndex)).getItem();
                        if(forklift.battery != null)
                        {
                            str = str + "|" + forklift.battery.getNumber();
                        }
                        if(forklift.charger != null)
                        {
                            str = str + "|" + forklift.charger.getNumber();
                        }
                        if(forklift.mina != null)
                        {
                            str = str + "|" + forklift.mina.getNumber();
                        }   
                        if(forklift.horometro > 0)
                        {
                            str = str + "|" + forklift.horometro;
                        }                      
                    }
                    return str;
                }
                default:
                    return "-";
            }
        }
        else if(type instanceof PlainTitem)
        {
            switch(columnIndex)
            {
                case COLUMN_NUMBER:
                    return ((PlainTitem)list.get(rowIndex)).number;
                case COLUMN_MARK:
                    return ((PlainTitem)list.get(rowIndex)).make;
                case COLUMN_MODEL:
                    return ((PlainTitem)list.get(rowIndex)).model;
                case COLUMN_SERIE:
                    return ((PlainTitem)list.get(rowIndex)).serie;
                default:
                    return "-";
            }
        }
        else
        {
            return "";
        }        
    }

    /**
     * @return the list
     */
    public List<Type> getList() 
    {
        return list;
    }
}