
package SIIL.service.quotation;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import core.bobeda.Business;
import java.io.IOException;
import java.sql.SQLException;
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

/**
 *
 * @author Azael Reyes
 */
public class ServiceQuotationTest 
{
    
    private static boolean FL_COMMIT = true;
    
    public ServiceQuotationTest() {
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
     * Test of downState method, of class ServiceQuotation.
     */
    @Test
    public void testDownState() 
    {
        System.out.println("downState");
    }

    /**
     * Test of downPAutho method, of class ServiceQuotation.
     */
    @Test
    public void testDownPAutho()  {
        System.out.println("downPAutho");
    }

    /**
     * Test of toString method, of class ServiceQuotation.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
    }

    /**
     * Test of searchByFolio method, of class ServiceQuotation.
     */
    @Test
    public void testSearchByFolio_4args_1()  {
        System.out.println("searchByFolio");
    }

    /**
     * Test of searchByFolio method, of class ServiceQuotation.
     */
    @Test
    public void testSearchByFolio_4args_2()  {
        System.out.println("searchByFolio");
    }

    /**
     * Test of getFullFolio method, of class ServiceQuotation.
     */
    @Test
    public void testGetFullFolio_0args() {
        System.out.println("getFullFolio");
    }

    /**
     * Test of getFullFolio method, of class ServiceQuotation.
     */
    @Test
    public void testGetFullFolio_Connection() 
    {
        System.out.println("getFullFolio");
    }

    /**
     * Test of upSerie method, of class ServiceQuotation.
     */
    @Test
    public void testUpSerie() 
    {
        System.out.println("upSerie");        
    }

    /**
     * Test of downSerie method, of class ServiceQuotation.
     */
    @Test
    public void testDownSerie() 
    {
        System.out.println("downSerie");        
    }

    /**
     * Test of downQuotation method, of class ServiceQuotation.
     */
    @Test
    public void testDownQuotation() 
    {
        System.out.println("downQuotation");
    }

    /**
     * Test of upQuotation method, of class ServiceQuotation.
     */
    @Test
    public void testUpQuotation() 
    {
        System.out.println("upQuotation");
    }

    /**
     * Test of getFail method, of class ServiceQuotation.
     */
    @Test
    public void testGetFail() {
        System.out.println("getFail");
        
    }

    /**
     * Test of updateFin method, of class ServiceQuotation.
     */
    @Test
    public void testUpdateFin()  {
        System.out.println("updateFin");
    }

    /**
     * Test of updateSurtir method, of class ServiceQuotation.
     */
    @Test
    public void testUpdateSurtir() 
    {
        System.out.println("updateSurtir");
    }

    /**
     * Test of updateArribo method, of class ServiceQuotation.
     */
    @Test
    public void testUpdateArribo() 
    {
        System.out.println("updateArribo");
        
    }

    /**
     * Test of updateEdit method, of class ServiceQuotation.
     */
    @Test
    public void testUpdateEdit() 
    {
        System.out.println("updateEdit");
        
    }

    /**
     * Test of setCredential method, of class ServiceQuotation.
     */
    @Test
    public void testSetCredential() {
        System.out.println("setCredential");
        
    }

    /**
     * Test of getOwner method, of class ServiceQuotation.
     */
    @Test
    public void testGetOwner() {
        System.out.println("getOwner");
        
    }

    /**
     * Test of getEntreprise method, of class ServiceQuotation.
     */
    @Test
    public void testGetEntreprise() {
        System.out.println("getEntreprise");
        
    }

    /**
     * Test of getTechnical method, of class ServiceQuotation.
     */
    @Test
    public void testGetTechnical() {
        System.out.println("getTechnical");
        
    }

    /**
     * Test of setTechnical method, of class ServiceQuotation.
     */
    @Test
    public void testSetTechnical() {
        System.out.println("setTechnical");
        
    }

    /**
     * Test of setFolio method, of class ServiceQuotation.
     */
    @Test
    public void testSetFolio() {
        System.out.println("setFolio");
        
    }

    /**
     * Test of getCredential method, of class ServiceQuotation.
     */
    @Test
    public void testGetCredential() {
        System.out.println("getCredential");
        
    }

    /**
     * Test of fill method, of class ServiceQuotation.
     */
    @Test
    public void testFill() {
        System.out.println("fill");
        
    }

    /**
     * Test of getProgressObject method, of class ServiceQuotation.
     */
    @Test
    public void testGetProgressObject() {
        System.out.println("getProgressObject");
        
    }

    /**
     * Test of setProgressObject method, of class ServiceQuotation.
     */
    @Test
    public void testSetProgressObject() {
        System.out.println("setProgressObject");
        
    }

    /**
     * Test of getServerDB method, of class ServiceQuotation.
     */
    @Test
    public void testGetServerDB() {
        System.out.println("getServerDB");
        
    }

    /**
     * Test of setServerDB method, of class ServiceQuotation.
     */
    @Test
    public void testSetServerDB() {
        System.out.println("setServerDB");
        
    }

    /**
     * Test of fillDetail method, of class ServiceQuotation.
     */
    @Test
    public void testFillDetail() {
        System.out.println("fillDetail");
        
    }

    /**
     * Test of getFhCretion method, of class ServiceQuotation.
     */
    @Test
    public void testGetFhCretion() {
        System.out.println("getFhCretion");
        
    }

    /**
     * Test of setFhCretion method, of class ServiceQuotation.
     */
    @Test
    public void testSetFhCretion() {
        System.out.println("setFhCretion");
        
    }

    /**
     * Test of getFhEdit method, of class ServiceQuotation.
     */
    @Test
    public void testGetFhEdit() {
        System.out.println("getFhEdit");
        
    }

    /**
     * Test of setFhEdit method, of class ServiceQuotation.
     */
    @Test
    public void testSetFhEdit() {
        System.out.println("setFhEdit");
        
    }

    /**
     * Test of getFhETAfl method, of class ServiceQuotation.
     */
    @Test
    public void testGetFhETAfl() {
        System.out.println("getFhETAfl");
        
    }

    /**
     * Test of setFhETAfl method, of class ServiceQuotation.
     */
    @Test
    public void testSetFhETAfl() {
        System.out.println("setFhETAfl");
        
    }

    /**
     * Test of getFhEnd method, of class ServiceQuotation.
     */
    @Test
    public void testGetFhEnd() {
        System.out.println("getFhEnd");
        
    }

    /**
     * Test of setFhEnd method, of class ServiceQuotation.
     */
    @Test
    public void testSetFhEnd() {
        System.out.println("setFhEnd");
        
    }

    /**
     * Test of getFhSurtir method, of class ServiceQuotation.
     */
    @Test
    public void testGetFhSurtir() {
        System.out.println("getFhSurtir");
        
    }

    /**
     * Test of setFhSurtir method, of class ServiceQuotation.
     */
    @Test
    public void testSetFhSurtir() {
        System.out.println("setFhSurtir");
        
    }

    /**
     * Test of setTerminalComment method, of class ServiceQuotation.
     */
    @Test
    public void testSetTerminalComment() {
        System.out.println("setTerminalComment");
        
    }

    /**
     * Test of getTerminalComment method, of class ServiceQuotation.
     */
    @Test
    public void testGetTerminalComment() {
        System.out.println("getTerminalComment");
    }

    /**
     * Test of getQuotation method, of class ServiceQuotation.
     */
    @Test
    public void testGetQuotation() {
        System.out.println("getQuotation");
    }

    /**
     * Test of getPAutho method, of class ServiceQuotation.
     */
    @Test
    public void testGetPAutho() {
        System.out.println("getPAutho");
    }

    /**
     * Test of download method, of class ServiceQuotation.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of getPOFile method, of class ServiceQuotation.
     */
    @Test
    public void testGetPOFile() {
        System.out.println("getPOFile");
    }

    /**
     * Test of upPOFile method, of class ServiceQuotation.
     */
    @Test
    public void testUpPOFile()  
    {
        System.out.println("upPOFile");
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
        
        ServiceQuotation serviceQuotation = new ServiceQuotation();
        Business business = new Business(null);
        Return ret = null;
        try 
        {
            if(business.selectRandom(dbserver) && serviceQuotation.selectRandom(dbserver.getConnection()).isFlag())
            {                
                serviceQuotation.download(dbserver);
                ret = serviceQuotation.upPOFile(dbserver,business);                
            }
            else
            {
                fail("Fallo al inicializar los objetos.");
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(ServiceQuotationTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

        if(ret.isFlag() && FL_COMMIT)
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
        else if(ret.isFail() && FL_COMMIT == false)
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
        dbserver.close();
    }

    /**
     * Test of importFromCN method, of class ServiceQuotation.
     */
    @Test
    public void testImportFromCN() 
    {
        System.out.println("importFromCN");
        /*
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            
        }
        
        ServiceQuotation serviceQuotation = new ServiceQuotation();
        serviceQuotation.select(dbserver, 7982);
        
        COTIZACI tbCotiza;
        Office office = new Office(1); 
        Return ret = null;
        try 
        {
            tbCotiza = new COTIZACI(Sucursal.BC_Tijuana);
            ret = serviceQuotation.importFromCN(dbserver, tbCotiza, office);
            tbCotiza.close();
        }
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(ServiceQuotationTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        Quotation operational = serviceQuotation.getQuotation();
        try 
        {
            operational.downTotal(dbserver);
            if(operational.downOffice(dbserver)) 
            {
                operational.getOffice().download(dbserver.getConnection());
            }
            else
            {
                fail("Fallo");
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ServiceQuotationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(ret.isFlag() && FL_COMMIT)
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
        else if(ret.isFail() && FL_COMMIT == false)
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
        dbserver.close();
        */
    }

    /**
     * Test of downFhETA method, of class ServiceQuotation.
     */
    @Test
    public void testDownFhETA() 
    {
        System.out.println("downFhETA");
        
    }

    /**
     * Test of downOwner2 method, of class ServiceQuotation.
     */
    @Test
    public void testDownOwner2() 
    {
        System.out.println("downOwner2");
    }

    /**
     * Test of downOwner method, of class ServiceQuotation.
     */
    @Test
    public void testDownOwner() 
    {
        System.out.println("downOwner");
    }

    /**
     * Test of getSamePO method, of class ServiceQuotation.
     */
    @Test
    public void testGetSamePO()
    {
        System.out.println("getSamePO");
    }

    /**
     * Test of downPOFile method, of class ServiceQuotation.
     */
    @Test
    public void testDownPOFile()
    {
        System.out.println("downPOFile");
    }

    /**
     * Test of getOwner2 method, of class ServiceQuotation.
     */
    @Test
    public void testGetOwner2() 
    {
        System.out.println("getOwner2");
    }

    /**
     * Test of getBusinesDocument method, of class ServiceQuotation.
     */
    @Test
    public void testGetBusinesDocument() 
    {
        System.out.println("getBusinesDocument");        
    }

    /**
     * Test of getFolioInTable method, of class ServiceQuotation.
     */
    @Test
    public void testGetFolioInTable() {
        System.out.println("getFolioInTable");
    }

    /**
     * Test of downloadDataVault method, of class ServiceQuotation.
     */
    @Test
    public void testDownloadDataVault()
    {
        System.out.println("downloadDataVault");
    }
    
}
