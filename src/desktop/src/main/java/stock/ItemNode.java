
package stock;

import database.mysql.stock.Item;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

/**
 *
 * @author Azael Reyes
 */
public class ItemNode extends AbstractMutableTreeTableNode
{
    protected static final int COLUMN_NUMBER    = 0;
    protected static final int COLUMN_MARK      = 1;
    protected static final int COLUMN_MODEL     = 2;
    protected static final int COLUMN_SERIE     = 3;

    /**
     * 
     * @param item
     */
    public ItemNode(Item item)
    {
        super(item);
    }
    
    @Override
    public Object getValueAt(int column) 
    {
        Item item = getUserObject();
        switch(column)
        {
            case COLUMN_NUMBER:
                return item.getNumber();
            case COLUMN_MARK:                
                return item.getMake();
            case COLUMN_MODEL:
                return item.getModel();
            default:
                return "";
        }
    }

    @Override
    public int getColumnCount() 
    {
        return 4;
    }
    
    @Override
    public Item getUserObject()
    {
        return (Item) super.getUserObject();
    }
}
