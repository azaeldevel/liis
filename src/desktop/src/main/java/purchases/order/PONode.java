
package purchases.order;

import java.text.SimpleDateFormat;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import database.mysql.purchases.order.PO;

/**
 * @version 0.1
 * @author Azael Reyes
 */
public class PONode  extends AbstractMutableTreeTableNode
{
    protected static final int COLUMN_FOLIO     = 0;
    protected static final int COLUMN_PROVIDER  = 1;
    protected static final int COLUMN_FECHA     = 2;
    protected static int COUNT_COLUMN     = 3;

    public PONode(PO po)
    {
        super(po);
    }
    
    @Override
    public Object getValueAt(int column) 
    {
        PO po = getUserObject();
        if(po == null) return null;
        String tmp = null;
        switch(column)
        {
            case COLUMN_FOLIO:
                if(po.getSerie() != null) 
                {
                    tmp = po.getSerie();
                }
                else
                {
                    tmp = "";
                }
                tmp += po.getFolio();
                return tmp;
            case COLUMN_PROVIDER:
                return po.getProvider().getNameRazonSocial();
            case COLUMN_FECHA:
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if(po.getFhETA() != null)
                {
                    return sdf.format(po.getFhETA());
                }
                else
                {
                    return"";
                }
            default:
                return null;
        }
    }

    @Override
    public int getColumnCount() 
    {
        return COUNT_COLUMN;
    }
    
    @Override
    public PO getUserObject()
    {
        return (PO) super.getUserObject();
    }
}
