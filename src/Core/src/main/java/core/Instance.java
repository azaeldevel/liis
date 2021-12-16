
package core;

import SIIL.Server.Database;
import session.User;
import SIIL.core.Exceptions.DatabaseException;
import SIIL.core.Exceptions.ValidationStateInputException;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Identifica una instancia en el lado del cliente.
 * @author Azael Reyes
 */
public class Instance implements Serializable
{
    private final String MYSQL_AVATAR_TABLE = "Instances";
    private String address;
    private User user;
    private String hascode;
    int ID;
    
    public boolean download(Database db) throws SQLException
    {
        String sql = "SELECT user,address,hascode,status,desde FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + ID ;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            user = new User(rs.getString(1));
            user.fill(db, rs.getString(1), "bc.tj");
            address = rs.getString(2);
            hascode = rs.getString(3);
            return true;
        }
        return false;
    }
    
    public Instance(int id)
    {
        ID = id;
    }
        
    public Instance()
    {
        ;
    }
    
    /**
     * Crea una identificacion para registrar una conexion nueva.
     * @param user
     * @throws UnknownHostException Si no se puede determinar el IP de la maquina.
     */
    public Instance(User user) throws UnknownHostException
    {
        address = Inet4Address.getLocalHost().getHostAddress();
        Random rn = new Random();
        this.user = user;
        hascode = String.valueOf(this.hashCode());
    }

    /**
    * @return the address
    */
    public String getAddress() 
    {
        return address;
    }

    /**
         * @return the user
         */
    public User getUser() 
    {
        return user;
    }

    public String getHasCode() 
    {
        return hascode;
    }
    
    /**
     * Retorna un arreglo de todos los registros correspondientes al usuario.
     * @param user
     * @param dbserver
     * @return null si algun error ocurre, entro caso enviara un intancia valida 
     * correspondiente al numero de registros encontrados.
     */
    public static ArrayList<Instance> select(User user, Database dbserver) throws DatabaseException 
    {
        ArrayList<Instance> instances = new ArrayList<>();
        String sql = "SELECT ID FROM Instances WHERE user = '" + user.getAlias() + "'";
        try 
        {
            Statement stmt = dbserver.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                Instance instance = new Instance();
                try 
                {
                    instance.fill(rs.getInt(1),dbserver);
                } 
                catch (DatabaseException ex) 
                {
                    throw new DatabaseException("Ocurrio un error mientras se rellenaban el arreglo.",ex);
                }
                instances.add(instance);
            }
            return instances;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Agrega un registro para la instancia indicada.
     * @param id Identificador de instancia.
     * @param hasCode Has Code de la instancia indicada
     * @param dbserver
     * @return 
     * @throws SIIL.core.Exceptions.ValidationStateInputException
     * @throws SIIL.core.Exceptions.DatabaseException
     */
    public static int insert(Instance id, String hasCode,Database dbserver) throws ValidationStateInputException, DatabaseException, SQLException
    {
        if(id == null)
        {
            throw new ValidationStateInputException("id no puede ser null.");
        }
        if(id.address == null)
        {
            throw new ValidationStateInputException("address no puede ser null.");
        }
        if(id.hascode == null)
        {
            throw new ValidationStateInputException("hascode no puede ser null.");
        }
        if(id.user == null)
        {
            throw new ValidationStateInputException("user no puede ser null.");
        }
        String userAlias = id.user.getAlias();
        if(userAlias == null)
        {
            throw new ValidationStateInputException("user.alias no puede ser null.");
        }
        
        String sql = "INSERT INTO Instances(address,user,status,desde,hascode) VALUES(?,?,?,?,?)";
        PreparedStatement stmt = null;
        try 
        {
            stmt = dbserver.getConnection().prepareStatement(sql);
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló prepareStatement para " + sql, ex);
        }
        try 
        {
            stmt.setString(1, id.getAddress());
            stmt.setString(2, id.getUser().getAlias());
            stmt.setString(3, "run");
            stmt.setDate(4, dbserver.getDateToday());
            stmt.setString(5, id.getHasCode());
        }  
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló la asignacion de parametro para instruccion prepareStatement", ex);
        }

       return stmt.executeUpdate();
    }

    /**
     * Retorna el registro correspondiente a la instacia indicada.
     * @param id Identificador de instancia.
     * @param dbserver
     * @return retorna la instacia pasada como parametro si esta registrada null de otroa forma.
     * @throws SIIL.core.Exceptions.ValidationStateInputException 
     * @throws SIIL.core.Exceptions.DatabaseException 
     */
    public static Instance select(Instance id, Database dbserver) throws ValidationStateInputException, DatabaseException 
    {
        if(id == null)
        {
            throw new ValidationStateInputException("id no puede ser null.");
        }
        if(id.address == null)
        {
            throw new ValidationStateInputException("address no puede ser null.");
        }
        if(id.hascode == null)
        {
            throw new ValidationStateInputException("hascode no puede ser null.");
        }
        if(id.user == null)
        {
            throw new ValidationStateInputException("user no puede ser null.");
        }
        String userAlias = id.user.getAlias();
        if(userAlias == null)
        {
            throw new ValidationStateInputException("user.alias no puede ser null.");
        }
        
        String sql = "SELECT ID FROM Instances WHERE address = ? AND hascode = ? AND user = ?";
        PreparedStatement stmt = null;
        try 
        {
            stmt = dbserver.getConnection().prepareStatement(sql);
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Fallo la creacion de prepareStatement para " + sql, ex);
        }        
        try 
        {
            stmt.setString(1, id.getAddress());
            stmt.setString(2, id.getHasCode());
            stmt.setString(3, id.getUser().getAlias());
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló la asignacion de parametros para prepareStatement", ex);
        }
            
        ResultSet rs;
        try 
        {
            rs = stmt.executeQuery();
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló la ejecucion de la operacion en base de datos.", ex);
        }
        
        try 
        {
            if(rs.next())
            {
                return id;
            }
            else
            {
                return null;
            }
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló la ejecucion de la operacion en base de datos.", ex);
        }
    }
    
    /**
     * Elimina la instancia indicada
     * @param id Identificador de instancia.
     * @param dbserver
     * @return 
     * @throws SIIL.core.Exceptions.DatabaseException Fallón la instrucion a base datos.
     */
    public static int delete(Instance id, Database dbserver) throws DatabaseException, ValidationStateInputException 
    {
        if(id == null)
        {
            throw new ValidationStateInputException("id no puede ser null.");
        }
        if(id.address == null)
        {
            throw new ValidationStateInputException("address no puede ser null.");
        }
        if(id.hascode == null)
        {
            throw new ValidationStateInputException("hascode no puede ser null.");
        }
        if(id.user == null)
        {
            throw new ValidationStateInputException("user no puede ser null.");
        }
        String userAlias = id.user.getAlias();
        if(userAlias == null)
        {
            throw new ValidationStateInputException("user.alias no puede ser null.");
        }
        
        String sql = "DELETE FROM Instances WHERE address = ? AND hascode = ? AND user = ?";
        PreparedStatement stmt = null;
        try 
        {
            stmt = dbserver.getConnection().prepareStatement(sql);
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló prepareStatement para " + sql, ex);
        }
        try 
        {
            stmt.setString(1, id.address);
            stmt.setString(2, id.hascode);
            stmt.setString(3, userAlias);
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló la asignacion de parametro para instruccion prepareStatement where " +  id.address + "|" + id.hascode + "|" + userAlias, ex);
        }
        
        try 
        {
            return stmt.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló la asignacion de parametro para instruccion DELETE executeUpdate", ex);
        }
    }

    /**
     * Carga los datos del registro indicado con los correspondientes en la base de datos.
     * @param id
     * @param dbserver
     * @return
     * @throws DatabaseException 
     */
    private boolean fill(int id, Database dbserver) throws DatabaseException 
    {
        String sql = "SELECT ID,address,hascode,user FROM Instances WHERE ID = " + id;
        Statement stmt = null;
        try 
        {
            stmt = dbserver.getConnection().createStatement();
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló createStatement para " + sql, ex);
        }
        
        
        try 
        {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                this.ID = id;
                this.address = rs.getString(2);
                this.hascode = rs.getString(3);
                this.user = new User(rs.getString(4));
            }
        } 
        catch (SQLException ex) 
        {
            throw new DatabaseException("Falló la ejecucion de la fill para " + sql, ex);
        }
        return false;
    }
}
