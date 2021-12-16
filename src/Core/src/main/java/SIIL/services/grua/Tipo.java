
package SIIL.services.grua;

import SIIL.Server.Database;
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
public class Tipo 
{
    private static final String MYSQL_AVATAR_TABLE = "GruaType";
    
    private int id;
    private String code;
    private String label;
    
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
    
    public static List<Tipo> listing(Database db) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY id ASC";
        Tipo tipo;
        List<Tipo> ls = new ArrayList<>();
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            tipo = new Tipo(rs.getInt(1));
            ls.add(tipo);
        }
        return ls;
    }
    
    public Tipo(int id)
    {
        this.id = id;
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> selectRandom(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return<Integer>(false,"Connection is null.");
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
            this.id = -1;
            return new Return<>(false);
        }
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

    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> download(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Connection is null.");
        }
        String sql = "SELECT code,label FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            code = rs.getString(1);
            label = rs.getString(2);
            return new Return<>(true);
        }
        else
        {
            return new Return<>(false,"Falló sql : " + sql);
        }   
    }

    /**
     * @return the id
     */
    public int getID() {
        return id;
    }

    private void clean() 
    {
        id = -1;
    }

}
