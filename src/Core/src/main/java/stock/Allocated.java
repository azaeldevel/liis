
package stock;

import SIIL.Server.Database;
import core.Renglon;
import database.mysql.stock.Item;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import process.Return;
import process.Row;

/**
 * MySQL Avatar
 * @version 0.1
 * @author Azael Reyes
 */
public class Allocated 
{
    public static final String MYSQL_AVATAR_TABLE = "StockAllocated";
    
    private int id;
    private Container container;
    private Flow item;

    public boolean download(Database dbserver) throws SQLException
    {
        String sql = "SELECT id,container,item FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            id = rs.getInt(1);
            if(rs.getInt(2) > 0)
            {
                container = new Container(rs.getInt(2));
                container.download(dbserver);
            }
            
            if(rs.getInt(3) > 0)
            {
                item = new Flow(rs.getInt(3));
                item.download(dbserver);
                item.getItem().download(dbserver);
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @param dbserver
     * @param renglon
     * @return true si el renglon completo es cargado, null si es parcial y false si no hay alguno
     * @throws SQLException 
     */
    public static Boolean isStored(Database dbserver, Renglon renglon) throws SQLException
    {
        List<Row> rws = renglon.getRows();        
        for(Row row : rws)
        {
            int count = 0;
            String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE item = " + row.getItem().getID();
            ResultSet rs = dbserver.query(sql);
            if(rs.next())
            {
               count++;
               continue; 
            }
            else
            {
                if(count > 0)
                {
                    return null;
                }
                else
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static Allocated isStored(Database dbserver,Flow item) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE item = " + item.getID();
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            return new Allocated(rs.getInt(1));
        }
        return null;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the container
     */
    public Container getContainer() {
        return container;
    }

    /**
     * @return the item
     */
    public Flow getFlow() {
        return item;
    }
    
    public Return insert(Connection connection, Container container, Flow item) throws SQLException
    {        
        if(id > 0)
        {
            return new Return(Return.Status.FAIL,"Allacated tine un ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(container == null)
        {
            return new Return(Return.Status.FAIL,"container is null.");            
        }
        else if(container.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"container tine un ID invalido."); 
        }
        if(item == null)
        {
            return new Return(Return.Status.FAIL,"item is null.");            
        }
        else if(item.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"item tine un ID invalido."); 
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(container,item) VALUES(" + container.getID() + "," + item.getID() + ")";
        //System.out.println(sql);
        Statement stmt = connection.createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return new Return(Return.Status.FAIL,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        ResultSet rs = stmt.getGeneratedKeys();
        int generated = -1;        
        if (rs.next())
        {
            clean();
            id = rs.getInt(1);
            return new Return(Return.Status.DONE, rs.getInt(1));
        }
        else
        {
            id = -1;
            return new Return(Return.Status.FAIL,"No genero ID para el registro.");
        }
    }

    private void clean() 
    {
        id = -1;
        container = null;
        item = null;
    }
    
    public Allocated(int id)
    {
        this.id = id;
    }
    
    public Return upContainer(Connection connection, Container container) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"allocated tine un ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(container == null)
        {
            return new Return(Return.Status.FAIL,"container is null.");
        }
        else if(container.getID() < 1)
        {
            throw new InvalidParameterException("container ID invalido.");
        }            
        this.container = null;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET container = " + container.getID() + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }

    public Return selectLast(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 0, 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            this.id = -1;
            return new Return(Return.Status.DONE);
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return selectRandom(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            this.id = -1;
            return new Return(Return.Status.DONE);
        }
    }
}
