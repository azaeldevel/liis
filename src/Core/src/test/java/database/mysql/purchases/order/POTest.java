
package database.mysql.purchases.order;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Azael Reyes
 */
public class POTest 
{
    private static final boolean FL_COMMIT = true;
    
    public POTest() 
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
    public void tearDown() 
    {
    }

    /**
     * Test of download method, of class PO.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of insert method, of class PO.
     */
    @Test
    public void testInsert() 
    {
        System.out.println("insert");
    }

    /**
     * Test of getDocumentoContable method, of class PO.
     */
    @Test
    public void testGetDocumentoContable() 
    {
        System.out.println("getDocumentoContable");
    }

    /**
     * Test of getDocumentTrace method, of class PO.
     */
    @Test
    public void testGetDocumentTrace() 
    {
        System.out.println("getDocumentTrace");
    }

    /**
     * Test of autoArrive method, of class PO.
     */
    @Test
    public void testAutoArrive() 
    {
        System.out.println("autoArrive");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        PO po = null;
        Credential credential = null;
        boolean ret = false;
        try 
        {
            credential = new Credential();
            String pass = "dev";
            User user = new User();
            user.setAlias("sistemas");
            user.setPassword(pass.toCharArray());
            user.download(dbserver);
            user.setBD("bc.tj");
            if(!credential.check(user, "alpha", serverConfig.getDataSource()))
            {
                fail("no se inicio sesion.");
                return;
            }
            po = new PO(16000);
            if(po.download(dbserver).isFlag())
            {
                po.downSerie(dbserver);
                po.downFolio(dbserver);
                ret = po.autoArrive(dbserver,credential,core.Renglon.selectStored(dbserver, po, false, null));
            }
            else
            {
                fail("No se encouentra el ID indicado");
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(POTest.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
                fail("Falló RollBack " + ex.getMessage());
                return;
            }
        }
        dbserver.Close();*/
    }

    /**
     * Test of selectQuotations method, of class PO.
     */
    @Test
    public void testSelectQuotations() throws Exception {
        System.out.println("selectQuotations");
    }

    /**
     * Test of upETA method, of class PO.
     */
    @Test
    public void testUpETA() throws Exception {
        System.out.println("upETA");
    }

    /**
     * Test of getFhETA method, of class PO.
     */
    @Test
    public void testGetFhETA() {
        System.out.println("getFhETA");
    }
    
}
