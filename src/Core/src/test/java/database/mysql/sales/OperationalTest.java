
package database.mysql.sales;

import SAT.CatalogoTest;
import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import process.Return;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class OperationalTest 
{
    private static final boolean FL_COMMIT = true;
    
    public OperationalTest() {
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
     * Test of insert method, of class Operational.
     */
    @Test
    public void testInsert()
    {
        System.out.println("insert");
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low; 
        Connection connection = dbserver.getConnection();
        
        process.State state = new process.State(-1);
        try 
        {
            state.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la selecion de stado : " + ex.getMessage());
        }
        Person operator = new Person();
        try 
        {
            operator.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la seleccion de operador : " + ex.getMessage());
        }
        Enterprise company = new Enterprise();
        try 
        {
            company.selectRandom(dbserver);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Office office = new Office(-1);
        try 
        {
            office.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        database.mysql.sales.Operational instance = new database.mysql.sales.Operational(-1);        
        Return result = null;
        try 
        {
            result = instance.insert(dbserver,office, state,operator,dbserver.getTimestamp(),company,Result,"DEV","TEST");
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la inserccion de la operacion : " + ex.getMessage());
        }        

        if(result.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(result.isFlag() && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                fail("Falló RollBack " + ex.getMessage());
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
            fail("Cant. Reg. incorrectos : " );
        }
        dbserver.close();
    }
    
}
