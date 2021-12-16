
package administracion;

import SIIL.Server.Database;
import SIIL.core.Office;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;

/**
 * MySQL Avatar
 * @version 0.1
 * @author Azael Reyes
 */
public class Building 
{
    public final String MYSQL_AVATAR_TABLE = "AdministracionBuilding";
    
    private int id;
    private Office office;
    private String name;
    private Building parent;
        
    
    public String getFullName()
    {
        String nm = name;
        
        if(parent != null)
        {
            if(parent.getParent() != null)
            {
                return nm + parent.getFullName();
            }
        }
        
        return nm;
    }
    
    
    public boolean download(Database dbserver) throws SQLException
    {
        String sql = "SELECT id,office,name,parent FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            if(rs.getInt(2) > 0)
            {
                office = new Office(rs.getInt(2));
                office.download(dbserver.getConnection());
            }
            else
            {
                office = null;
            }
            name = rs.getString(3);
            if(rs.getInt(4) > 0)
            {
                parent = new Building(rs.getInt(4));
                parent.download(dbserver);
            }
            else
            {
                parent = null;
            }
            
            return true;
        }
        
        return false;
    }
    
    
    public Return insert(Connection connection,String name,Office office,Building parent) throws SQLException
    {
        if(id > 0)
        {
            return new Return(Return.Status.FAIL,"ID > 0");
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is NULL");
        }
        if(name == null)
        {
            return new Return(Return.Status.FAIL,"name is NULL");
        }
        if(office == null)
        {
            return new Return(Return.Status.FAIL,"Office is NULL");
        }
        else if(office.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"ID de Office is invalid.");
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(name,office";
        if(parent == null)
        {
            sql = sql + ") VALUES('" + name + "'," + office.getID() + ")";
        }
        else
        {
            sql = sql + ",parent) VALUES('" + name + "'," + office.getID() + "," + parent.getID() + ")";
        }
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
            return new Return(Return.Status.DONE,new Integer(rs.getInt(1)));
        }
        else
        {
            id = -1;
            return new Return(Return.Status.FAIL,"No genero ID para el registro.");
        }
    }
    /**
     * 
     * @param id 
     */
    public Building(int id)
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
     * @return the office
     */
    public Office getOffice() {
        return office;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the parent
     */
    public Building getParent() {
        return parent;
    }    

    private void clean() 
    {
        id = -1;
        name = null;
        office = null;
        parent = null;
    }

    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public int selectRandom(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
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
}
