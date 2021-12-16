
package core.bobeda;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import java.io.File;
import java.io.IOException;
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

/**
 *
 * @author Azael Reyes
 */
public class ArchivoTest 
{
    private static boolean FL_COMMIT = true;
    
    public ArchivoTest() 
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
     * Test of download method, of class Archivo.
     */
    @Test
    public void testDownload_3args()
    {
        System.out.println("download");
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
        
        Archivo archivo = new Archivo(-1);
        boolean ret = false;
        try 
        {
            archivo.selectRandom(dbserver);
            ret = archivo.download(dbserver, ftpServer, new File("C:\\Users\\Azael Reyes\\Desktop\\temp"));
        } 
        catch (SQLException | IOException ex) 
        {
            Logger.getLogger(ArchivoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(ret == false)
        {
            fail("Fallo la descarga del archivo");
        }
        dbserver.close();*/
    }

    /**
     * Test of download method, of class Archivo.
     */
    @Test
    public void testDownload_Database() 
    {   
        System.out.println("download");
    }

    /**
     * Test of getID method, of class Archivo.
     */
    @Test
    public void testGetID() 
    {
        System.out.println("getID");
    }

    /**
     * Test of getFhFolio method, of class Archivo.
     */
    @Test
    public void testGetFhFolio() 
    {
        System.out.println("getFhFolio");
    }

    /**
     * Test of getDirectory method, of class Archivo.
     */
    @Test
    public void testGetDirectory() 
    {
        System.out.println("getDirectory");
    }

    /**
     * Test of getNombre method, of class Archivo.
     */
    @Test
    public void testGetNombre() 
    {
        System.out.println("getNombre");
    }

    /**
     * Test of getCodeName method, of class Archivo.
     */
    @Test
    public void testGetCodeName() 
    {
        System.out.println("getCodeName");
    }

    /**
     * Test of getDownloadFileName method, of class Archivo.
     */
    @Test
    public void testGetDownloadFileName() 
    {
        System.out.println("getDownloadFileName");
    }

    /**
     * Test of getDownloadedFile method, of class Archivo.
     */
    @Test
    public void testGetDownloadedFile() 
    {
        System.out.println("getDownloadedFile");
    }    
}
