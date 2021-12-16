
package SIIL.services.trabajo;

import SIIL.services.Trabajo;
import java.awt.Color;
import java.util.Date;
import process.State;

/**
 *
 * @author Azael Reyes
 */
public class CellRenderer extends javax.swing.table.DefaultTableCellRenderer
{
    public static final Color RED = new Color(255,102,102);
    public static final Color GREEN = new Color(102,200,102);
    public static final Color BLUE = new Color(153,200,234);
    public static final Color ORANGE = new Color(255,157,102);
    public static final Color PURPLE = new Color(157,157,255);
    public static final Color YELLOW_LOW = new Color(202,202,150);
    public static final Color YELLOW_MEDIUM = new Color(202,202,100);
    public static final Color YELLOW_HIGH = new Color(202,202,50);
    public static final Color GRAY = Color.GRAY;
    private static final int ONEWEEK = 604800000;
    private static final int TOWWEEKS = 604800000 * 2;
    
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column) 
    {
        final java.awt.Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        RelacionTableModel model = (RelacionTableModel) table.getModel();
        Trabajo trabajo = model.getValueAt(table.convertRowIndexToModel(row)); 
        State state = trabajo.getState();
        int ordinal = trabajo.getState().getOrdinal();
        if(isSelected)
        {
            cellComponent.setBackground(Color.BLUE);
            cellComponent.setForeground(Color.WHITE);
            return cellComponent;
        }
        else if(trabajo.getSheet() == Trabajo.Sheet.CAMPO)
        {
            if(trabajo.getState().getOrdinal() == 2)
            {
                cellComponent.setBackground(RED);
            }
            else if(trabajo.getState().getOrdinal() == 3)
            {
                cellComponent.setBackground(BLUE);
            }
            else if(trabajo.getState().getOrdinal() == 4)
            {
                cellComponent.setBackground(PURPLE);
            }
            else if(trabajo.getState().getOrdinal() == 5)
            {
                cellComponent.setBackground(ORANGE);
            }
            else if(trabajo.getState().getOrdinal() == 6)
            {
                cellComponent.setBackground(GREEN);
            }
            else if(trabajo.getState().getOrdinal() == 7)
            {
                cellComponent.setBackground(GREEN);
            }
            else if(trabajo.getState().getOrdinal() == 8)
            {
                cellComponent.setBackground(GREEN);
            }
            else
            {
                cellComponent.setBackground(Color.white);
            }
        }
        else if(trabajo.getSheet() == Trabajo.Sheet.MTTO)
        {
            Date fecha = trabajo.getFhToDo();
            Date now = model.getNow();
            long diff = now.getTime() - fecha.getTime();
            int sems = (int)(diff / ONEWEEK);
            if(diff < 1)
            {
                cellComponent.setBackground(Color.WHITE);
            }
            else if(diff < ONEWEEK)
            {
                cellComponent.setBackground(YELLOW_LOW);
            }
            else if(diff < TOWWEEKS)
            {
                cellComponent.setBackground(YELLOW_MEDIUM);
            }
            else if(diff >= TOWWEEKS)
            {
                cellComponent.setBackground(RED);
            }
            else
            {
                cellComponent.setBackground(YELLOW_HIGH);
            }
        }
        else if(trabajo.getState().getOrdinal() == 2)
        {
            cellComponent.setBackground(RED);
        }
        else if(trabajo.getState().getOrdinal() == 3)
        {
            cellComponent.setBackground(BLUE);
        }
        else if(trabajo.getState().getOrdinal() == 4)
        {
            cellComponent.setBackground(PURPLE);
        }
        else if(trabajo.getState().getOrdinal() == 5)
        {
            cellComponent.setBackground(ORANGE);
        }
        else if(trabajo.getState().getOrdinal() == 6)
        {
            cellComponent.setBackground(GREEN);            
        }
        else if(trabajo.getState().getOrdinal() == 7)
        {
            cellComponent.setBackground(GREEN);            
        }
        else if(trabajo.getState().getOrdinal() == 8)
        {
            cellComponent.setBackground(GREEN);            
        }
        else
        {
            cellComponent.setBackground(Color.white);
        }
        
        /*
        if(trabajo.getState().getOrdinal() == 6)
        {
            cellComponent.setForeground(GRAY);
        }
        else
        {
            cellComponent.setForeground(Color.black);  
        }
        */
        cellComponent.setForeground(Color.black);
        
        return cellComponent;
    }
}
