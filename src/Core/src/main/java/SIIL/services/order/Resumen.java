
package SIIL.services.order;

import SIIL.Server.Database;
import SIIL.trace.Trace;
import SIIL.trace.Value;
import core.FailResultOperationException;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import stock.Flow;

/**
 *
 * @author Azael Reyes
 */
public class Resumen
{
    public static final String MYSQL_AVATAR_TABLE = "ServiceOrderResumen";
    private int id;
    //private Titem titem;
    private Order order;
    private Module module;
    private Flow flow;

    
    public Boolean delete(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        int afected = stmt.executeUpdate(sql);
        if(afected == 1)
        {
            return true;
        }
        return false; 
    }
    
    public static Resumen select(Database db,Flow flow) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE flowItem = " + flow.getID();
        ResultSet rs = db.query(sql);        
        Resumen resumen = null;
        if(rs.next())
        {
            resumen = new Resumen(rs.getInt(1));
        }
        else
        {
            return null;
        }
        
        //check si hay mas resultado
        if(rs.next())
        {
            throw new FailResultOperationException("La consulta [" + sql + "] tiene mulples registro como resultado.");
        }
        
        return resumen;
    }
    
    /**
     * 
     * @param db
     * @param order nueva orden que se asignara
     * @param check determina si la actualizacion es condicionalmente
     * @param traceContext
     * @return si se hace el cambio returna true, false si no y null si no se omitio
     * @throws SQLException 
     */
    public Boolean upOrder(Database db, Order order, Boolean check,Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido.");
        }
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(order == null)
        {
            throw new InvalidParameterException("Order is null.");
        }
        else if(order.getID() < 1)
        {
            throw new InvalidParameterException("Order ID es invalido.");
        }
        if(check == null)
        {
            throw new InvalidParameterException("check is null.");
        }
        Value val;
        if(check)
        {
            //descargar la orden actual
            downOrder(db);
            if(this.order.getFhService() == null) this.order.downFhService(db);
            if(order.getFhService() == null) order.downFhService(db);
            //verificar si la nueva es mas reciente
            if(order.getFhService().before(this.order.getFhService()))
            {
                val = new Value();
                val.setTraceID(traceContext.getID());
                val.setTable(MYSQL_AVATAR_TABLE);
                val.setAfter("");
                val.setField("lastServ");
                val.setBrief("La orden tiene fecha anterior.");   
                this.order.downFolio(db);
                this.order.downFhService(db);
                val.setLlave("folio=" + order.getFolio() + ";fecha=" + order.getFhService());   
                val.insert(db);
                return null;
            }
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET lastServ = " + order.getID() + " WHERE id=" + id;
        val = new Value();
        val.setTraceID(traceContext.getID());
        val.setTable(MYSQL_AVATAR_TABLE);
        if(order.getItemFlow() == null)
        {
            order.downItemFlow(db);
            order.getItemFlow().downItem(db.getConnection());
            order.getItemFlow().getItem().downNumber(db.getConnection());
        }
        else if(order.getItemFlow().getItem() == null)
        {
            order.getItemFlow().downItem(db.getConnection());
            order.getItemFlow().getItem().downNumber(db.getConnection());
        }
                else  if(order.getItemFlow().getItem().getNumber() == null)
                {
                    order.getItemFlow().getItem().downNumber(db.getConnection());                    
                }
                val.setAfter(order.getItemFlow().getItem().getNumber());
                val.setField("lastServ");
                val.setBrief("Se actualiza la orden asociada");   
                this.order.downFolio(db);
                this.order.downFhService(db);
                val.setLlave("folio=" + order.getFolio() + ";fecha=" + order.getFhService());   
                val.insert(db);
        //System.out.println(sql);
        Statement stmt = db.getConnection().createStatement();    
        int affected = stmt.executeUpdate(sql);
        if( affected == 1)
        {
            return true;
        }
        else if(affected > 1)
        {
            throw new FailResultOperationException("Se afectaron demasiados registro con esta operacion (" + affected + ")");
        }
        else
        {
            return false;
        }
    }
    
    public Resumen(int id)
    {
        this.id = id;
    }
    
    public Boolean insert(Database db,Flow flow,Order order,Module module,Trace traceContext) throws SQLException
    {
        clean();
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        if(flow == null)
        {
            throw new InvalidParameterException("Flow is null.");
        }
        if(order == null)
        {
            throw new InvalidParameterException("Order is null.");
        }
        if(module == null)
        {
            throw new InvalidParameterException("Module is null.");
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (module,lastServ,flowItem) VALUES(" + module.getID() + "," + order.getID() + "," + flow.getID() + ")";
        Value val;
        val = new Value();
        val.setTraceID(traceContext.getID());
        val.setTable(MYSQL_AVATAR_TABLE);
        if(flow.getItem() == null)
        {
            flow.downItem(db.getConnection());
        }
        else if(flow.getItem().getNumber() == null)
        {
            flow.getItem().downNumber(db.getConnection());
        }
        val.setAfter(flow.getItem().getNumber());
        val.setField("flowItem");
        val.setBrief("Se agregar el equipo al Resumen de Servicios");        
        val.setLlave("folio="+order.getFolio()+";fecha="+order.getFhService());        
        val.insert(db);
        //System.out.println(sql);
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);        
        if(affected != 1)
        {
            return false;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            id = rs.getInt(1);
            return true;
        }
        else
        {
            id = -1;
            return false;
        }
    }
    
    /**
     * @return the id
     */
    public int getID() {
        return id;
    }


    /**
     * @return the order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @return the module
     */
    public Module getModule() {
        return module;
    }

    private void clean() 
    {
        id = -1;
        module = null;
        order = null;
        flow = null;
    }

    public Boolean downOrder(Database db) throws SQLException 
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT lastServ FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.order = new Order(rs.getInt(1));
            return true;
        }
        else
        {
            this.order = null;
            return false;
        }
    }

    public Boolean downFlow(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT flowItem FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.flow = new Flow(rs.getInt(1));
            return true;
        }
        else
        {
            this.flow = null;
            return false;
        }
    }

    public Boolean downModule(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT module FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.module = new Module(rs.getInt(1));
            return true;
        }
        else
        {
            this.module = null;
            return false;
        }
    }

    /**
     * @return the flow
     */
    public Flow getFlow() {
        return flow;
    }

    /**
     * @param flow the flow to set
     */
    public void setFlow(Flow flow) {
        this.flow = flow;
    }
}
