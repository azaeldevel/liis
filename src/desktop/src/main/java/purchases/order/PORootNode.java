
package purchases.order;

import database.mysql.purchases.order.PO;

/**
 * @version 0.1
 * @author Azael Reyes
 */
public class PORootNode extends PONode
{    
    /**
     * 
     * @param po 
     */
    public PORootNode(PO po) 
    {
        super(po);
    }
    
    /**
     * 
     * @param node 
     */
    public void add(POHeaderNode node)
    {
        super.add(node);
    }
}
