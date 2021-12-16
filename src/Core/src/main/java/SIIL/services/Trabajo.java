package SIIL.services;

import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.trace.Trace;
import SIIL.trace.Value;
import core.FailResultOperationException;
import core.Searchable;
import database.mysql.sales.Remision;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import process.Module;
import process.State;

/**
 *
 * @author Azael Reyes
 */
public class Trabajo implements Searchable
{
    public final static String MYSQL_AVATAR_TABLE = "ServicesTrabajo";    
    public final static String MYSQL_AVATAR_TABLE_RL = "Relacion de Trabajo"; 

    /**
     * @return the fhUpdate
     */
    public java.util.Date getFhUpdate() {
        return fhUpdate;
    }

    public enum Sheet
    {
        CAMPO,
        TALLER,
        GRUA,
        MTTO,
        NEW
    }
        
    private int id;
    private State state;
    private Sheet sheet;
    private Remision sa;
    private ServiceQuotation quotedService;
    private Enterprise enterprise;
    private Person mechanic;
    private String brief;
    private java.util.Date fhToDo;  
    private java.util.Date fhUpdate;
    
    public boolean upFhUpdate(Database db, Timestamp date,Trace traceContext) throws SQLException
    {
        if(getId() < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }        
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        if(date == null)
        {
            throw new InvalidParameterException("No se permite valores nulos en los parametros");
        }
        this.fhUpdate = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET fhUpdate='" + date + "' WHERE id=" + getId();
        Statement stmt = db.getConnection().createStatement();
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Fecha");
                val.setBrief("Fecha de Actualizacion");
                val.setLlave("folio=" + id);
                val.insert(db);
            }
        }
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    public static Timestamp lastUpdated(Database db, Timestamp last) throws SQLException 
    {
        if(db == null)
        {
            return null;
        }
        String sql = "SELECT fhUpdate FROM " + MYSQL_AVATAR_TABLE ;
        if(last == null)
        {
            sql += " WHERE fhUpdate IS NOT NULL ORDER BY fhUpdate DESC LIMIT 1";
        }
        else
        {
            sql += " WHERE fhUpdate > '" + last + "' ORDER BY fhUpdate DESC LIMIT 1";
        }
        
        Statement stmt = db.getConnection().createStatement();
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);        
        if(rs.next())
        {
            System.out.println("Updatable : Si");
            return rs.getTimestamp(1);
        }
        else
        {
            System.out.println("Updatable : No");
            return null;    
        }
    }
    
    public boolean downFhUpdate(Database db) throws SQLException 
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT fhUpdate FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            fhUpdate = rs.getTimestamp(1);
            return true;
        }
        else
        {
            fhUpdate = null;
            return false;    
        }
    }
    
    
    @Override
    public String getIdentificator()
    {
        return sa.getFullFolio();
    }
    
    @Override
    public String getBrief() 
    {
        return brief;
    }
    
    /**
     * 
     * @param database
     * @param office
     * @param text
     * @param activeSA Si se buscara en el campo de SA
     * @return
     * @throws SQLException 
     */
    public static List<Searchable> search(Database database,Office office,String text,boolean activeSA) throws SQLException
    {
        List<Searchable> list = new ArrayList<>();        
        String table = MYSQL_AVATAR_TABLE;
        String sql = "SELECT id FROM " + table ;  
        String whereSQL = " WHERE (flag IS NULL OR flag != 'D') ";
        if(activeSA == true)
        {
            List<Remision> lstR = Remision.search(database, office, text, 0);
            for(Remision remision : lstR)
            {
                ResultSet rs = database.query(sql + whereSQL + " AND sa = " + remision.getID());
                while(rs.next())
                {
                    Trabajo trb = new Trabajo(rs.getInt(1));
                    trb.download(database);
                    list.add(trb);                    
                }
            }            
        }        
        return list;
    }
    
    public Boolean downDate(Database db) throws SQLException 
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT fhTodo FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getId();
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            fhToDo = rs.getDate(1);
            return true;
        }
        else
        {
            fhToDo = null;
            return false;    
        }
    }
    
    public static boolean have(Database db, java.util.Date date) throws SQLException
    {      
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE (flag!='D' OR flag is null) AND fhToDo = '" + new java.sql.Date(date.getTime())+ "'";
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            return true;
        }
        else
        {
            return false;
        }        
    }
        
    public static boolean have(Database db, java.util.Date date, int ordinal) throws SQLException
    {      
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        State state = new State(-1);
        state.select(db.getConnection(), new Module(8), ordinal);
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE (flag!='D' OR flag is null) AND fhToDo = '" + new java.sql.Date(date.getTime()) + "' AND state = " + state.getID();
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            return true;
        }
        else
        {
            return false;
        }        
    }
    
    public boolean delete(Database db, Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET flag='D' WHERE id=" + id;
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val;
                val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Delete");
                val.setBrief("Delete");
                val.setLlave("folio=" + id);
                val.insert(db);
            }
        }
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean upSheet(Database db, Sheet sheet,Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }        
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        if(sheet == null)
        {
            throw new InvalidParameterException("No se permite valores nulos en los parametros");
        }
        this.sheet = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET sheet=";
        if(Sheet.CAMPO == sheet)
        {
            sql = sql + "'CM'";
        }
        else if(Sheet.TALLER == sheet)
        {
            sql = sql + "'TLL'";
        }
        else if(Sheet.GRUA == sheet)
        {
            sql = sql + "'GR'";
        }
        else if(Sheet.MTTO == sheet)
        {
            sql = sql + "'MAN'";
        }
        else
        {
            sql = sql + sheet;
        }
        sql += " WHERE id=" + id;
        //System.out.println(sql);
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val;
                val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Hoja");
                val.setBrief("Cambio de Hoja");
                val.setLlave("folio=" + id);
                val.insert(db);
            }
        }
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Boolean checkClient(Database db,Enterprise newEnterprise) throws SQLException
    {
        if(enterprise == null)
        {
            if(!downClient(db))
            {
                throw new FailResultOperationException("No se encontro cliente para RT = " + id);
            }
        }
        if(enterprise.getID() < 1)
        {
            throw new FailResultOperationException("No se encontro cliente para RT = " + id);
        }
        if(newEnterprise == null)
        {
            throw new FailResultOperationException("checkClient el cliente del operando es null ");
        }
        
        if(enterprise.getID() == newEnterprise.getID())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Boolean downState(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT state FROM " + MYSQL_AVATAR_TABLE + " WHERE state > 0 AND id = " + getId();
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            state = new State(rs.getInt(1));
            return true;
        }
        else
        {
            state = null;
            return false;
        }
    }
    
    public static Trabajo create(Database db, Sheet sheet, Enterprise enterprise,ServiceQuotation quotedService,java.util.Date fecha,String comment,Person operator,Trace traceContext) throws SQLException
    {
        if(db == null)
        {
            return null;
        }
        if(sheet == null)
        {
            return null;
        }
        if(enterprise.getID() < 1)
        {
            return null;
        }
        
        Trabajo newTrabajo = insert(db, sheet, enterprise,operator,traceContext);
        if(newTrabajo != null)
        {
            if(quotedService != null)
            {
                if(!newTrabajo.upQuotedService(db, quotedService,traceContext)) throw new FailResultOperationException("Fallo la actualizacion de Trabajo.Contizacion de servicio ");
            }
            if(fecha != null)
            {
                if(!newTrabajo.upfhToDo(db, fecha,traceContext))  throw new FailResultOperationException("Fallo la actualizacion de Trabajo.fecha");
            }
            else
            {
                if(!newTrabajo.upfhToDo(db, new java.util.Date(),traceContext))  throw new FailResultOperationException("Fallo la actualizacion de Trabajo.fecha");
            }
            if(comment != null)
            {
                if(!newTrabajo.upBrief(db, comment,traceContext)) throw new FailResultOperationException("Fallo la actualizacion de Trabajo.brief");
            }
        }
        else
        {
            throw new FailResultOperationException("Fallo la actualizacion de Trabajo.ibsert, no se creo en registro");
        }
        return newTrabajo;
    }
    
    public Boolean downClient(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT company FROM " + MYSQL_AVATAR_TABLE + " WHERE company > 0 AND id = " + getId();
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            enterprise = new Enterprise(rs.getInt(1));
            return true;
        }
        else
        {
            enterprise = null;
            return false;
        }
    }
    
    public boolean upClient(Database db, Enterprise enterprise,Trace traceContext) throws SQLException
    {
        if(getId() < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }        
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        this.enterprise = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET company=";  
        if(enterprise != null)
        {
            sql += enterprise.getID();
        }
        else
        {
            sql += "NULL";
        }
        sql += " WHERE id=" + getId();
        Statement stmt = db.getConnection().createStatement();
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val;
                val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Cliente");
                val.setBrief("Asignacion de cliente");
                val.setLlave("folio=" + id);
                val.insert(db);
            }
        }
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * @return the id
     */
    public int getId() 
    {
        return id;
    }
    
    public Boolean downQuotedService(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT quotedService FROM " + MYSQL_AVATAR_TABLE + " WHERE quotedService > 0 AND id = " + getId();
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            quotedService = new ServiceQuotation();
            quotedService.setID(rs.getInt(1));
            return true;
        }
        else
        {
            quotedService = null;
            return false;
        }
    }
    
    public Boolean downSA(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT sa FROM " + MYSQL_AVATAR_TABLE + " WHERE sa > 0 AND id = " + getId();
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            if(rs.getInt(1) > 0)
            {
                sa = new Remision(rs.getInt(1));
                sa.download(connection);
                return true;                
            }
            return false;
        }
        else
        {
            sa = null;
            return false;
        }
    }
    
    public boolean upQuotedService(Database db, ServiceQuotation quotedService,Trace traceContext) throws SQLException
    {
        if(getId() < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }        
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        this.quotedService = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET quotedService="; 
        if(quotedService != null)
        {
            sql += quotedService.getID();
        }
        else
        {
            sql += "NULL";
        }
        sql += " WHERE id=" + id;
        Statement stmt = db.getConnection().createStatement();
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Cotizacion");
                val.setBrief("Asig. de Cotizacion");
                val.setLlave("folio=" + id);
                val.insert(db);
            }
        }
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * @return the quotedService
     */
    public ServiceQuotation getQuotedService() 
    {
        return quotedService;
    }

    /**
     * @param quotedService the quotedService to set
     */
    public void setQuotedService(ServiceQuotation quotedService) 
    {
        this.quotedService = quotedService;
    }
    
    public boolean upState(Database db, State state,Trace traceContext) throws SQLException
    {
        if(id < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        this.state = null;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET state = " + state.getID() + " WHERE id=" + getId();
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql);
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("estado");
                val.setBrief("Asig. de estado");
                val.setLlave("folio=" + id);
                val.insert(db);
            }
        }
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    
    /**
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * @return the sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * @return the sa
     */
    public Remision getSA() {
        return sa;
    }

    /**
     * @return the company
     */
    public Enterprise getCompany() {
        return enterprise;
    }

    /**
     * @return the mechanic
     */
    public Person getMechanic() {
        return mechanic;
    }

    
    /**
     * @return the fhToDo
     */
    public java.util.Date getFhToDo() {
        return fhToDo;
    }
       
    public Boolean download(Database db) throws SQLException 
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT state,company,mechanic,fhToDo,brief,sa,sheet,quotedService FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getId();
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            state = new State(rs.getInt(1));
            enterprise = new Enterprise(rs.getInt(2));
            mechanic = new Person(rs.getInt(3));
            if(rs.getDate(4) != null) fhToDo = new java.util.Date(rs.getDate(4).getTime());            
            brief = rs.getString(5);
            if(rs.getInt(6) > 0)
            {
                sa = new Remision(rs.getInt(6));
                sa.downSerie(db);
                sa.downFolio(db);            }
            else
            {
                sa = null;
            }
            if(rs.getString(7).equals("CM"))
            {
                sheet = Sheet.CAMPO;
            }
            else if(rs.getString(7).equals("TLL"))
            {
                sheet = Sheet.TALLER;
            }
            else if(rs.getString(7).equals("GR"))
            {
                sheet = Sheet.GRUA;
            }
            else if(rs.getString(7).equals("MAN"))
            {
                sheet = Sheet.MTTO;
            }
            else
            {
                sheet = null;
            }
            if(rs.getInt(8) > 0)
            {
                quotedService = new ServiceQuotation();
                quotedService.setID(rs.getInt(8));
            }
            else
            {
                quotedService = null;
            }
            return true;
        }
        else
        {
            clean();
            return false;    
        }
    }
    
    
    public Boolean downMechanic(Database db) throws SQLException 
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT mechanic FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getId();
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            mechanic = new Person(rs.getInt(1));
            return true;
        }
        else
        {
            mechanic = null;
            return false;    
        }
    }
        
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Boolean downBrief(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT brief FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getId();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            brief = rs.getString(1);
            return true;
        }
        else
        {
            brief = null;
            return false;
        }
    }
        
    public boolean upBrief(Database db, String brief,Trace traceContext) throws SQLException
    {
        if(getId() < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }        
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        if(brief == null)
        {
            throw new InvalidParameterException("No se permite valores nulos en los parametros");
        }
        this.brief = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET brief='" + brief + "' WHERE id=" + getId();
        Statement stmt = db.getConnection().createStatement();
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Comentario");
                val.setBrief("Asig. de Comentario");
                val.setLlave("folio=" + id);
                val.insert(db);
            }
        }
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean upfhToDo(Database db, java.util.Date date,Trace traceContext) throws SQLException
    {
        if(getId() < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }        
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        if(date == null)
        {
            throw new InvalidParameterException("No se permite valores nulos en los parametros");
        }
        this.fhToDo = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET fhToDo='" + new java.sql.Date(date.getTime()) + "' WHERE id=" + getId();
        Statement stmt = db.getConnection().createStatement();
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Fecha");
                val.setBrief("Asig. de Fecha");
                val.setLlave("folio=" + id);
                val.insert(db);
            }
        }
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    } 
    
    public boolean upMechanic(Database db, Person mechanic,Trace traceContext) throws SQLException
    {
        if(getId() < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }        
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        this.mechanic = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET mechanic=";
        if(mechanic != null)
        {
            sql += mechanic.getpID();
            if(traceContext != null)
            {
                if(traceContext.getTrace() > 0)
                {
                    Value val = new Value();
                    val.setTraceID(traceContext.getTrace());
                    val.setTable(MYSQL_AVATAR_TABLE_RL);
                    val.setField("Mecanico");
                    val.setBrief("Asig. de Mecanico");
                    val.setAfter(mechanic.toString());
                    val.setLlave("folio=" + id);
                    val.insert(db);
                }
            }
        }
        else
        {
            sql += "NULL";
        }
        sql += " WHERE id=" + id;
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 
     */
    private void clean() 
    {
        id = -1;
        brief = null;
        mechanic = null;
        sa = null;
        sheet = null;
        state = null;
    }
    
    /**
     * 
     * @param db
     * @return
     * @throws SQLException 
     */
    public Boolean selectRandom(Database db) throws SQLException 
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
    
    public boolean upSA(Database db, Remision sa,Trace traceContext) throws SQLException
    {
        if(getId() < 1)
        {
            throw new InvalidParameterException("ID invalido."); 
        }        
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        this.sa = null;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET sa=";
        if(sa != null)
        {
            sql += sa.getID(); 
        }
        else
        {
            sql += "NULL" ;
        }
        sql += " WHERE id=" + getId();
        Statement stmt = db.getConnection().createStatement();
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("SA");
                val.setBrief("Asig. de SA");
                val.setAfter(sa.getFullFolio());
                val.setLlave("folio=" + id);
                val.insert(db);
            }
        }
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Trabajo(int id)
    {
        this.id = id;
    }
    
    public static Trabajo insert(Database db, Sheet sheet, Enterprise enterprise, Person operator,Trace traceContext) throws SQLException
    {
        if(db == null)
        {
            throw new InvalidParameterException("La conexion a la base datos es null");
        }
        if(sheet == null)
        {
            throw new InvalidParameterException("El tipo de hoja es null");
        }
        if(enterprise == null)
        {
            throw new InvalidParameterException("La empresa es null");
        }
        else if(enterprise.getID() < 1)
        {
            throw new InvalidParameterException("El ID de la empresa es invalido");
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(mechanic,state,sheet,company,office,fhFolio) VALUES(NULL,11,";
        if(Sheet.CAMPO == sheet)
        {
            sql = sql + "'CM'";
            if(traceContext != null)
            {
                if(traceContext.getTrace() > 0)
                {
                    Value val = new Value();
                    val.setTraceID(traceContext.getTrace());
                    val.setTable(MYSQL_AVATAR_TABLE_RL);
                    val.setField("Hoja");
                    val.setAfter("Campo");
                    val.setBrief("Hoja");
                    val.setLlave("¿?");
                    val.insert(db);
                }
            }
        }
        else if(Sheet.TALLER == sheet)
        {
            sql = sql + "'TLL'";
            if(traceContext != null)
            {
                if(traceContext.getTrace() > 0)
                {
                    Value val = new Value();
                    val.setTraceID(traceContext.getTrace());
                    val.setTable(MYSQL_AVATAR_TABLE_RL);
                    val.setField("Hoja");
                    val.setAfter("Taller");
                    val.setBrief("Hoja");
                    val.setLlave("¿?");
                    val.insert(db);
                }
            }
        }
        else if(Sheet.GRUA == sheet)
        {
            sql = sql + "'GR'";
            if(traceContext != null)
            {
                if(traceContext.getTrace() > 0)
                {
                    Value val = new Value();
                    val.setTraceID(traceContext.getTrace());
                    val.setTable(MYSQL_AVATAR_TABLE_RL);
                    val.setField("Hoja");
                    val.setAfter("Grua");
                    val.setBrief("Hoja");
                    val.setLlave("¿?");
                    val.insert(db);
                }
            }
        }
        else if(Sheet.MTTO == sheet)
        {
            sql = sql + "'MAN'";
            if(traceContext != null)
            {
                if(traceContext.getTrace() > 0)
                {
                    Value val = new Value();
                    val.setTraceID(traceContext.getTrace());
                    val.setTable(MYSQL_AVATAR_TABLE_RL);
                    val.setField("Hoja");
                    val.setBrief("Hoja");
                    val.setAfter("Mantenimiento");
                    val.setLlave("¿?");
                    val.insert(db);
                }
            }
        }
        else
        {
            sql = sql + sheet;
        }
        sql = sql + "," + enterprise.getID();
        sql = sql + "," + operator.getOffice().getID();
        sql = sql + ",'" + db.getTimestamp() + "')";
        if(traceContext != null)
        {
            if(traceContext.getTrace() > 0)
            {
                Value val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Cliente");
                val.setAfter(enterprise.getName());
                val.setBrief("Cliente");
                val.setLlave("¿?");
                val.insert(db);
                val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Office");
                val.setBrief("Oficina");
                val.setLlave("¿?");
                val.setAfter(operator.getOffice().getName());
                val.insert(db);
                val = new Value();
                val.setTraceID(traceContext.getTrace());
                val.setTable(MYSQL_AVATAR_TABLE_RL);
                val.setField("Fecha");
                val.setBrief("Fecha");
                val.setLlave("¿?");
                val.setAfter(db.getTimestamp().toString());
                val.insert(db);
           }
        }
        //System.out.println(sql);
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            throw new FailResultOperationException("No se genero nuevo registro para la orden [" + sql + "]");
        }
        ResultSet rs = stmt.getGeneratedKeys();
        int generated = -1;        
        if (rs.next())
        {
            return new Trabajo(rs.getInt(1));
        }
        else
        {
            return null;
        }
    }    
}
