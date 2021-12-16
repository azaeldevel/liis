
package core.bobeda;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.core.MD5;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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

/**
 *
 * @author Azael Reyes
 */
public class FTPTest {
    
    public FTPTest() {
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
     * Test of connect method, of class FTP.
     */
    @Test
    public void testConnect() 
    {
        System.out.println("connect");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();   
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        } 
        catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        FTP instance = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = instance.connect(serverConfig);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(expResult)
        {
            
        }
        else
        {
            fail("No se realizo al conexion al servidor");            
        }*/        
    }

    /**
     * Test of store method, of class FTP.
     */
    @Test
    public void testUpload()
    {
        System.out.println("upload");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();   
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        } 
        catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        FTP instance = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = instance.connect(serverConfig);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(expResult)
        {
            try 
            {
                String fName = "kit.pdf";
                File file = new File(fName);
                FileInputStream inputStream = new FileInputStream(file);
                if(!instance.upload(Vault.Type.PO,Vault.Origen.CLIENTE, "Test",fName, inputStream)) fail("Fallo la carga de archivo");
                inputStream.close();
                instance.logout();
                instance.disconnect();
            }
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
            }
        }
        else
        {
            fail("No se realizo al conexion al servidor");            
        }       */ 
    }

    /**
     * Test of download method, of class FTP.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();   
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        } 
        catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        FTP instance = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = instance.connect(serverConfig);
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        if(expResult)
        {
            try 
            {
                OutputStream outputStream = instance.download(Vault.Type.PO,Vault.Origen.CLIENTE, "Test","kit.pdf","kit2.pdf");
                if(outputStream == null) fail("Fallo la carga de archivo");                
                outputStream.close();
                instance.logout();
                instance.disconnect();
            }
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
            }
        }
        else
        {
            fail("No se realizo al conexion al servidor");            
        }    */
    }

    /**
     * Test of addSubdirectory method, of class FTP.
     */
    @Test
    public void testAddSubdirectory() 
    {
        System.out.println("addSubdirectory");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();   
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        } 
        catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        FTP instance = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = instance.connect(serverConfig);
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        if(expResult)
        {
            try 
            {
                MD5 md5 = new MD5();
                Date date = new Date();
                String md5StrFile = md5.generate(date.toString());
                expResult = instance.addSubdirectory(Vault.Type.PO,Vault.Origen.CLIENTE, md5StrFile);
                instance.logout();
                instance.disconnect();
            }
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
            }
        }
        else
        {
            fail("No se realizo al conexion al servidor");            
        }    */
    }
    
}
