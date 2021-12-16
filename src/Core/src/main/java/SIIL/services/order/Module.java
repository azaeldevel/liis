
package SIIL.services.order;

import SIIL.Server.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Azael Reyes
 */
public class Module 
{
    private static final String MYSQL_AVATAR_TABLE = "ServiceOrderModules";
    
    private int id;
    private String name;
    
    public String toString()
    {
        return name;
    }
    
    public static List<Module> listing(Database db) throws SQLException
    {
        ArrayList<Module> ls = new ArrayList<>();
        
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE;
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            ls.add(new Module(rs.getInt(1)));
        }
        return ls;
    }
    
    /**
     * 
     * @param id 
     */
    public Module(int id)
    {
        this.id = id;
    }
    
    /**
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @param db
     * @return
     * @throws SQLException 
     */
    public Boolean downName(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT name FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.name = rs.getString(1);
            return true;
        }
        else
        {
            this.name = null;
            return false;
        }
    }
}
