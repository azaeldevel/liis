
package SIIL.Server;

import SIIL.artifact.AmbiguosException;
import SIIL.artifact.DeployException;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

/**
 *
 * @author Azael
 */
public class Database
{
    @Deprecated
    private DSLContext context;
    String phase;
    private Connection connection;
    private java.sql.Statement stmt;
    private String strSQL;
       

    @Deprecated
    public int update(String sqlStr) 
    {
        try
        {
            String sql = "UPDATE " + sqlStr;
            System.out.println(sql);
            strSQL = sql;
            int r = connection.createStatement().executeUpdate(sql);
            //conn.commit();
            return r;            
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        
        return 0;
    }
    
    public String getSQL() 
    {
        return strSQL;
    }
    
    @Deprecated
    public int insert(String sqlStr, int autoGeneratedKeys) 
    {
        stmt = null;
        try
        {
            String sql = "INSERT INTO " + sqlStr;
            stmt = connection.createStatement();
            System.out.println(sql);
            strSQL = sql;
            int r = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
            if(r > 0)
            {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) 
                {
                    return rs.getInt(1);
                }
            }
            else
            {
                connection.rollback();
                return -1;            
            }                
            return -1;
        }
        catch(SQLException se)
        {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, se);
        }
        return -1;
   }
    
    @Deprecated
    public int INSERT(String sqlStr) throws SQLException 
    {
        stmt = connection.createStatement();
        String sql = "INSERT INTO " + sqlStr;
        //System.out.println(sql);
        strSQL = sql;
        return stmt.executeUpdate(sql);
    }
    
    @Deprecated
    public int insert(String sqlStr) 
    {
        stmt = null;
        try
        {
            stmt = connection.createStatement();
            String sql = "INSERT INTO " + sqlStr;
            System.out.println(sql);
            strSQL = sql;
            int r = stmt.executeUpdate(sql);
            if(r > 0)
            {
                //conn.commit();            
            }
            else
            {
                connection.rollback();            
            }                
            return r;
        }
        catch(SQLException se)
        {
            return 0;
        }
   }
    
    ResultSet select(String sql) 
    {
        Statement stmt = null;
        try
        {
            //for(int i=0;i < emprs.size();i++)
            {                
                //System.out.println(sql);
                if(connection == null)
                {
                    throw new Exception("No hay una conexion activa");
                }
                stmt = (Statement) connection.createStatement();            
                ResultSet rs = stmt.executeQuery(sql);
                return rs;                
            }
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        catch(Exception e)
        {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        
        return null;
    }
    
    public ResultSet query(String sql) throws SQLException
    {
        java.sql.Statement stmt = connection.createStatement();
        //System.out.println(sql);
        return stmt.executeQuery(sql);
    }
    
    @Deprecated
    public DSLContext getContext()
    {
        return context;
    }

    public Database(SIIL.core.config.Server config) throws ClassNotFoundException, SQLException
    {
        createContex(config.getDataSource());
    }
    
    public Database(SQLServerDataSource ds) throws ClassNotFoundException, SQLException
    {
        createContex(ds);
    }
    
    public Database(MysqlDataSource ds) throws ClassNotFoundException, SQLException
    {
        createContex(ds);
    }
    
    @Deprecated
    public Database(SQLDialect dialect,String phase,MysqlDataSource ds) throws SQLException, DeployException, AmbiguosException, ClassNotFoundException
    {
        this.phase = phase;
        createContex(dialect,ds);
    }
    
    @Deprecated
    public Database(String phase,MysqlDataSource ds) throws SQLException, DeployException, AmbiguosException, ClassNotFoundException
    {
        this.phase = phase;
        createContex(SQLDialect.MYSQL,ds);
    }  
        
    @Deprecated
    final DSLContext createContex(SQLDialect dialect,MysqlDataSource ds) throws SQLException, DeployException, AmbiguosException, ClassNotFoundException
    {
        //HashMap<String,String> params = getParamsConnection();
        Class.forName("com.mysql.jdbc.Driver");
        //ds.setPassword("123456ar");
        connection = ds.getConnection();
        
        connection.setAutoCommit(false);
        // El esquema fue generado para una base de datos con nombre diferente, 
        // sin embargo en la url se indica el nombre correcto withRenderSchema(false) 
        // evita que se tome el nombre por default
        Settings settings = new Settings().withRenderSchema(false);
        context = DSL.using(connection, dialect,settings);
        return context;
    }
    
    final DSLContext createContex(SQLServerDataSource ds) throws ClassNotFoundException, SQLException
    {
        if(ds == null) return null;
        //Class.forName("com.mysql.jdbc.Driver");        
        connection = ds.getConnection();
        
        connection.setAutoCommit(false);
        connection.setReadOnly(true);
        // El esquema fue generado para una base de datos con nombre diferente, 
        // sin embargo en la url se indica el nombre correcto withRenderSchema(false) 
        // evita que se tome el nombre por default
        Settings settings = new Settings().withRenderSchema(false);
        context = DSL.using(connection,SQLDialect.MYSQL,settings);
        return context;
    }
    final DSLContext createContex(MysqlDataSource ds) throws ClassNotFoundException, SQLException
    {
        if(ds == null) return null;
        Class.forName("com.mysql.jdbc.Driver");  
        //ds.setCharacterEncoding("ISO_8859_1");
        connection = ds.getConnection();
        
        connection.setAutoCommit(false);
        // El esquema fue generado para una base de datos con nombre diferente, 
        // sin embargo en la url se indica el nombre correcto withRenderSchema(false) 
        // evita que se tome el nombre por default
        Settings settings = new Settings().withRenderSchema(false);
        context = DSL.using(connection,SQLDialect.MYSQL,settings);
        return context;
    }

    private HashMap<String,String> getParamsConnection()
    {
        HashMap<String,String> params = new HashMap<String,String>();
                
        params.put("driver","com.mysql.jdbc.Driver"); 
        params.put("user","application");
        params.put("password","KLOIy!hg68:,i-'¡s2o@5"); 
        
        if (phase.equals("alpha")) 
        {
            params.put("url","jdbc:mysql://192.168.1.200/dbssiila");
        }
        else if (phase.equals("beta"))
        {
            params.put("url","jdbc:mysql://192.168.1.200/DBSSIILbr");
        }
        else if (phase.equals("release"))
        {
            params.put("url","jdbc:mysql://192.168.1.200/DBSSIIL");
        }
        
        return params;
    }

    /**
     * @return the dialect
     */
    public SQLDialect getDialect() {
        return context.dialect();
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    public boolean valid() 
    {
        if(context.dialect() != null)
        {
            if(context != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(connection != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public void commit() throws SQLException 
    {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public Timestamp getTimestamp() throws SQLException 
    {
        String sql = "SELECT NOW()";
        Statement pstmt = (Statement) connection.createStatement();
        ResultSet rs = pstmt.executeQuery(sql);     
        if(rs.next())
        {
            return rs.getTimestamp("NOW()");
        }
        
        return null;
    }
    public Date getDateToday() throws SQLException 
    {
        String sql = "SELECT NOW()";
        Statement pstmt = (Statement) connection.createStatement();
        ResultSet rs = pstmt.executeQuery(sql);     
        if(rs.next())
        {
            Date d = rs.getDate("NOW()");
            return d;
        }
        
        return null;
    }
    public int getDateMonth() throws SQLException 
    {
        String sql = "SELECT MONTH(NOW()) as month";
        Statement pstmt = (Statement) connection.createStatement();
        ResultSet rs = pstmt.executeQuery(sql);     
        if(rs.next())
        {
            int m = rs.getInt("month");
            return m;
        }        
        return 0;
    }
    public int getDateYear() throws SQLException 
    {
        String sql = "SELECT YEAR(NOW()) as year";
        Statement pstmt = (Statement) connection.createStatement();
        ResultSet rs = pstmt.executeQuery(sql);     
        if(rs.next())
        {
            int y = rs.getInt("year");
            return y;
        }        
        return 0;
    }
    public void close() 
    {
        try 
        {
            if(connection != null)connection.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
