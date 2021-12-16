
package process;

import SIIL.Server.Database;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jxl.Sheet;

/**
 *
 * @author Azael Reyes
 */
public class SATClaves 
{
    private int catalog;
    private String clave;
    private String nombre;
    private String description;

    private static final String MYSQL_AVATAR_TABLE = "SATClaves";
    
    public boolean selectLast(Database connection) throws SQLException 
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
            this.catalog = rs.getInt(1);
            return true;
        }
        else
        {
            this.catalog = -1;
            return false;
        }
    }
    
    public boolean insert(Database database, SATCatalogo catalogo, String clave, String nombre, String descripcion) throws SQLException
    {
        if(database == null)
        {
            return false;
        }
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (catalog,clave,nombre,descripcion) VALUES(" + catalogo.getID() + ",'" + clave + "'";
        if(nombre != null)
        {
            sql += ",'" + nombre + "'";
        }
        else
        {
            sql += ",NULL";
        }
        if(descripcion != null)
        {
            sql += ",'" + descripcion + "')";
        }
        else
        {
            sql += ",NULL)";
        }
        Statement stmt = database.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return false;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            catalog = rs.getInt(1);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    public static boolean importFormaPago(Database database,SATCatalogo catalogo, Sheet sheet) throws SQLException
    {
        //insersetar
        for(int i = 7; i < 28; i++)
        {
            SATClaves claves = new SATClaves();
            if(claves.insert(database, catalogo, sheet.getCell(0, i).getContents(), null , sheet.getCell(1, i).getContents()) == false);
            {
                return false;
            }
        }
        
        return true;
    }
    
    
    /**
     * @return the catalog
     */
    public int getCatalog() {
        return catalog;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    
}
