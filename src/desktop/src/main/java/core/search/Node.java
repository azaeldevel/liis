
package core.search;

import core.Searchable;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

/**
 *
 * @author Azael Reyes
 */
public class Node extends AbstractMutableTreeTableNode
{
    protected static final int COLUMN_IDENTIFICATOR     = 0;
    protected static final int COLUMN_PREVIEW           = 1;
    protected static int COUNT_COLUMN     = 2;    
    
    public Node(Searchable searchable)
    {
        super(searchable);
    }
    
    @Override
    public Object getValueAt(int column) 
    {
        switch(column)
        {
            case COLUMN_IDENTIFICATOR:
                return getUserObject().getIdentificator();
            case COLUMN_PREVIEW:
                return getUserObject().getBrief();
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
    public Searchable getUserObject()
    {
        return (Searchable) super.getUserObject();
    }
}
