
package database.mysql.stock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class Service extends Item
{
    public static final String MYSQL_AVATAR_TABLE = "StockService";
        
    public static Return<Integer> isExist(Connection connection, String number)throws SQLException
    {
        String sql = "SELECT id FROM " + Item.MYSQL_AVATAR_TABLE + " WHERE number = '" + number + "'";
        Statement stmt = connection.createStatement();
        ResultSet  rs =stmt.executeQuery(sql);
        if(rs.next())
        {
            sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + rs.getInt(1);
            if(rs.next())
            {
                return new Return<>(true,rs.getInt(1));
            }
            else
            {
                return new Return<>(false,-1);
            }
        }
        else
        {
            return new Return<>(false,-1);
        }
    }
    
    /**
     * Inserta un nuevo numero de parte en el catalogo.
     * @param connection
     * @param number
     * @param description
     * @return Id de registro creado.
     * @throws java.sql.SQLException Lanzada desde el engine
     */
    public int insert(Connection connection, String number,String description,boolean activeSerie,String serie) throws SQLException
    {
        if(connection == null)
        {
            return -1;
        }
        Return ret = super.insert(connection, number,description,Item.Type.SERVICIO,activeSerie,serie);
        if(ret.isFail()) return -1;
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id) VALUES(" + super.getID() + ")";
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(sql);        
    } 

    public Service(int idItem) 
    {
        super(idItem);
    }

}
