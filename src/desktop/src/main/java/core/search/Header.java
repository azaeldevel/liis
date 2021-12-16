
package core.search;

import core.Searchable;

/**
 *
 * @author Azael Reyes
 */
public class Header extends Node
{
    private String name;
    public Header(Searchable searchable,String name) 
    {
        super(searchable);
        this.name = name;
    }   
    
    public void add(Child child)
    {
        super.add(child);
    }
        
    @Override
    public Object getValueAt(int column) 
    {
        if(column == 0)
        {
            return name;
        }
        else
        {
            return "";            
        }
    }
}
