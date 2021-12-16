
package SIIL.services.grua;

import SIIL.Server.Database;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import process.Return;

/**
 *
 * @author Azael
 */
public class Uso 
{
    private static final String MYSQL_AVATAR_TABLE = "GruaUso";
    
    private String code;
    private String label;
    private int id;

    
    public boolean selectCode(Database connection,String code) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE code = '" + code + "'";
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            id = rs.getInt(1);
            return true;
        }
        else
        {
            return false;
        }   
    }
    
    @Override
    public String toString()
    {
        if(id > 0)
        {            
            return label;
        }
        else if(id == -1000)
        {
            return "Seleccione...";//usado en el combobox
        }
        else
        {
            return null;
        }
    }
    
    public Uso(int id)
    {
        this.id = id;
    }
    
    public static List<Uso> listing(Database db) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY id ASC";
        Uso uso;
        List<Uso> ls = new ArrayList<>();
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            uso = new Uso(rs.getInt(1));
            ls.add(uso);
        }
        return ls;
    }
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public Return download(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT code,label,id FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getID();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            code = rs.getString(1);
            label = rs.getString(2);
            id = rs.getInt(3);
            return new Return(Return.Status.DONE);
        }
        else
        {
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + getID());
        }   
    }
    

    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean selectLast(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 0, 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return true;
        }
        else
        {
            this.id = -1;
            return false;
        }
    }
    
    /**
     * Asigna un ID al azar
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
        id = -1;
        code = null;
        label = null;
    }

    /**
     * @return the id
     */
    public int getID() {
        return id;
    }
    
}
