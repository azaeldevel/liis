
package database.mysql.purchases.order;

import java.sql.Connection;


/**
 * @version 0.1
 * @author Azael Reyes
 */
public class Document extends process.Document implements process.Searchable
{
    @Override
    public void search(Connection connection, String search) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(Connection connection, String where, String order, int limit, int offset) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
