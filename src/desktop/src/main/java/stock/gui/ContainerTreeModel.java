
package stock.gui;

import SIIL.Server.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Azael Reyes
 */
public class ContainerTreeModel implements TreeModel
{
    public void download(Database dbserver) throws SQLException
    {
        String sql = "SELECT id FROM " + stock.Container.MYSQL_AVATAR_TABLE + " WHERE parent IS NULL ORDER BY name DESC";
        ResultSet rs = dbserver.query(sql);
        while(rs.next())
        {
            stock.Container container = new stock.Container(rs.getInt(1));
            
        }
    }
    
    @Override
    public Object getRoot() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getChild(Object parent, int index) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChildCount(Object parent) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLeaf(Object node) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
