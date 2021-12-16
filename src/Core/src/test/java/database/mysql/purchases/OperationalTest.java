
package database.mysql.purchases;

import SIIL.Server.Database;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Azael Reyes
 */
public class OperationalTest 
{    
    private static boolean FL_COMMIT = true;
    
    public OperationalTest() 
    {
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
    }
    
    @Before
    public void setUp() 
    {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of upETA method, of class Operational.
     */
    @Test
    public void testUpETA() 
    {
        System.out.println("upETA");
       /* Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddhhmm");
        Random r = new Random();        
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        
        Operational operational = new Operational(-1);
        boolean ret = false;
        try 
        {        
            operational.selectRandom(dbserver.getConnection());
            ret = operational.upETA(dbserver, date);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        if(ret && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(ret && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
                return;
            }
            //fail("Cant. Reg. incorrectos : ");
        }
        dbserver.Close();*/
    }

    /**
     * Test of download method, of class Operational.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of insert method, of class Operational.
     */
    @Test
    public void testInsert() 
    {
        System.out.println("insert");        
    }

    /**
     * Test of getProvider method, of class Operational.
     */
    @Test
    public void testGetProvider() 
    {
        System.out.println("getProvider");        
    }    
}
