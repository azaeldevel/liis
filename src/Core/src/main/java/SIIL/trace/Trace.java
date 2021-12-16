
package SIIL.trace;

import SIIL.Server.Database;
import java.security.InvalidParameterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azael
 */
public class Trace
{
    private static final String MYSQL_AVATAR_TABLE = "Trace";
    
    public boolean upBrief(Database db, String brief) throws SQLException
    {
        if(ID < 1)
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
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET brief='" + brief + "' WHERE id=" + ID;
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
    
    
    public int addOperator(Database db,String table) throws SQLException
    {
        Value val = new Value();
        val = new Value();
        val.setTraceID(getTrace());
        val.setTable(table);
        val.setField("operator");
        val.setAfter(user.toString());
        val.setBrief("¿Quien hace?");
        val.setLlave("-");
        return insert(db);
    }
    
    static ArrayList<Value> downDetail(Database conn, Trace trace) throws SQLException 
    {
        ArrayList<Value> listValues = new ArrayList<>();
        String sql = "SELECT ID,traceID FROM TraceDetail WHERE traceID = ? ";

        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setInt(1, trace.getTraceID());
        ResultSet rs = pstmt.executeQuery();
        while (rs.next())
        {
            Value value = new Value();
            value.download(conn,rs.getInt("ID"));
            listValues.add(value);
        }
        return listValues;
    }
    
    static List<Trace> fillSearch(Database conn, String search, int count) throws SQLException 
    {
        if(search.length() < 0 || count < 1)
        {
            return null;
        }
        
        ArrayList<Trace> listTrace = new ArrayList<>();
        String sql = "SELECT * FROM Trace WHERE brief LIKE '%" + search + "%' OR user LIKE '%" + search + "%' OR trace LIKE '%" + search + "%' ORDER BY ID DESC LIMIT " + count;
        ResultSet rs;
        
        Statement stmt;
        
        stmt = (Statement) conn.getConnection().createStatement();
        rs = stmt.executeQuery(sql);
        while (rs.next())
        {
            Trace t = new Trace();
            t.setID(rs.getInt("ID"));
            SIIL.Server.User user = new SIIL.Server.User();
            user.setAlias(rs.getString("user"));
            user.down(conn);
            t.setUser(user);
            t.setBrief(rs.getString("brief"));
            t.setDate(rs.getDate("fhTrace"));
            t.traceID = rs.getInt("trace");
            listTrace.add(t);
        }
        
        return listTrace;
    }

    static List<Trace> fill(Database conn, String search, int count) throws SQLException 
    {
        ArrayList<Trace> listTrace = new ArrayList<>();
        String sql = "SELECT * FROM Trace WHERE brief LIKE '%" + search + "%' OR trace LIKE '%" + search + "%' ORDER BY ID DESC LIMIT " + count;
        ResultSet rs;
        
        Statement stmt;
        
        stmt = (Statement) conn.getConnection().createStatement();
        rs = stmt.executeQuery(sql);
        while (rs.next())
        {
            Trace t = new Trace();
            t.setID(rs.getInt("ID"));
            SIIL.Server.User user = new SIIL.Server.User();
            user.setAlias(rs.getString("user"));
            user.down(conn);
            t.setUser(user);
            t.setBrief(rs.getString("brief"));
            t.setDate(rs.getDate("fhTrace"));
            t.traceID = rs.getInt("trace");
            listTrace.add(t);
        }
        
        return listTrace;
    }
    
    public Trace()
    {
        
    }

    static ArrayList<Trace> fill(Database conn) throws SQLException 
    {
        ArrayList<Trace> listTrace = new ArrayList<>();
        String sql = "SELECT * FROM Trace LIMIT 1000 ";
        ResultSet rs;
        
        Statement stmt;
        
        stmt = (Statement) conn.getConnection().createStatement();
        rs = stmt.executeQuery(sql);
        while (rs.next())
        {
            Trace t = new Trace();
            t.setID(rs.getInt("ID"));
            SIIL.Server.User user = new SIIL.Server.User();
            user.setAlias(rs.getString("user"));
            user.down(conn);
            t.setUser(user);
            t.setBrief(rs.getString("brief"));
            t.setDate(rs.getDate("fhTrace"));
            t.traceID = rs.getInt("trace");
            listTrace.add(t);
        }
        
        return listTrace;
    }
    
    
    private SIIL.Server.User user;
    private Date fhTrace;
    private String brief;
    private int ID;
    private int traceID;
    private String BD;
    private int index;

    public int getTrace()
    {
        return getTraceID();
    }
    
    private void createID(Database conn)
    {
        String sql = "SELECT MAX(trace) FROM Trace";
        ResultSet rs;
        
        Statement stmt;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                traceID = rs.getInt("MAX(trace)") + 1;
            }
            else
            {
                traceID = 0;
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*@Deprecated
    private void createID(Database conn)
    {
        String sql = "SELECT MAX(trace) FROM Trace";
        ResultSet rs;
        
        Statement stmt;
        try 
        {
            stmt = (com.mysql.jdbc.Statement) conn.getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                traceID = rs.getInt("MAX(trace)") + 1;
            }
            else
            {
                traceID = 0;
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    public Trace(String bd, SIIL.Server.User u, String b)
    {
        user = u;
        brief = b;
        BD = bd;
    }

    public int insert(Database conn) throws SQLException 
    {
        createID(conn);  
        
        String sql = "INSERT INTO Trace(trace,BD,user,fhTrace,brief) VALUES(" + getTraceID() + ",'" + getBD()+ "','" + getUser().getAlias() +"',NOW(),'" + getBrief() + "')";                
        Statement stmt = conn.getConnection().createStatement();
        //System.out.println(sql);
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        int generated = -1;        
        if (rs.next())
        {
            ID = rs.getInt(1);
        }
        else
        {
            ID = -1;
        }
        
        return affected;
    }
    

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the user
     */
    public SIIL.Server.User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(SIIL.Server.User user) {
        this.user = user;
    }

    /**
     * @return the brief
     */
    public String getBrief() {
        return brief;
    }

    /**
     * @param brief the brief to set
     */
    public void setBrief(String brief) {
        this.brief = brief;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }
    
    /*public void setTraceID(int trace)
    {
        traceID = trace;
    }*/

    /**
     * @return the traceID
     */
    public int getTraceID() {
        return traceID;
    }

    /**
     * @return the BD
     */
    public String getBD() {
        return BD;
    }

    /**
     * @param BD the BD to set
     */
    public void setBD(String BD) {
        this.BD = BD;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return fhTrace;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.fhTrace = date;
    }
    
}
