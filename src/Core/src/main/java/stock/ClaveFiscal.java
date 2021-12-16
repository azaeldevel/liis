
package stock;

import SIIL.Server.Database;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;

/**
 *
 * @author Azael Reyes
 */
public class ClaveFiscal 
{
    private static final String MYSQL_AVATAR_TABLE = "StockClaveFiscal";
    
    private int id;
    private String clave;
    private String descripcion;

    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public int selectLast(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 0, 1";
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
        }
        else
        {
            this.id = -1;
        }
        
        return this.id;
    }
    
    public boolean upDescripcion(Database dbserver, String descripcion) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(descripcion == null)
        {
            return false;            
        }
        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET descripcion = '" + descripcion + "' WHERE id = " + id;
        Statement stmt = dbserver.getConnection().createStatement();
        stmt.executeUpdate(sql);
        return true;
    }
    
    public boolean upClave(Database dbserver, String clave) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(clave == null)
        {
            return false;            
        }
        
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET clave = '" + clave + "' WHERE id = " + id;
        Statement stmt = dbserver.getConnection().createStatement();
        stmt.executeUpdate(sql);
        return true;
    }

    public boolean insert(Database dbserver,String clave,String descripcion) throws SQLException
    {
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (clave,descripcion) VALUES('" + clave + "','" + descripcion + "')";
        Statement stmt = dbserver.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return false;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            id = rs.getInt(1);
            return true;
        }
        else
        {
            id = -1;
            return false;
        }
    }
    
    
    /**
     * @return the id
     */
    public int getID() 
    {
        return id;
    }

    /**
     * @return the clave
     */
    public String getClave() 
    {
        return clave;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() 
    {
        return descripcion;
    }
    
    
}
