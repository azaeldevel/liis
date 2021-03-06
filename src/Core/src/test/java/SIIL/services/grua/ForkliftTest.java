
package SIIL.services.grua;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import database.mysql.purchases.Provider;
import database.mysql.stock.Titem;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class ForkliftTest 
{
    private static final boolean FL_COMMIT = true;
    
    public ForkliftTest() {
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
     * Test of selectRandom method, of class Forklift.
     */
    @Test
    public void testSelectRandom() throws Exception {
        System.out.println("selectRandom");
    }

    /**
     * Test of insert method, of class Forklift.
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
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;   
        
        Forklift item = new Forklift(-1);
        Provider prov = new Provider(-1);    
        try 
        {
            prov.selectRandom(dbserver.getConnection());
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        Return<Integer> result;
        Return<Integer> retMake = null;
        Return<Integer> retModel = null;
        
        try 
        {
            result = item.insert(dbserver.getConnection(),"TEST-" + sdf.format(date) + "-" + Result,"Montacaragas de Prueba." + sdf.format(date),Titem.Import.NoImport,false,"");
            retMake = item.upMake(dbserver.getConnection(), "SIIL");
            retModel = item.upModel(dbserver.getConnection(), "TEST");
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail( ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        int insertedID = 0;
        if(result.isFail())
        {
            fail(result.getMessage());
        }
        insertedID = result.getParam();
        if(retMake.isFlag() == false | retModel.isFlag() == false)
        {
            fail("Fall?? alguna operacion adjacentes.");
            return;
        }
        if(insertedID > 0 && FL_COMMIT)
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
        else if(insertedID > 0 && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
                fail("Fall?? RollBack " + ex.getMessage());
                return;
            }
            fail("Cant. Reg. incorrectos : " + insertedID);
        }
        dbserver.close();
    }
    
    /**
     * Test of getBattery method, of class Forklift.
     */
    @Test
    public void testGetBattery() {
        System.out.println("getBattery");
    }

    /**
     * Test of getCharger method, of class Forklift.
     */
    @Test
    public void testGetCharger() {
        System.out.println("getCharger");
    }

    /**
     * Test of getMina method, of class Forklift.
     */
    @Test
    public void testGetMina() {
        System.out.println("getMina");
    }

    /**
     * Test of getHorometro method, of class Forklift.
     */
    @Test
    public void testGetHorometro() {
        System.out.println("getHorometro");
    }
    
}
