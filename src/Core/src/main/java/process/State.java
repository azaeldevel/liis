
package process;

import SIIL.Server.Database;
import SIIL.Server.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azael Reyes
 */
public class State 
{
    private static final String MYSQL_AVATAR_TABLE = "ProcessStates";       
    public enum Steps
    {
        CS_CREATED,
        CS_QUOTED,
        CS_ETA,
        CS_TRANS,
        CS_ALMACEN,
        CS_ENTREGANDO,
        CS_FIN,
        CS_CANCEL,
        
        PO_INIT,
        PO_CREATED,
        PO_PURCHASE,
        PO_PURCHASED,
        PO_ARRIVED,
        
        RT_ACCEPTED,
        RT_URGE,
        RT_ASIGNED,
        RT_ATTENDING,
        RT_QUOTING,
        RT_INVOICING,
        RT_INVOICED,
        RT_END,
        RT_CANCEL,
        
        IS_REVIEW,
        IS_EDIT,
        IS_STAMPING,
        IS_STAMPED,
        IS_SAVED,
        
        SALES_QUOTATION,
        SALES_REMISION,
        SALES_INVOICE,
        
        NOTHING,
    }
    
    //
    private int id;
    private String code;
    private String name;
    private int ordinal;
    private Module module;   
    
    
    @Override
    public String toString()
    {
        if(id > 0)
        {
            return name;
        }
        else
        {
            return "Seleccione...";
        }
    }
    
