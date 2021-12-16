
package SAT;

import SIIL.Server.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Azael Reyes
 */
public class Clave 
{
    private static final String MYSQL_AVATAR_TABLE = "SATClave";
    
    private int id;
    private String clave;
    private String nombre;
    private String decripcion;
    private Date vigenciaInicio;
    private Date vigenciaFin;


    public boolean upVigenciaFin(Database db, Date vigenciaFin) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        this.vigenciaFin = null;
        SimpleDateFormat currentLocale = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET vigenciaFin='" + currentLocale.format(vigenciaFin) + "' WHERE id=" + id;
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
    
    public boolean upVigenciaInicio(Database db, Date vigenciaInicio) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        this.vigenciaInicio = null;
        SimpleDateFormat currentLocale = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET vigenciaInicio='" + currentLocale.format(vigenciaInicio) + "' WHERE id=" + id;
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
    
    public boolean upDecripcion(Database db, String decripcion) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        this.decripcion = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET decripcion='" + decripcion + "' WHERE id=" + id;
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
    
    public boolean insert(Database dbserver,String clave,String nombre,String descripcion) throws SQLException
    {
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (clave,nombre) VALUES('" + clave + "','" + nombre + "')";
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
            return true;
        }
    }
    
    public  Clave(int id)
    {
        this.id = id;
    }
    
    public static List<Clave> listing(Database dbserver) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY nombre ASC";
        ResultSet rs = dbserver.query(sql);
        ArrayList<Clave> arr = new ArrayList<>();
        while(rs.next())
        {
            Clave clave = new Clave(rs.getInt(1));
            if(clave.download(dbserver)) arr.add(clave);
        }
        
        return arr;
    }
    
    public boolean download(Database dbserver) throws SQLException
    {
        String sql = "SELECT clave,nombre,decripcion,vigenciaInicio,vigenciaFin " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            clave = rs.getString(1);
            nombre = rs.getString(2);
            decripcion = rs.getString(3);
            vigenciaInicio = rs.getDate(4);
            vigenciaFin = rs.getDate(5);
            return true;
        }
        else
        {
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
     * @return the decripcion
     */
    public String getDecripcion() 
    {
        return decripcion;
    }

    /**
     * @return the vigenciaInicio
     */
    public Date getVigenciaInicio() 
    {
        return vigenciaInicio;
    }

    /**
     * @return the vigenciaFin
     */
    public Date getVigenciaFin() 
    {
        return vigenciaFin;
    }

    /**
     * @return the nombre
     */
    public String getNombre() 
    {
        return nombre;
    }
    
    
}
