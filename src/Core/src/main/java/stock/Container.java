
package stock;

import SIIL.Server.Database;
import administracion.Building;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import process.Return;

/**
 * MySQL Avatar
 * @version 0.1
 * @author Azael Reyes
 */
public class Container 
{
    public static final String MYSQL_AVATAR_TABLE = "StockContainer";
    
    private int id;
    private String name;
    private administracion.Building building;
    private Container parent;

    @Override
    public String toString()
    {
        if(id == -1000)
        {
            return "Seleccione...";
        }
        else if(building != null && name != null)
        {
            return getFullName();
        }
        else
        {
            return "";
        }        
    }
    
    public static ArrayList<Container> listing(Database dbserver) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY name ASC";
        ResultSet rs = dbserver.query(sql);
        ArrayList<Container> list = new ArrayList<>();
        while(rs.next())
        {
            Container container = new Container(rs.getInt(1));
            container.download(dbserver);
            list.add(container);
        }
        return list;
    }
    
    public String getFullName()
    {
        if(parent != null)
        {
            return name + "." + parent.getFullName();
        }
        else
        {
            return name + " - " + building.getName();
        }
    }
    
    public boolean download(Database dbserver) throws SQLException
    {
        String sql = "SELECT id,name,building,parent FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            name = rs.getString(2);
            if(rs.getInt(3) > 0) 
            {
                building = new Building(rs.getInt(3));
                building.download(dbserver);
            }
            if(rs.getInt(4) > 0)
            {
                parent = new Container(rs.getInt(4));
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
    
    public Container(int id)
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the build
     */
    public administracion.Building getBuilding() {
        return building;
    }

    /**
     * @return the parent
     */
    public Container getParent() {
        return parent;
    }
    
    public Return insert(Connection connection,String name,administracion.Building build,Container parent) throws SQLException
    {        
        if(id > 0)
        {
            return new Return(Return.Status.FAIL,"ID tiene valor > 0");
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(name == null)
        {
            return new Return(Return.Status.FAIL,"name is null.");
        }
        else if(name.length() < 5)
        {
            return new Return(Return.Status.FAIL,"name tien menos de 5 caracteres.");
        }
        if(build == null)
        {
            return new Return(Return.Status.FAIL,"build is null.");
        }
        else if(build.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"build tiene un ID invalido.");
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(name,building";
        if(parent == null)
        {
            sql = sql + ") VALUES('" + name + "'," + build.getID() + ")";
        }
        else
        {
            sql = sql + ",parent) VALUES('" + name + "'," + build.getID() + "," + parent.getID()  + ")";
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
        building = null;
        id = -1;
        name = null;
        parent = null;
    }
    
    public Return upBuilding(Connection connection, administracion.Building building) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"container tine un ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(building == null)
        {
            return new Return(Return.Status.FAIL,"building is null.");            
        }
        else if(building.getID() < 1)
        {
            throw new InvalidParameterException("building tine un ID invalido."); 
        }        
        this.building = null;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET building = " + building.getID() + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }
    
    public Return upParent(Connection connection, Container container) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"container tine un ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(container == null)
        {
            return new Return(Return.Status.FAIL,"container is null.");
        }
        this.parent = null;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET parent = " + container.getID() + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }

    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public int selectLast(Connection connection) throws SQLException 
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
        }
        else
        {
            this.id = -1;
        }
        
        return this.id;
    }
    

    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> selectRandom(Connection connection) throws SQLException 
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
            return new Return<>(true);
        }
        else
        {
            return new Return<>(false);
        }
    }
}
