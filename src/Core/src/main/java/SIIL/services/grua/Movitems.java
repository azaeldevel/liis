
package SIIL.services.grua;

import SIIL.Server.Database;
import core.PlainTitem;
import database.mysql.stock.Titem;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import process.Return;
import stock.Flow;

/**
 *
 * @author Azael Reyes
 */
public class Movitems 
{
    private static final String MYSQL_AVATAR_TABLE = "Movtitems";
    private static final String MYSQL_AVATAR_TABLE_BACKWARD_BD = "bc.tj";

    
    /**
     * 
     * @param connection
     * @param mov
     * @param hequis
     * @return
     * @throws SQLException 
     */
    public int insert(Database connection, Movements mov, PlainTitem hequis) throws SQLException
    {
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (mov";
        if(hequis.number != null)
        {
            sql += ",numeco";
        }
        if(hequis.make != null)
        {
            sql += ",marca";
        }
        if(hequis.model != null)
        {
            sql += ",modelo";
        }
        if(hequis.serie != null)
        {
            sql += ",serie";
        }
        sql += ") VALUES(" + mov.getID();
        if(hequis.number != null)
        {
            sql += ",'" + hequis.number + "'";
        }
        if(hequis.make != null)
        {
            sql += ",'" + hequis.make + "'";
        }
        if(hequis.model != null)
        {
            sql += ",'" + hequis.model + "'";
        }
        if(hequis.serie != null)
        {
            sql += ",'" + hequis.serie + "'";
        }
        sql += ")";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        return stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return selectLast(Connection connection) throws SQLException 
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
            return new Return(Return.Status.DONE);
        }
        else
        {
            return new Return(Return.Status.DONE);
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
            return new Return(Return.Status.DONE);
        }
        else
        {
            return new Return(Return.Status.DONE);
        }
    }
    
    public Return insert(Database connection,List<Flow> hequis,Movements mov) throws SQLException
    {        
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(hequis == null)
        {
            return new Return(Return.Status.FAIL,"hequis is null.");
        }
        else if(hequis.size() < 1)
        {
            return new Return(Return.Status.FAIL,"hequis is una lista vacia");
        }
        if(mov == null)
        {
            return new Return(Return.Status.FAIL,"mov is null.");
        }
        else if(mov.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"el ID del movimiento es invalido");
        }
        
        for(Flow hequi: hequis)
        {
            Return ret = insert(connection, hequi, mov);
            if(ret.getStatus() == Return.Status.FAIL) return  ret;
            
        }
        
        return new Return(Return.Status.DONE);
    }

    private Return insert(Database connection, Flow hequi, Movements mov) throws SQLException 
    {        
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(hequi == null)
        {
            return new Return(Return.Status.FAIL,"hequi is null.");
        }
        else if(hequi.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"el folio de hequi es invalido");
        }
        if(mov == null)
        {
            return new Return(Return.Status.FAIL,"mov is null.");
        }
        else if(mov.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"el ID del movimiento es invalido");
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (mov,numeco";
        if(hequi.getItem().getMake() != null)
        {
            sql = sql + ",marca";
        }
        if(hequi.getItem().getModel() != null)
        {
            sql = sql + ",modelo";
        }
        if(hequi.getSerie() != null)
        {
            sql = sql + ",serie";
        }
        if(Forklift.isForklift(connection, hequi.getItem()))
        {
            Forklift forklift = (Forklift) hequi.getItem();
            if(forklift.horometro > 0)
            {
                sql = sql + ",horometro";
            }
        }
        sql = sql + ") VALUES(" + mov.getID() + ",'" + hequi.getItem().getNumber() + "'";
        if(hequi.getItem().getMake() != null)
        {
            sql = sql + ",'" + hequi.getItem().getMake() + "'";
        }
        if(hequi.getItem().getModel() != null)
        {
            sql = sql + ",'" + hequi.getItem().getModel() + "'";
        }
        if(hequi.getSerie() != null)
        {
            sql = sql + ",'" + hequi.getSerie() + "'";
        }
        if(Forklift.isForklift(connection, hequi.getItem()))
        {
            Forklift forklift = (Forklift) hequi.getItem();
            if(forklift.horometro > 0)
            {
                sql = sql + "," + forklift.horometro;
            }
        }
        sql = sql + ")";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);         
        if(Forklift.isForklift(connection, hequi.getItem()))
        {
            SIIL.services.grua.Forklift forklift = (SIIL.services.grua.Forklift) hequi.getItem();
            if(forklift.battery != null)
            {
                sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (mov,numeco,marca,modelo,serie) VALUES(" + mov.getID() + ",'" + forklift.battery.getNumber() + "','" + forklift.battery.getMake() + "','" + forklift.battery.getModel() + "','?')" ;
                stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            }
            if(forklift.charger != null)
            {
                sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (mov,numeco,marca,modelo,serie) VALUES(" + mov.getID() + ",'" + forklift.charger.getNumber() + "','" + forklift.charger.getMake() + "','" + forklift.charger.getModel() + "','?')" ;
                stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            }
            if(forklift.mina != null)
            {
                sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (mov,numeco,marca,modelo,serie) VALUES(" + mov.getID() + ",'" + forklift.mina.getNumber() + "','" + forklift.mina.getMake() + "','" + forklift.mina.getModel() + "','?')" ;
                stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            }
        }
        if(affected != 1)
        {
            return new Return(Return.Status.FAIL,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            return new Return(Return.Status.DONE, rs.getInt(1));
        }
        else
        {
            return new Return(Return.Status.FAIL,"No genero ID para el registro.");
        }
    }

    private void clean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
