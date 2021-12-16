
package database.mysql.sales;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.services.order.Association;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
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
public class RemisionTest 
{
    private static final boolean FL_COMMIT = true;
    
    public RemisionTest() {
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
     * Test of insert method, of class Remision.
     */
    @Test
    public void testInsert() throws Exception {
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
        
        process.State state = new process.State(-1);
        try 
        {
            state.selectRandom(dbserver.getConnection());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la selecion de stado : " + ex.getMessage());
        }
        Person operator = new Person();
        try 
        {
            operator.selectRandom(dbserver.getConnection());
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
            office.selectRandom(dbserver.getConnection());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        database.mysql.sales.Remision instance = new database.mysql.sales.Remision(-1);        
        Return result = null;
        try 
        {
            result = instance.insert(dbserver,office, state,operator,dbserver.getTimestamp(),company,Result,"DEV","SR");
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
            fail("Cant. Reg. incorrectos : ");
        }
        dbserver.close();
    }

    /**
     * Test of downOrdenesArchivo method, of class Remision.
     */
    @Test
    public void testDownOrdenesArchivo()
    {
        System.out.println("downOrdenesArchivo");
    }

    /**
     * Test of massAssociationCleanDirectory method, of class Remision.
     */
    @Test
    public void testMassAssociationCleanDirectory() 
    {
        System.out.println("massAssociationCleanDirectory");
        
    }

    /**
     * Test of massAssociation method, of class Remision.
     */
    @Test
    public void testMassAssociation() 
    {
        System.out.println("massAssociation");
    }

    /**
     * Test of massAssociationReadDirectory method, of class Remision.
     */
    @Test
    public void testMassAssociationReadDirectory() 
    {
        System.out.println("massAssociationReadDirectory");
    }

    /**
     * Test of upArchivoOS method, of class Remision.
     */
    @Test
    public void testUpArchivoOS() 
    {
        System.out.println("upArchivoOS");
    }

    /**
     * Test of generate method, of class Remision.
     */
    @Test
    public void testGenerate() 
    {
        System.out.println("generate");
    }

    /**
     * Test of search method, of class Remision.
     */
    @Test
    public void testSearch_3args() 
    {
        System.out.println("search");
    }

    /**
     * Test of fromCN2 method, of class Remision.
     */
    @Test
    public void testFromCN2() 
    {
        System.out.println("fromCN2");
    }

    /**
     * Test of fromCN method, of class Remision.
     */
    @Test
    public void testFromCN() 
    {
        System.out.println("fromCN");
    }

    /**
     * Test of search method, of class Remision.
     */
    @Test
    public void testSearch_4args() 
    {
        System.out.println("search");
    }

    /**
     * Test of selectRandom method, of class Remision.
     */
    @Test
    public void testSelectRandom() 
    {
        System.out.println("selectRandom");
    }

    /**
     * Test of getIdentificator method, of class Remision.
     */
    @Test
    public void testGetIdentificator() 
    {
        System.out.println("getIdentificator");
    }

    /**
     * Test of getBrief method, of class Remision.
     */
    @Test
    public void testGetBrief() 
    {
        System.out.println("getBrief");
    }

    /**
     * Test of getArchivoOS method, of class Remision.
     */
    @Test
    public void testGetArchivoOS() 
    {
        System.out.println("getArchivoOS");
    }
    
}
