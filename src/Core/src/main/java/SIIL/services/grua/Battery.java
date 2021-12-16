
package SIIL.services.grua;

import SIIL.Server.Database;
import database.mysql.stock.Titem;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;

/**
 *
 * @author Azael Reyes
 */
public class Battery extends database.mysql.stock.Titem
{
    public static final String MYSQL_AVATAR_TABLE = "Battery";  

    
    /**
     * Asigna un ID al azar
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return search(Connection connection, String number) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE number = '" + number + "'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            super.id = rs.getInt(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            super.id = -1;
            return new Return(Return.Status.DONE);
        }
    }
    
    public static boolean isBattery(Database database, Titem titem) throws SQLException 
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + titem.getID();
        Statement stmt = database.getConnection().createStatement();
        //System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            return true;
        }
        else
        {
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
        
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE id > 0 ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            super.id = rs.getInt(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            super.id = -1;
            return new Return(Return.Status.DONE);
        }
    }
    
    /**
     * Inserta un nuevo numero de parte en el catalogo.
     * @param connection
     * @param number
     * @param description
     * @param backward tru para activar el soporte con Grua 1.0
     * @param activeSerie
     * @param serie
     * @return Id de registro creado.
     * @throws java.sql.SQLException Lanzada desde el engine
     */
    @Override
    public Return<Integer> insert(Connection connection, String number,String description,Import backward,boolean activeSerie,String serie) throws SQLException
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        Return ret = super.insert(connection, number,description,backward,activeSerie,serie);
        if(ret.isFail()) return ret;
        Statement stmt = connection.createStatement();
        String sqlHequi = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(id,BD,number) VALUES(" + super.getID() + ",'" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + number + "')";
        //System.out.println(sqlHequi);
        int affectedHequi = 0;
        if(backward == Import.UpdateTitemFromStocK | backward == Import.CreateSotckUpdateTitem)
        {
            String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET id = " + super.getID() + " WHERE BD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' AND number = '" + number + "'";
            affectedHequi = stmt.executeUpdate(sql);
        }
        else
        {
            affectedHequi = stmt.executeUpdate(sqlHequi);
        }
        if(affectedHequi != 1)
        {
            return new Return<>(false,"Se afectaron '" + affectedHequi + "' registro(s) lo cual es incorrecto.");
        }
        return new Return<>(true, super.getID());
    }
    
    public Battery(int idItem) 
    {
        super(idItem);
    }    

    private void clean() 
    {
    }
}
