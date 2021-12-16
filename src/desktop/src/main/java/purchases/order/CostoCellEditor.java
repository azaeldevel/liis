
package purchases.order;

import SIIL.Server.Database;
import core.Renglon;
import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import process.Moneda;

/**
 *
 * @author Azael Reyes
 */
public class CostoCellEditor extends AbstractCellEditor implements TableCellEditor 
{
    private final JTextField component;
    private Database dbserver;
    private Moneda moneda;
    
    public CostoCellEditor()
    {
        component = new JTextField();
        component.setBorder(null);     
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,int rowIndex, int vColIndex) 
    {
        FacturaReadTableModel model = (FacturaReadTableModel) table.getModel();
        
        double v = (double) value;
        Renglon renglon = model.getValueAt(rowIndex);
        try 
        {
            boolean ret = renglon.upCostPurchase(dbserver, v, Moneda.USD);
            if(ret)
            {
                dbserver.commit();
            }
            else
            {
                dbserver.rollback();
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(CostoCellEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ((JTextField) component).setText(String.valueOf(v));        
        return component;
    }

    /**
     * @param dbserver the dbserver to set
     */
    public void setDBServer(Database dbserver) {
        this.dbserver = dbserver;
    }    

    @Override
    public Object getCellEditorValue() 
    {
        return Double.parseDouble(component.getText());
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
    
}
