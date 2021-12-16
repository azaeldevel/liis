
package stock;

import database.mysql.stock.Titem;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class FlowNode extends AbstractMutableTreeTableNode
{
    protected static final int COLUMN_TIPO    = 0;
    protected static final int COLUMN_NUMBER    = 1;
    protected static final int COLUMN_MARK      = 2;
    protected static final int COLUMN_MODEL     = 3;
    protected static final int COLUMN_SERIE     = 4;
    protected static final int COUNT_COLUMN     = 5;

    /**
     * 
     * @param flow
     */
    public FlowNode(Flow flow)
    {
        super(flow);
    }
    
    @Override
    public Object getValueAt(int column) 
    {
        Flow item = getUserObject();
        Titem titem = (Titem) item.getItem();
        switch(column)
        {
            case COLUMN_TIPO:
                return titem.getType();
            case COLUMN_NUMBER:
                return item.getItem().getNumber();
            case COLUMN_MARK:                
                return item.getItem().getMake();
            case COLUMN_MODEL:
                return item.getItem().getModel();
            case COLUMN_SERIE:  
                return item.getSerie();
            default:
                return "";
        }
    }

    @Override
    public int getColumnCount() 
    {
        return COUNT_COLUMN;
    }
    
    @Override
    public Flow getUserObject()
    {
        return (Flow) super.getUserObject();
    }
}
