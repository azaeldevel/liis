
package SIIL.trace;

import SIIL.Server.Database;
import SIIL.Server.MySQL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Azael
 */
public class Value
{
    private String table;
    private String field;
    private String before;
    private String after;
    private int traceID;
    private int ID;
    private String description;
    private String llave;
         
    public void download(Database conn, int ID) throws SQLException
    {
        String sql = "SELECT * FROM TraceDetail WHERE ID = ?";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setInt(1, ID);        
        ResultSet rs = pstmt.executeQuery();        
        if (rs.next())
        {
            this.ID = ID;
            traceID = rs.getInt("traceID");
            table = rs.getString("tabla");
            field = rs.getString("campo");
            description = rs.getString("descrip");
            llave = rs.getString("llave");   
            setObjectsValues(rs);
        }
    }
    
    private void setObjectsValues(ResultSet rs) throws SQLException
    {
        if(table.equals("Orcom"))
        {
            if(field.equals("ID"))
            {
                before = rs.getString("antes");
                after = rs.getString("despues");
            }
            else if(field.equals("folio"))
            {
                before = rs.getString("antes");
                after = rs.getString("despues");
            }
            else if(field.equals("estado"))
            {
                before = rs.getString("antes");
                after = rs.getString("despues");
            }
            else if(field.equals("fhAutho"))
            {
                if(rs.getString("antes") != null)
                {
                    Date dt = new Date(Long.valueOf(rs.getString("antes")));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getString("despues") != null)
                {
                    Date dt = new Date(Long.valueOf(rs.getString("despues")));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("fhETA"))
            {
                if(rs.getLong("antes") > 0)
                {
                    Date dt = new Date(rs.getLong("antes"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("fhArribo"))
            {
                if(rs.getLong("antes") > 0)
                {
                    Date dt = new Date(rs.getLong("antes"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("fhSurtido"))
            {
                if(rs.getLong("antes") > 0)
                {
                    Date dt = new Date(rs.getLong("antes"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("fhFin"))
            {
                if(!rs.getString("antes").equals("*"))
                {
                    if(rs.getLong("antes") > 0)
                    {

                        Date dt = new Date(rs.getLong("antes"));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                        before = sdf.format(dt);
                    }
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("note"))
            {
                if(rs.getLong("antes") > 0)
                {
                    Date dt = new Date(rs.getLong("antes"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("fhFolio"))
            {
                if(rs.getLong("antes") > 0)
                {
                    Date dt = new Date(rs.getLong("antes"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("docType"))
            {
                before = rs.getString("antes");
                after = rs.getString("despues");
            }
            else if(field.equals("creator"))
            {
                before = rs.getString("antes");
                after = rs.getString("despues");
            }
            else if(field.equals("ownerName"))
            {
                before = rs.getString("antes");
                after = rs.getString("despues");
            }
            else if(field.equals("ownerUser"))
            {
                before = rs.getString("antes");
                after = rs.getString("despues");
            }
            else if(field.equals("suc"))
            {
                before = rs.getString("antes");
                after = rs.getString("despues");
            }
            else if(field.equals("sa"))
            {
                before = rs.getString("antes");
                after = rs.getString("despues");
            }
            else if(field.equals("fhETAfl"))
            {
                if(rs.getLong("antes") > 0)
                {
                    Date dt = new Date(rs.getLong("antes"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("fhEdit"))
            {
                if(rs.getLong("antes") > 0)
                {
                    Date dt = new Date(rs.getLong("antes"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("fhFinfl"))
            {
                if(rs.getLong("antes") > 0)
                {
                    Date dt = new Date(rs.getLong("antes"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            }
            else if(field.equals("fhCancel"))
            {
                if(rs.getLong("antes") > 0)
                {
                    Date dt = new Date(rs.getLong("antes"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    before = sdf.format(dt);
                }
                
                if(rs.getLong("despues") > 0)
                {
                    Date dt = new Date(rs.getLong("despues"));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    after = sdf.format(dt);
                }
            } 
        }
        else
        {
            before = rs.getString("antes");
            after = rs.getString("despues");                
        }
    }
    
    /**
     * @return the table
     */
    public String getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return the before
     */
    public String getBefore() 
    {
        if(before == null)
        {
            return "";
        }
        else
        {
            return before;
        }
    }

    /**
     * @param before the before to set
     */
    public void setBefore(String before) {
        this.before = before;
    }

    /**
     * @return the after
     */
    public String getAfter() {
        return after;
    }

    /**
     * @param after the after to set
     */
    public void setAfter(String after) {
        this.after = after;
    }

    /**
     * @return the trace
     */
    public int getTrace() {
        return traceID;
    }

    /**
     * @param trace the trace to set
     */
    public void setTrace(int trace) {
        this.traceID = trace;
    }

    /**
     * @return the id
     */
    public int getID() {
        return ID;
    }

    /**
     * @param id the id to set
     */
    public void setID(int id) {
        this.ID = id;
    }

    /**
     * @return the description
     */
    public String getBrief() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setBrief(String description) {
        this.description = description;
    }

    /**
     * 
     * @param conn
     * @return
     * @throws SQLException 
     */
    public int insert(Database conn) throws SQLException 
    {
        //createID(conn);
        String sql = "INSERT INTO TraceDetail(traceID,tabla,campo,antes,despues,descrip,llave) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setInt(1, traceID);
        pstmt.setString(2, table);
        pstmt.setString(3, field);
        if(before == null)
        {
            pstmt.setNull(4, 0);
        }
        else
        {
            pstmt.setString(4, before.toString());
        }      
        if(after == null)
        {
            pstmt.setNull(5, 0);
        }
        else
        {
            pstmt.setString(5, after.toString());
        } 
        pstmt.setString(6, description);
        pstmt.setString(7, llave);
        
        return pstmt.executeUpdate();
    }
    
    /**
     * 
     * @param conn
     * @return
     * @throws SQLException 
     */
    public int insert(MySQL conn) throws SQLException 
    {
        //createID(conn);
        String sql = "INSERT INTO TraceDetail(traceID,tabla,campo,antes,despues,descrip,llave) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
        pstmt.setInt(1, traceID);
        pstmt.setString(2, table);
        pstmt.setString(3, field);
        if(before == null)
        {
            pstmt.setNull(4, 0);
        }
        else
        {
            pstmt.setString(4, before.toString());
        }        
        pstmt.setString(5, after.toString());
        pstmt.setString(6, description);
        pstmt.setString(7, llave);
        
        return pstmt.executeUpdate();
    }

    /**
     * @return the llave
     */
    public String getLlave() {
        return llave;
    }

    /**
     * @param llave the llave to set
     */
    public void setLlave(String llave) {
        this.llave = llave;
    }

    public void setTraceID(int trace) {
        traceID = trace;
    }
}
