
package purchases.order;

import java.awt.Color;
import stock.Flow;

/**
 *
 * @author Azael Reyes
 */
public class FacturaCellRenderer extends javax.swing.table.DefaultTableCellRenderer
{
    
    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column) 
    {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        FacturaReadTableModel model = (FacturaReadTableModel) table.getModel();
        core.Renglon renglon = model.getValueAt(table.convertRowIndexToModel(row));         
        Flow.Estado estado = renglon.getEstado();
        
        if(estado != null)
        {
            super.setForeground(Color.GRAY);
        }
        else
        {
            super.setForeground(Color.BLACK);
        }
        
        return this;
    }
}
