
package SAT;

import SIIL.Server.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Azael Reyes
 */
public class Catalogo 
{
    private static final String MYSQL_AVATAR_TABLE = "SATCatalogo";
    
    private int id;
    private String clave;
    private String nombre;
    
    
    public boolean upNombre(Database db, String nombre) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        this.nombre = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET clave='" + nombre + "' WHERE id=" + id;
        Statement stmt = db.getConnection().createStatement();
        if(stmt.executeUpdate(sql) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    public boolean upClave(Database db, String clave) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        this.clave = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET clave='" + clave + "' WHERE id=" + id;
        Statement stmt = db.getConnection().createStatement();
        if(stmt.executeUpdate(sql) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    } 
    
    public  Catalogo(int id)
    {
        this.id = id;
    }
    
    public static List<Catalogo> listing(Database dbserver) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY nombre ASC";
        ResultSet rs = dbserver.query(sql);
        ArrayList<Catalogo> arr = new ArrayList<>();
        while(rs.next())
        {
            Catalogo catalogo = new Catalogo(rs.getInt(1));
            if(catalogo.download(dbserver)) arr.add(catalogo);
        }
        
        return arr;
    }
    
    public boolean download(Database dbserver) throws SQLException
    {
        String sql = "SELECT clave,nombre " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            clave = rs.getString(1);
            nombre = rs.getString(2);
            return true;
        }
        else
        {
            return false;
        }        
    }
    
    public boolean insert(Database dbserver,String clave,String nombre) throws SQLException
    {
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (clave,nombre) VALUES('" + clave + "','" + nombre + "')";
        Statement stmt = dbserver.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return false;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if(rs.next())
        {
            id = rs.getInt(1);
            return true;
        }
        else
        {
            id = -1;
            return true;
        }
    }
    
    /**
     * @return the id
     */
    public int getId() 
    {
        return id;
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
    
}
