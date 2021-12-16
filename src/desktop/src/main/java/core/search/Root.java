
package core.search;

import core.Searchable;

/**
 *
 * @author Azael Reyes
 */
public class Root extends Node
{
    public Root(Searchable searchable) 
    {
        super(searchable);
    }
    
    public void add(Header header)
    {
        super.add(header);
    }
    
    @Override
    public Object getValueAt(int column) 
    {
        return "";
    }
    
}
