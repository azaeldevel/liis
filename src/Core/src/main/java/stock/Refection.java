
package stock;

import database.mysql.stock.Item;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;

/**
 * MySQL Avatar
 * @version 1.0.0
 * @author Azael Reyes
 */
public class Refection extends Item
{
    public static final String MYSQL_AVATAR_TABLE = "StockRefection";
    private static final String MYSQL_AVATAR_TABLE_CROSS = "StockCross";
    
    
    /**
     * Retorna tru si la refaccion esta dad de alta.
     * @param connection
     * @param number
     * @return 
     * @throws java.sql.SQLException 
     */
    public static Return<Integer> isExist(Connection connection, String number) throws SQLException 
    {
        Return<Integer> ret = Item.isExist(connection,number);
        if(ret.isFail())return ret;
        
        String sql = "SELECT idItem FROM " + MYSQL_AVATAR_TABLE + " WHERE idItem = " + (ret.getParam());
        Statement stmt = connection.createStatement();
        ResultSet  rs =stmt.executeQuery(sql);
        if(rs.next())
        {
            //System.out.println("refection.isExist " + number);
            return ret;
        }
        else
        {
            //System.out.println("refection.isExist no " + number);
            return new Return<>(false,-1);
        }
    }
    
    /**
     * 
     * @param idItem 
     */
    public Refection(int idItem)
    {
        super(idItem);
    }
    
    /**
     * Inserta un nuevo número de parte en el catalogo.
     * @param connection
     * @param number
     * @param description
     * @return Id de registro creado.
     * @throws java.sql.SQLException Lanzada desde el engine
     */
    public Return<Integer> insert(Connection connection, String number,String description,boolean activeSerie,String serie) throws SQLException
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        Return ret = super.insert(connection, number,description,Item.Type.REFECTION,activeSerie,serie);
        if(ret.isFail()) return ret;
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(idItem) VALUES(" + super.getID() + ")";
        Statement stmt = connection.createStatement();
        System.out.println(sql);
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return<>(false,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        //connection.commit();
        return new Return<>(true, super.getID());
    }

    /**
     * 
     */
    protected void clean() 
    {
    }

    /**
     * Agrega un nuevo cruse a la refacción.
     * @param connection
     * @param refection
     * @param priority
     * @return 
     * @throws java.sql.SQLException 
     */
    public Return addCross(Connection connection, Refection refection,int priority) throws SQLException
    {
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(refection == null)
        {
            throw new InvalidParameterException("refection is null.");
        }
        else if(refection.getID() < 1)
        {
            throw new InvalidParameterException("El ID de la refacion es invalido.");            
        }
        
        Return exist = existCross(connection,refection,priority);
        if(exist.getStatus() == Return.Status.DONE)
        {
            return new Return(Return.Status.FAIL, "El número de cruse ya existe.",exist);
        }
        else
        {
            return insertCross(connection,refection,priority);
        }
    }

    /**
     * Verifica si existe el cruse indicado.
     * @param connection
     * @param refection
     * @param priority
     * @return 
     * @throws java.sql.SQLException 
     */
    public Return existCross(Connection connection, Refection refection, int priority) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(refection == null)
        {
            return new Return(Return.Status.FAIL,"refection is null.");
        }
        else if(refection.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"El ID de la refacion es invalido.");            
        }
        
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE_CROSS + " WHERE refection = " + getID() + " AND `cross` = " + refection.getID();
        //System.out.println(sql);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            return new Return(Return.Status.DONE);
        }
        else
        {
            return new Return(Return.Status.FAIL);
        }
    }

    /**
     * 
     * @param connection
     * @param refection
     * @param priority
     * @return 
     */
    private Return insertCross(Connection connection, Refection refection, int priority) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(refection == null)
        {
            return new Return(Return.Status.FAIL,"refection is null.");
        }
        else if(refection.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"El ID de la refacion es invalido.");            
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE_CROSS + "(refection,`cross`,priority) VALUES(" + getID() + "," + refection.getID() + "," + priority + ")";
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return new Return(Return.Status.FAIL,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        ResultSet rs = stmt.getGeneratedKeys();        
        if (rs.next())
        {
            return new Return(Return.Status.DONE,new Integer(rs.getInt(1)));
        }
        else
        {
            return new Return(Return.Status.FAIL,"No genero ID para el registro.");
        }
    }
    

    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    @Override
    public Return selectRandom(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT idItem FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            super.setID(rs.getInt(1));
            return new Return(Return.Status.DONE);
        }
        else
        {
            super.setID(-1);
            return new Return(Return.Status.DONE);
        }
    }
}
