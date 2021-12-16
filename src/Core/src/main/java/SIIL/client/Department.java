
package SIIL.client;

import SIIL.Server.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Azael
 */
public class Department 
{
    private Integer id;
    public String code;
    public String name;
    
    public Department(String code)
    {
        this.code = code;
    }

    public Department(String code, String name) 
    {
        this.code = code;
        this.name = name;
    }

    public Department(Database db, Integer id) 
    {
        this.id = id;
        Throwable th = download(db);
    }

    private Throwable download(Database db) 
    {
        String sql = "SELECT code,name FROM Departments WHERE id = " + id;
        try 
        {
            Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                this.code = rs.getString(1);
                this.name = rs.getString(2);
                return null;
            }            
            return new Exception("Falló la descarga de atributos para la departamento con id " + id);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
            return new Exception("Falló la descarga de atributos para la departamento con id " + id, ex);
        }
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
