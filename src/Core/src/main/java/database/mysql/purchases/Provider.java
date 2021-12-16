
package database.mysql.purchases;

import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Sucursal;
import SIIL.CN.Tables.PROVEEDO;
import SIIL.Server.Database;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import process.ErrorCodes;
import process.Return;

/**
 * MySQL Avatar
 * @version 0.1
 * @author Azael Reyes
 */
public class Provider 
{
    private final static String MYSQL_AVATAR_TABLE = "PurchasesProvider";
    private int id;
    private String nameShort;
    private String nameRazonSocial;
    private String RFC;
    private String number;
    
    
    public static int isExist(Database database,String number) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE number = '" + number + "'";
        Statement stmt = database.getConnection().createStatement();
        //System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            return rs.getInt(1);
        }
        else
        {
            return -1;
        }
    }
        
    public static Return<Integer> importFromCN(Connection connection) throws IOException, SQLException
    {
        PROVEEDO ordenes = new PROVEEDO(Sucursal.BC_Tijuana);       
        List<DBFRecord> list = ordenes.readAll();
        Provider provider = new Provider(-1);
        Return<Integer> ret = null;
        String rfc = null;
        for(DBFRecord rec : list)
        {
            //System.out.println(rec.getString(0) + "|-|" + rec.getString(1)  + "|-|" + rec.getString(3));
            //System.out.println("Insertando..");
            ret = provider.insert(connection,rec.getString(0).trim(),rec.getString(1));
            if(ret.isFail()) 
            {
                if(ret.getError() == ErrorCodes.ERR_MYSQL_APOSTROPHE)
                {
                    System.err.println(">>>Existe apostrofe, se ignoró " + rec.getString(0).trim());
                }
                else
                {
                    return ret;
                }
            }
            //System.out.println("Getting RFC..");
            rfc = rec.getString(3).trim();
            if(rfc.length() > 9)
            {
                if(Provider.validRFC(rfc))
                {
                    //System.out.println("Inserting RFC..." + rfc);
                    ret = provider.upRFC(connection,rfc);
                    if(ret.isFail()) return ret;
                }
            }
        }
        return new Return<>(true,list.size());
    }
    
    public static List<Provider>  search(Connection connection,String search) throws SQLException
    {
        if(connection == null)
        {
            return null;
        }
        
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE;
        sql = sql + " WHERE  ";
        sql = sql + " nameShort like '%" + search + "%' OR nameRazonSocial like '%" + search + "%' OR rfc like '%"  + search + "%'";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        List<Provider> list = new ArrayList<>();
        while(rs.next())
        {
            Provider prov = new Provider(rs.getInt(1));
            prov.downRFC(connection);
            prov.downNumber(connection);
            prov.downRazonSocial(connection);
            list.add(prov);
        }
        return list;
    }
    
    public static List<Provider> search(Connection connection,String where, String order, int limit,int offset) throws SQLException 
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE ;

        if(where != null)
        {
            sql = sql + " WHERE " + where;
        }
        if(order != null)
        {
            sql = sql + order;
        }
        if(limit > 0)
        {
            sql = sql + " LIMIT " + limit;
            if(offset > 0)
            {
                sql = sql + " OFFSET " + offset;
            }
        }
        //System.out.println(sql);
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        List<Provider> list = new ArrayList<>();
        while(rs.next())
        {
            Provider prov = new Provider(rs.getInt(1));
            prov.downRFC(connection);
            prov.downNumber(connection);
            prov.downRazonSocial(connection);
            list.add(prov);
        }
        return list;
    }
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return downRFC(Connection connection) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT rfc FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            RFC = rs.getString(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            RFC = null;
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + id);
        }  
    }
    
    public Return downRazonSocial(Connection connection) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT nameRazonSocial FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            nameRazonSocial = rs.getString(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            nameRazonSocial = null;
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + id);
        }  
    }
    
    public Return downNameShort(Connection connection) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT nameShort FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            nameShort = rs.getString(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            nameShort = null;
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + id);
        }  
    }
    
    /**
     * Por ID de proveedor
     * @param id 
     */
    public Provider(int id)
    {
        this.id = id;
    }
    
    /**
     * Crea un nuevo proveedor con los datos básicos.
     * @param connection Conexion a base de datos.
     * @param number
     * @param nameRazonSocial Razon Sociaol del proveedor
     * @return
     * @throws SQLException 
     */
    public Return<Integer> insert(Connection connection,String number,String nameRazonSocial) throws SQLException
    {
        if(connection == null || nameRazonSocial == null || number == null)
        {
            return new Return<>(false,"No se permite valores nulos en los parametros");
        }
        if(number.length() < 1)
        {
            return new Return<>(false,"El number del provedor deve de ser de almenos 1 caracteres.");
        }
        else if(number.contains("'"))
        {
            return new Return<>(false,"El caracter \"'\" es no deve formar parte del valor.",ErrorCodes.ERR_MYSQL_APOSTROPHE);            
        }        
        if(nameRazonSocial.length() < 5)
        {
            return new Return<>(false,"La Razón Social del provedor deve de ser de almenos 5 caracteres.");
        }
        else if(nameRazonSocial.contains("'"))
        {
            return new Return<>(false,"El caracter \"'\" es no deve formar parte del valor.",ErrorCodes.ERR_MYSQL_APOSTROPHE);            
        }
        
               
        String sql = "INSERT INTO PurchasesProvider(number,nameRazonSocial) VALUES('" + number + "','" + nameRazonSocial + "')";                
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return new Return(false,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            clean();
            //connection.commit();
            id = rs.getInt(1);
            return new Return(true, rs.getInt(1));
        }
        else
        {
            id = -1;
            //connection.rollback();
            return new Return(false,"No genero ID para el registro.");
        }
    }

    private static boolean validRFC(String RFC) 
    {
        return RFC.matches("[A-Z]{3,6}[0-9]{6}[A-Z|0-9]");
    }

    /**
     * @return the id
     */
    public int getID() 
    {
        return id;
    }

    /**
     * @return the nameShort
     */
    public String getNameShort() {
        return nameShort;
    }

    /**
     * @return the nameRazonSocial
     */
    public String getNameRazonSocial() {
        return nameRazonSocial;
    }

    /**
     * @return the RFC
     */
    public String getRFC() {
        return RFC;
    }

    public boolean selectLast(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM PurchasesProvider ORDER BY id DESC LIMIT 0, 1";
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

    private void clean() {
        RFC = null;
        id = -1;
        nameRazonSocial = null;
        nameShort = null;
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

    public Return<Integer> upRFC(Connection connection,String rfc) throws SQLException 
    {
        if(id < 1)
        {
            return new Return<>(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return<>(false,"connection is null");
        }
        if(rfc == null)
        {
            return new Return<>(false,"rfc is null.");
        }
        if(validRFC(rfc))
        {
            return new Return<>(false,"formato de RFC incorrecto.");
        }
        this.RFC = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET rfc='" + rfc + "' WHERE id=" + id;
        Statement stmt = connection.createStatement();
        System.out.println(sql);
        int affected = stmt.executeUpdate(sql);
        if(affected != 1)
        {
            return new Return<>(false,"Cantida de registro incorrectos : " + affected);
        }
        return new Return<>(true, affected);
    }

    private Return<Integer> downNumber(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT number FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            number = rs.getString(1);
            return new Return<>(true);
        }
        else
        {
            number = null;
            return new Return(false,"No se encontro nigun registro para el ID = " + id);
        }  
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }
}
