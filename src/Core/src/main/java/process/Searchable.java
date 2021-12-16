
package process;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @version 0.1
 * @author Azael Reyes
 */
public interface Searchable
{
    public void search(Connection connection,String search) throws SQLException;
    public void search(Connection connection,String where,String order,int limit,int offset) throws SQLException;
}
