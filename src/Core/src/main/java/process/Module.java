
package process;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * MySQL Avatar
 * @version 0.1
 * @author Azael Reyes
 */
public class Module 
{
    private final String MYSQL_AVATAR_TABLE = "ProcessModule";
    public enum Type
    {
        COTIZACION_SERVICIO,
        RELACION_TRABAJO
    }
    //
    private int id;
    private String code;
    private String name;

    /**
     * 
     * @param connection
     * @param code
     * @return
     * @throws SQLException 
     */
    public Return select(Connection connection,String code) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT id,code,name FROM " + MYSQL_AVATAR_TABLE + " WHERE code = '" + code + "'";
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            this.code = rs.getString(2);
            this.name = rs.getString(3);
            return new Return(Return.Status.DONE);
        }
        else
        {
            clean();
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + id);
        }
    }

    /**
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
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

    private void clean() 
    {
        this.code = null;
        this.id = -1;
        this.name = null;
    }
    
    public Module(int id)
    {
        this.id = id;
    }
}
