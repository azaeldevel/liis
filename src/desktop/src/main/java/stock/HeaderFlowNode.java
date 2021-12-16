
package stock;

import database.mysql.stock.Item;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class HeaderFlowNode extends FlowNode
{
    public HeaderFlowNode(Flow flow) 
    {
        super(flow);
    }
    
    @Override
    public Object getValueAt(int column) 
    {
        return "";
    }
}
