
package SIIL.Server;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author areyes
 */
public class DatabaseTest {
    
    public DatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of update method, of class Database.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
    }

    /**
     * Test of getSQL method, of class Database.
     */
    @Test
    public void testGetSQL() {
        System.out.println("getSQL");
    }

    /**
     * Test of insert method, of class Database.
     */
    @Test
    public void testInsert_String_int() {
        System.out.println("insert");
    }

    /**
     * Test of INSERT method, of class Database.
     */
    @Test
    public void testINSERT() throws Exception {
        System.out.println("INSERT");
        String sqlStr = "";
        Database instance = null;
    }

    /**
     * Test of insert method, of class Database.
     */
    @Test
    public void testInsert_String() {
        System.out.println("insert");
    }

    /**
     * Test of select method, of class Database.
     */
    @Test
    public void testSelect() {
        System.out.println("select");
    }

    /**
     * Test of query method, of class Database.
     */
    @Test
    public void testQuery() throws Exception {
        System.out.println("query");
    }

    /**
     * Test of getContext method, of class Database.
     */
    @Test
    public void testGetContext() {
        System.out.println("getContext");
    }

    /**
     * Test of createContex method, of class Database.
     */
    @Test
    public void testCreateContex_SQLDialect_MysqlDataSource() throws Exception 
    {
        System.out.println("createContex");
    }

    /**
     * Test of createContex method, of class Database.
     */
    @Test
    public void testCreateContex_SQLServerDataSource() throws Exception 
    {
        System.out.println("Conectando a al servidor SQL Server...");
        /*
        SQLServerDataSource ssqlds = new SQLServerDataSource();
        ssqlds.setUser("sa");
        ssqlds.setPassword("Trycer2000");
        ssqlds.setServerName("192.168.1.200\\COMPAC");
        //ssqlds.setPortNumber(1433);
        ssqlds.setDatabaseName("SIIL_COMERCIAL_DEVEL");
        Connection conssqlds = ssqlds.getConnection();
        conssqlds.setReadOnly(true);
        //>>>>>>
        String userName = "sa";
        String password = "Trycer2000";
        String url = "jdbc:sqlserver://192.168.1.200\\COMPAC;databaseName=SIIL_COMERCIAL_DEV";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conssqlds = DriverManager.getConnection(url, userName, password);
        //<<<<<<<<<<       
        if(conssqlds == null)
        {
            fail("Fallo la conexion a SQL Server.");
        }
        
        String sqlstr = "SELECT * FROM dbo.engUser";
        Statement stmt = conssqlds.createStatement();
        ResultSet rs = stmt.executeQuery(sqlstr);
        while(rs.next())
        {
            System.out.println(rs.getString(4));
        }
        conssqlds.close();
        */    
        
        
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig.getComercial());
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        catch (IOException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        catch (SAXException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        String sqlstr = "SELECT * FROM dbo.engUser";
        Statement stmt  = dbserver.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sqlstr);
        while(rs.next())
        {
            System.out.println(rs.getString(4));
        }     */  
        
    }

    /**
     * Test of createContex method, of class Database.
     */
    @Test
    public void testCreateContex_MysqlDataSource() throws Exception 
    {
        System.out.println("createContex");
    }

    /**
     * Test of getDialect method, of class Database.
     */
    @Test
    public void testGetDialect() {
        System.out.println("getDialect");
    }

    /**
     * Test of getConnection method, of class Database.
     */
    @Test
    public void testGetConnection() 
    {
        System.out.println("getConnection");
    }

    /**
     * Test of valid method, of class Database.
     */
    @Test
    public void testValid() 
    {
        System.out.println("valid");
    }

    /**
     * Test of commit method, of class Database.
     */
    @Test
    public void testCommit() throws Exception 
    {
        System.out.println("commit");
    }

    /**
     * Test of rollback method, of class Database.
     */
    @Test
    public void testRollback() throws Exception 
    {
        System.out.println("rollback");
    }

    /**
     * Test of getTimestamp method, of class Database.
     */
    @Test
    public void testGetTimestamp() throws Exception 
    {
        System.out.println("getTimestamp");
    }

    /**
     * Test of getDateToday method, of class Database.
     */
    @Test
    public void testGetDateToday() throws Exception 
    {
        System.out.println("getDateToday");
    }

    /**
     * Test of getDateMonth method, of class Database.
     */
    @Test
    public void testGetDateMonth() throws Exception 
    {
        System.out.println("getDateMonth");
    }

    /**
     * Test of getDateYear method, of class Database.
     */
    @Test
    public void testGetDateYear() throws Exception 
    {
        System.out.println("getDateYear");
    }

    /**
     * Test of close method, of class Database.
     */
    @Test
    public void testClose() 
    {
        System.out.println("close");
    }
    
}
