
package process;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import sales.Invoice;
import core.bobeda.FTP;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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

/**
 *
 * @author Azael Reyes
 */
public class AttachedTest 
{
    private static final boolean FL_COMMIT = true;
    
    public AttachedTest() 
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
     * Test of importCN method, of class Attached.
     */
    @Test
    public void testImportCN()
    {
        System.out.println("importCN");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
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
        
        
        FTP ftpServer = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = ftpServer.connect(serverConfig);
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        if(!expResult)
        {
            fail("Fallo la conexion al servidor de archivos");
        }
        Office office = new Office(-1);
        Return ret = new Return(true);
        try 
        {
            office.selectCode(dbserver, "bc.tj");
            office.download(dbserver.getConnection());            
            Invoice factura = Invoice.loadFromCN(new File(SIIL.CN.Tables.CN60.DIR_CN60_TJ), "A", 29520);
            Enterprise comp = factura.getEnterprise(dbserver);
            comp.downMailFact(dbserver);
            ret = Attached.importCN(dbserver, ftpServer, office,factura,comp.getEmailFactura(office),comp,true);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AttachedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        catch (Exception ex) 
        {
            fail(ex.getMessage());
            //Logger.getLogger(AttachedTest.class.getName()).log(Level.SEVERE, null, ex);
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
        else if(ret.isFlag() && FL_COMMIT == false)
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
                fail(ret.getMessage());
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
        dbserver.close();  */      
    }

    /**
     * Test of importCN method, of class Attached.
     */
    @Test
    public void testImportCN_5args_1() 
    {
        System.out.println("importCN");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
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
        
        
        FTP ftpServer = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = ftpServer.connect(serverConfig);
        }
        catch (IOException ex) 
        {
            //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        if(!expResult)
        {
            fail("Fallo la conexion al servidor de archivos");
        }
        Office office = new Office(-1);
        Return ret = new Return(true);
        Person person = new Person(-1);
        try 
        {
            person.selectRandom(dbserver.getConnection());
            person.download(dbserver);
            office.selectCode(dbserver, "bc.tj");
            office.download(dbserver.getConnection());            
            List<Invoice> invoices = Invoice.loadFromCN(new File(SIIL.CN.Tables.CN60.DIR_CN60_TJ), "A", 23688,23695);
            //invoices.get(0).downCompany(dbserver);
            Enterprise enterprise = invoices.get(0).getEnterprise(dbserver);
            enterprise.downMailFact(dbserver);
            ret = Attached.importCN(dbserver, ftpServer, office,invoices,enterprise.getEmailFactura(office),enterprise,person,false);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AttachedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        catch (Exception ex) 
        {
            fail(ex.getMessage());
            //Logger.getLogger(AttachedTest.class.getName()).log(Level.SEVERE, null, ex);
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
        else if(ret.isFlag() && FL_COMMIT == false)
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
                fail(ret.getMessage());
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
        dbserver.close();  */
    }

    /**
     * Test of download method, of class Attached.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of search method, of class Attached.
     */
    @Test
    public void testSearch() 
    {
        System.out.println("search");
    }

    /**
     * Test of guessCompany method, of class Attached.
     */
    @Test
    public void testGuessCompany() 
    {
        System.out.println("guessCompany");
    }

    /**
     * Test of importCN method, of class Attached.
     */
    @Test
    public void testImportCN_5args_2() 
    {
        System.out.println("importCN");
    }

    /**
     * Test of getMail method, of class Attached.
     */
    @Test
    public void testGetMail() {
        System.out.println("getMail");
    }

    /**
     * Test of getVault method, of class Attached.
     */
    @Test
    public void testGetVault() {
        System.out.println("getVault");
    }

    /**
     * Test of getEnterprise method, of class Attached.
     */
    @Test
    public void testGetEnterprise() {
        System.out.println("getEnterprise");
    }
    
}