    public Boolean select(Connection connection,Module module,String code) throws SQLException
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT code,name,ordinal,module,id FROM " + MYSQL_AVATAR_TABLE + " WHERE module = " + module.getID() + " AND code = '" + code + "'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.code = rs.getString(1);
            this.name = rs.getString(2);
            this.ordinal = rs.getInt(3);
            this.module = new Module(rs.getInt(4));
            this.id = rs.getInt(5);
            return true;
        }
        else
        {
            clean();
            return false;
        }
    }
    
    public Boolean download(Database db) throws SQLException
    {
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT code,`name`,ordinal,module FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + this.id;
        //System.out.println(sql);
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.code = rs.getString(1);            
            this.name = rs.getString(2);            
            this.ordinal = rs.getInt(3);            
            this.module = new Module(rs.getInt(4));            
            return true;
        }
        else
        {
            clean();
            return false;
        }
    }
    
    public State previous(Database db) throws SQLException
    {
        String query = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE module = " + module.getID() + " AND ordinal = " + (ordinal - 1);
        //System.out.println(query);
        ResultSet rs = db.query(query);
        State newState;
        if(rs.next())
        {
            newState = new State(rs.getInt(1));
            if(newState.download(db))
            {
                return newState;
            }
            else 
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    public State next(Database db) throws SQLException
    {
        String query = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE module = " + module.getID() + " AND ordinal = " + (ordinal + 1);
        //System.out.println(query);
        ResultSet rs = db.query(query);
        State newState;
        if(rs.next())
        {
            newState = new State(rs.getInt(1));
            if(newState.download(db))
            {
                return newState;
            }
            else 
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    
    public Boolean select(Database db,Steps step) throws SQLException
    {
        if(step == Steps.CS_CREATED)
        {
            id = 3;
        }
        else if(step == Steps.CS_QUOTED)
        {
            id = 4;
        }
        else if(step == Steps.CS_ETA)
        {
            id = 5;
        }
        else if(step == Steps.CS_TRANS)
        {
            id = 6;
        }
        else if (step == Steps.CS_ALMACEN)
        {
            id = 7;
        }
        else if(step == Steps.CS_ENTREGANDO)
        {
            id = 8;
        }
        else if(step == Steps.CS_FIN)
        {
            id = 9;            
        }
        else if(step == Steps.CS_CANCEL)
        {
            id = 10;
        }
        else if(step == Steps.PO_INIT)
        {
            id = 1;
        }
        else if(step == Steps.PO_CREATED)
        {
            id = 2;
        }
        else if(step == Steps.PO_PURCHASE)
        {
            id = 19;
        }
        else if(step == Steps.PO_PURCHASED)
        {
            id = 20;
        }
        else if(step == Steps.PO_ARRIVED)
        {
            id = 34;
        }
        else if(step == Steps.RT_ACCEPTED)
        {
            id = 11;
        }
        else if(step == Steps.RT_URGE)
        {
            id = 17;
        }
        else if(step == Steps.RT_ASIGNED)
        {
            id = 12;
        }
        else if(step == Steps.RT_ATTENDING)
        {
            id = 13;
        }
        else if(step == Steps.RT_QUOTING)
        {
            id = 18;
        }
        else if(step == Steps.RT_INVOICING)
        {
            id = 32;
        }
        else if(step == Steps.RT_INVOICED)
        {
            id = 33;
        }
        else if(step == Steps.RT_END)
        {
            id = 14;
        }
        else if(step == Steps.RT_CANCEL)
        {
            id = 15;
        }
        else if(step == Steps.IS_REVIEW)
        {
            id = 27;
        }
        else if(step == Steps.IS_EDIT)
        {
            id = 28;
        }
        else if(step == Steps.IS_STAMPING)
        {
            id = 29;
        }
        else if(step == Steps.IS_STAMPED)
        {
            id = 30;
        }
        else if(step == Steps.IS_SAVED)
        {
            id = 31;
        }
        else if(step == Steps.SALES_QUOTATION)
        {
            id = 35;
        }
        else if(step == Steps.SALES_REMISION)
        {
            id = 36;
        }
        else if(step == Steps.SALES_INVOICE)
        {
            id = 37;
        }
        else
        {
            id = -1;
        }
        
        return download(db);
    }
    

    public Steps getStep()
    {
        switch(id)
        {
            case 3:
                return Steps.CS_CREATED;
            case 4:
                return Steps.CS_QUOTED;
            case 5:
                return Steps.CS_ETA;
            case 6:
                return Steps.CS_TRANS;
            case 7:
                return Steps.CS_ALMACEN;
            case 8:
                return Steps.CS_ENTREGANDO;
            case 9:
                return Steps.CS_FIN;
            case 10:
                return Steps.CS_CANCEL; 
                
            case 1:
                return Steps.PO_INIT;
            case 2:
                return Steps.PO_CREATED;
            case 19:
                return Steps.PO_PURCHASE;
            case 20:
                return Steps.PO_PURCHASED;
            case 34:
                return Steps.PO_ARRIVED;
                
            case 11:
                return Steps.RT_ACCEPTED;
            case 17:
                return Steps.RT_URGE;
            case 12:
                return Steps.RT_ASIGNED;
            case 13:
                return Steps.RT_ATTENDING;
            case 18:
                return Steps.RT_QUOTING;
            case 32:
                return Steps.RT_INVOICING;
            case 33:
                return Steps.RT_INVOICED;
            case 14:
                return Steps.RT_END;
            case 15:
                return Steps.RT_END;   
                
            case 27:
                return Steps.IS_REVIEW;  
            case 28:
                return Steps.IS_EDIT; 
            case 29:
                return Steps.IS_STAMPING;
            case 30:
                return Steps.IS_STAMPED; 
            case 31:
                return Steps.IS_SAVED;
                
            case 35:
                return Steps.SALES_QUOTATION;
            case 36:
                return Steps.SALES_REMISION;
            case 37:
                return Steps.SALES_INVOICE;
                
                
            default:
                return Steps.NOTHING;
        }
    }
    
    @Deprecated
    public Throwable fill(Database db,String code) 
    {
        String query = "SELECT * FROM " + MYSQL_AVATAR_TABLE + " WHERE module = 6 AND code = '" + code + "'";
        ResultSet rs;
        Throwable ret = null;
        
        java.sql.Statement stmt;
        try 
        {
            stmt = (Statement) db.getConnection().prepareStatement(query);
            rs = stmt.executeQuery(query);
            if(rs.next())
            {
                this.id = rs.getInt("id");
                this.code = code;
                this.name = rs.getString("name");
                this.ordinal = rs.getInt("ordinal");
                return null;
            }
            else
            {
                return new Exception("Fallo el relledo del estado de Servicio con cÃ³digo " + code + "'");
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return ex;
        }
    }

    
    public static ArrayList<State> selectAll(Database connection,Module module) throws SQLException
    {
        if(connection == null)
        {
            return null;
        }
        
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE module=" + module.getID() + " ORDER BY ordinal ASC";        
        //System.out.println(sql);
        ResultSet rs = connection.query(sql);
        ArrayList<State> states = new ArrayList<>();
        while(rs.next())
        {
            State state = new State(rs.getInt(1));
            state.download(connection);
            states.add(state);
        }
        if(states.size() > 0)
        {
            return states;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Seleciona el estado correspondiente al modulo y ordinal indicados.
     * @param connection
     * @param module
     * @param ordinal
     * @return
     * @throws SQLException 
     */
    public Return select(Connection connection,Module module,int ordinal) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT code,name,ordinal,module,id FROM " + MYSQL_AVATAR_TABLE + " WHERE module = " + module.getID() + " AND ordinal = " + ordinal;
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.code = rs.getString(1);
            this.name = rs.getString(2);
            this.ordinal = rs.getInt(3);
            this.module = new Module(rs.getInt(4));
            this.id = rs.getInt(5);
            return new Return(Return.Status.DONE);
        }
        else
        {
            clean();
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + id);
        }
    }
    
    
    /**
     * 
     * @param id 
     */
    public State(int id)
    {
        this.id = id;
    }
    
    /**
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the ordinal
     */
    public int getOrdinal() {
        return ordinal;
    }
    
    @Deprecated
    public Return download(Connection connection) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT code,`name`,ordinal,module FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + this.id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.code = rs.getString(1);
            this.name = rs.getString(2);
            this.ordinal = rs.getInt(3);
            this.module = new Module(rs.getInt(4));
            return new Return(Return.Status.DONE);
        }
        else
        {
            clean();
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + id);
        }
    }

    private void clean() 
    {
        code = null;
        name = null;
        ordinal = -1;
        module = null;
        id = -1;
    }   
    
    /**
     * 
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

    /**
     * @return the module
     */
    public Module getModule() {
        return module;
    }
}
