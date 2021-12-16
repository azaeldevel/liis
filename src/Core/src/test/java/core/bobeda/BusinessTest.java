
package core.bobeda;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
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
import process.Moneda;
import process.Return;
import process.TipoCambio;

/**
 *
 * @author Azael Reyes
 */
public class BusinessTest 
{
    private static boolean FL_COMMIT = true;
    
    public BusinessTest() {
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
     * Test of add method, of class Business.
     */
    @Test
    public void testAdd()
    {
        /*System.out.println("add");
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
        Vault bobeda = new Vault(-1);
        Enterprise enterprise = new Enterprise();
        Return ret = null;
        if(expResult)
        {
            try 
            {               
                enterprise.selectRandom(dbserver);                
                enterprise.download(dbserver);                
                if(!ftpServer.isExist(Vault.Type.PO,Vault.Origen.CLIENTE, enterprise.getNumber()))
                {
                    if(!ftpServer.addSubdirectory(Vault.Type.PO,Vault.Origen.CLIENTE, enterprise.getNumber()))
                    {
                        fail("No se pudo crear el subdirectio de cliente");
                    }
                }
                //if(ftpServer.addSubdirectory(FTP.Directories.CLIENTE_PO, md5StrFile)) 
                {
                    File file = new File("kit.pdf");
                    FileInputStream in = new FileInputStream(file);
                    TipoCambio tipoCambio = new TipoCambio(Moneda.MXN,Moneda.MXN,19.2365);
                    ret = Business.add(dbserver,ftpServer,"kit.pdf",in,enterprise,123456.98,"Archivo de prueba",Vault.Type.PO,Vault.Origen.CLIENTE,new Date().toString(),tipoCambio);
                }
            }
            catch (IOException | SQLException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
            }
        }
        else
        {
            fail("No se realizo al conexion al servidor");            
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
        try 
        {
            ftpServer.logout();
            ftpServer.disconnect();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(BobedaTest.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    /**
     * Test of getBobeda method, of class Business.
     */
    @Test
    public void testGetBobeda() {
        System.out.println("getBobeda");
    }

    /**
     * Test of getMonto method, of class Business.
     */
    @Test
    public void testGetMonto() {
        System.out.println("getMonto");
    }

    /**
     * Test of getEnterprise method, of class Business.
     */
    @Test
    public void testGetEnterprise() {
        System.out.println("getEnterprise");
    }
    
}
