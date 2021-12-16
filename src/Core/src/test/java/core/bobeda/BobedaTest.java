
package core.bobeda;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import process.Return;

/**
 *
 * @author Azael Reyes
 */
public class BobedaTest 
{    
    private static boolean FL_COMMIT = true;
    
    public BobedaTest() {
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
     * Test of selectRandom method, of class Bobeda.
     */
    @Test
    public void testSelectRandom() 
    {
        System.out.println("selectRandom");
        
    }

    /**
     * Test of getFile method, of class Bobeda.
     */
    @Test
    public void testGetFile() 
    {
        System.out.println("getFile");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        OutputStream file = null;
        try 
        {
            Vault bobeda = new Vault(Vault.selectRandom(dbserver));
            file = Vault.getFile(dbserver, ftpServer,bobeda ,"C:\\Users\\Azael Reyes\\Desktop\\");
        } 
        catch (SQLException | IOException ex) 
        {
            Logger.getLogger(BobedaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(file == null) fail("Fallo la descarga del archivo.");*/
        
    }

    /**
     * Test of add method, of class Bobeda.
     */
    @Test
    public void testAdd()
    {
        System.out.println("add");
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
                    ret = Archivo.add(dbserver,ftpServer,"kit.pdf",in,enterprise,"Archivo de prueba",Vault.Type.PO,Vault.Origen.CLIENTE);
                }
            }
            catch (IOException | SQLException ex) 
            {
                fail(ex.getMessage());
            }
        }
        else
        {
            fail("No se realizo al conexion al servidor");            
        } 
        if(!(ret.getParam() instanceof Archivo))
        {
            fail("add deve de retorna una instacia Vault");
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
}
