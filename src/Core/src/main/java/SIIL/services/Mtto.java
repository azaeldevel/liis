
package SIIL.services;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import static SIIL.services.Trabajo.MYSQL_AVATAR_TABLE;
import SIIL.services.order.Order;
import SIIL.trace.Trace;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;
import stock.Flow;

/**
 *
 * @author Azael Reyes
 */
public class Mtto 
{
    private int id;
    private Flow equipo;
    private String location;
    private Enterprise enterprise;
    private Order penultimateOrder;
    private String penultimateComment;
    private Order lastOrder;
    private String lastComment;

    private static final String MYSQL_AVATAR_TABLE = "ServicesMtto";

    public Return addOrder(Database dbserver, Order order,String comment,Trace traceContext) throws SQLException 
    {
        if(dbserver == null)
        {
            return new Return(false,"Conexion BD es nula");
        }
        String sql = "SELECT lastOrder,lastComment FROM " + MYSQL_AVATAR_TABLE + " WHERE lastOrder > 0 AND id = " + id;
        Statement stmt = dbserver.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            upPenultimateOrder(dbserver, new Order(rs.getInt(1)), traceContext);
            upPenultimateCommnet(dbserver, rs.getString(2), traceContext);
            upLastOrder(dbserver, order, traceContext);
            upLastCommnet(dbserver, comment, traceContext);
            return new Return(true);
        }
        else
        {
            return new Return(false,"No penultima orden asociada");
        }
    }
    
    public Return downLastComment(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Conexion BD es nula");
        }
        String sql = "SELECT lastComment FROM " + MYSQL_AVATAR_TABLE + " WHERE lastComment IS NOT NULL AND id = " + id;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            lastComment = rs.getString(1);
            return new Return(true);
        }
        else
        {
            lastComment = null;
            return new Return(false,"No hay comentario en ultima orden");
        }
    }
    
    public Return downLastOrder(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Conexion BD es nula");
        }
        String sql = "SELECT lastOrder FROM " + MYSQL_AVATAR_TABLE + " WHERE lastOrder > 0 AND id = " + id;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            lastOrder = new Order(rs.getInt(1));
            return new Return(true);
        }
        else
        {
            lastOrder = null;
            return new Return(false,"No penultima orden asociada");
        }
    }
       
    
    public Return downPenultimateComment(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Conexion BD es nula");
        }
        String sql = "SELECT penultimateComment FROM " + MYSQL_AVATAR_TABLE + " WHERE penultimateComment IS NOT NULL AND id = " + id;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            penultimateComment = rs.getString(1);
            return new Return(true);
        }
        else
        {
            penultimateComment = null;
            return new Return(false,"No hay comentario en penultima orden");
        }
    }
    
    public Return downPenultimateOrder(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Conexion BD es nula");
        }
        String sql = "SELECT penultimateOrder FROM " + MYSQL_AVATAR_TABLE + " WHERE penultimateOrder > 0 AND id = " + id;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            penultimateOrder = new Order(rs.getInt(1));
            return new Return(true);
        }
        else
        {
            penultimateOrder = null;
            return new Return(false,"No penultima orden asociada");
        }
    }
        
    public Return downClient(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Conexion BD es nula");
        }
        String sql = "SELECT company FROM " + MYSQL_AVATAR_TABLE + " WHERE company > 0 AND id = " + id;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            enterprise = new Enterprise(rs.getInt(1));
            return new Return(true);
        }
        else
        {
            enterprise = null;
            return new Return(false,"NO hay cliente asociado");
        }
    }
        
    public Return upPenultimateOrder(Database db, Order order,Trace traceContext) throws SQLException
    {        
        if(db == null)
        {
            return new Return(false,"Conexion a servidor NULL");
        }
        if(order  == null)
        {
            return new Return(false,"Parametro comment es NULL");
        }
        else if(order.getID() < 1)
        {
            return new Return(false,"Parametro prevOrder con ID = " + order.getID());
        }
        this.penultimateOrder = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET penultimateOrder =" + order.getID() + " WHERE  id = " + id;
        Statement stmt = db.getConnection().createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return new Return(true);
        }
        else
        {
            return new Return(false,"Cantidad de efecto invalido : " + retUp);
        }
    }
    
    public Return upLastOrder(Database db, Order lastOrder,Trace traceContext) throws SQLException
    {        
        if(db == null)
        {
            return new Return(false,"Conexion a servidor NULL");
        }
        if(lastOrder  == null)
        {
            return new Return(false,"Parametro comment es NULL");
        }
        this.lastOrder = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET lastOrder = " + lastOrder.getID() + " WHERE  id =" + id;
        Statement stmt = db.getConnection().createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return new Return(true);
        }
        else
        {
            return new Return(false,"Cantidad de efecto invalido : " + retUp);
        }
    }
    
    public Return upPenultimateCommnet(Database db, String comment,Trace traceContext) throws SQLException
    {        
        if(db == null)
        {
            return new Return(false,"Conexion a servidor NULL");
        }
        if(comment  == null)
        {
            return new Return(false,"Parametro comment es NULL");
        }
        this.penultimateComment = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET penultimateComment='" + comment+ "' WHERE  id =" + id;
        Statement stmt = db.getConnection().createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return new Return(true);
        }
        else
        {
            return new Return(false,"Cantidad de efecto invalido : " + retUp);
        }
    }
        
    public Return upLastCommnet(Database db, String comment,Trace traceContext) throws SQLException
    {        
        if(db == null)
        {
            return new Return(false,"Conexion a servidor NULL");
        }
        else if(comment == null)
        {
            return new Return(false,"Parametro comment es NULL");
        }
        this.lastComment = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET lastComment='" + comment+ "' WHERE  id =" + id;
        Statement stmt = db.getConnection().createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return new Return(true);
        }
        else
        {
            return new Return(false,"Cantidad de efecto invalido : " + retUp);
        }
    }
    
    public boolean selectRandom(Database db) throws SQLException 
    {
        clean();      
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = db.getConnection().createStatement();
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
    
    public Return upEquipo(Database db, Flow equipo,Trace traceContext) throws SQLException
    {        
        if(db == null)
        {
            return new Return(false,"Conexion a servidor NULL");
        }
        if(equipo  == null)
        {
            return new Return(false,"Parametro equipo es NULL ");
        }
        else if(equipo.getID() < 1)
        {
            return new Return(false,"Parametro equipo con ID = " + equipo.getID());
        } 
        this.equipo = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET equipo=" + equipo.getID() + " WHERE  id =" + id;
        Statement stmt = db.getConnection().createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return new Return(true);
        }
        else
        {
            return new Return(false,"Cantidad de efecto invalido : " + retUp);
        }
    }
    
    public Return upEnterprise(Database db, Enterprise enterprise,Trace traceContext) throws SQLException
    {        
        if(db == null)
        {
            return new Return(false,"Conexion a servidor NULL");
        }          
        if(enterprise  == null)
        {
            return new Return(false,"Parametro enterprise es NULL");
        }
        else if(enterprise.getID() < 1)
        {
            return new Return(false,"Parametro enterprise con ID = " + enterprise.getID());
        }
        this.enterprise = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET company=" + enterprise.getID() + " WHERE  id =" + id;
        Statement stmt = db.getConnection().createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return new Return(true);
        }
        else
        {
            return new Return(false,"Affected row invalid : " + retUp);
        }
    }

    public Return insert(Database db,Flow equipo,Enterprise enterprise,Trace traceContext) throws SQLException
    {
        if(id > 0)
        {
            return new Return(false,"Llamada a insert con ID = " + id);
        }
        if(db == null)
        {
            return new Return(false,"Conexion a servidor NULL");
        }        
        if(equipo  == null)
        {
            return new Return(false,"Parametro equipo es NULL ");
        }
        else if(equipo.getID() < 1)
        {
            return new Return(false,"Parametro equipo con ID = " + equipo.getID());
        }        
        if(enterprise  == null)
        {
            return new Return(false,"Parametro enterprise es NULL");
        }
        else if(enterprise.getID() < 1)
        {
            return new Return(false,"Parametro enterprise con ID = " + enterprise.getID());
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(equipo,company) VALUES(" + equipo.getID() + "," + enterprise.getID() + ")";
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return new Return(false,"INSERT no unico renglon, total : " + affected);
        }
        ResultSet rs = stmt.getGeneratedKeys();
        int generated = -1;        
        if (rs.next())
        {
            id = rs.getInt(1);
            return new Return(true,id);
        }
        else
        {
            id = -1;
            return new Return(false,"INSERT no genero key");
        }
    }
    
    /**
     * @return the id
     */
    public int getId() 
    {
        return id;
    }

    /**
     * @return the equipo
     */
    public Flow getEquipo() 
    {
        return equipo;
    }

    /**
     * @return the ubique
     */
    public String getLocation() 
    {
        return location;
    }

    /**
     * @return the prevOrder
     */
    public Order getPenultimateOrder() 
    {
        return penultimateOrder;
    }

    /**
     * @return the preComment
     */
    public String getPenultimateComment()
    {
        return penultimateComment;
    }

    /**
     * @return the lastOrder
     */
    public Order getLastOrder() 
    {
        return lastOrder;
    }

    /**
     * @return the lastComment
     */
    public String getLastComment() 
    {
        return lastComment;
    }

    /**
     * @return the enterprise
     */
    public Enterprise getEnterprise() 
    {
        return enterprise;
    }

    private void clean() 
    {
        id = -1;
        enterprise = null;
        equipo = null;
        penultimateComment = null;
        penultimateOrder = null;
        lastComment = null;
        lastOrder = null;
        location = null;
    }
}
