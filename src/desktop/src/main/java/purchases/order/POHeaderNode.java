
package purchases.order;

import database.mysql.purchases.order.PO;

/**
 * @version 0.1
 * @author Azael Reyes
 */
public class POHeaderNode extends POChildNode
{    
    String title;
    
    public POHeaderNode(PO po) 
    {
        super(po);
    }  
    
    @Override
    public Object getValueAt(int column) 
    {
        if(getUserObject() == null) return null;
        String tmp = null;
        switch(column)
        {
            case COLUMN_FOLIO:
                return title;
            default:
                return "---";
        }
    }
    
    /**
     * 
     * @param node 
     */
    public void add(POChildNode node)
    {
        super.add(node);
    }
}
