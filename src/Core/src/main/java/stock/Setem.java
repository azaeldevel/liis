
package stock;

import database.mysql.stock.Item;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;

/**
 * MySQL Avatar
 * @version 1.0.0
 * @author Azael Reyes
 */
public class Setem extends Item
{    
    private final String MYSQL_AVATAR_TABLE = "StockSetem";
    
    public Setem(int idItem) 
    {
        super(idItem);
    }  
    
    /**
     * Inserta un nuevo numero de parte en el catalogo.
     * @param connection
     * @param number
     * @return Id de registro creado.
     * @throws java.sql.SQLException Lanzada desde el engine
     */
    public Return<Integer> insert(Connection connection, String number,String description,boolean activeSerie,String serie) throws SQLException
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        Return ret = super.insert(connection, number,description,Item.Type.ARTICULO,activeSerie,serie);
        if(ret.isFail()) return ret;
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id) VALUES(" + super.getID() + ")";
        Statement stmt = connection.createStatement();
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return<>(false,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        return new Return<>(true, super.getID());
    }  

    /**
     * 
     */
    private void clean() 
    {
        ;
    }
}
