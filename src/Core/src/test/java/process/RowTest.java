
package process;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import stock.Flow;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class RowTest 
{
    private static final boolean FL_COMMIT = true;
    
    public RowTest() {
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
     * Test of insert method, of class Row.
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low; 
        Connection connection = dbserver.getConnection();
        
        List<Flow> items = new ArrayList<>();
        Flow item1 = new Flow(-1);
        Flow item2 = new Flow(-1);
        Flow item3 = new Flow(-1);
        Flow item4 = new Flow(-1);
        Flow item5 = new Flow(-1);
        Operational op = new Operational(-1);
        
        try 
        {
            item1.selectRandom(connection);
            items.add(item1);
            item2.selectRandom(connection);
            items.add(item2);
            item3.selectRandom(connection);
            items.add(item3);
            item4.selectRandom(connection);
            items.add(item4);
            item5.selectRandom(connection);
            items.add(item5);
            
            op.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(RowsTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        
        Row rows = new Row(-1);
        Return<Integer> ret1 = null;
        Return<Integer> ret2 = null;
        Return<Integer> ret3 = null;
        Return<Integer> ret4 = null;
        Return<Integer> ret5 = null;
        try 
        {
            ret1 = rows.insert(connection, op, item1);
            ret2 = rows.insert(connection, op, item2);
            ret3 = rows.insert(connection, op, item3);
            ret4 = rows.insert(connection, op, item4);
            ret5 = rows.insert(connection, op, item5);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(RowsTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        
        if(ret1.isFail())
        {
            //Logger.getLogger(RowsTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ret1.getMessage());
            return;
        }
        if(ret2.isFail())
        {
            //Logger.getLogger(RowsTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ret2.getMessage());
            return;
        }
        if(ret3.isFail())
        {
            //Logger.getLogger(RowsTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ret3.getMessage());
            return;
        }
        if(ret4.isFail())
        {
            //Logger.getLogger(RowsTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ret4.getMessage());
            return;
        }
        if(ret5.isFail())
        {
            //Logger.getLogger(RowsTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ret5.getMessage());
            return;
        }
        if(FL_COMMIT)
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
        else if(FL_COMMIT == false)
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
        }
        dbserver.close();
    }

    /**
     * Test of getOperational method, of class Row.
     */
    @Test
    public void testGetOperational() {
        System.out.println("getOperational");
    }

    /**
     * Test of getItems method, of class Row.
     */
    @Test
    public void testGetItems() {
        System.out.println("getItems");
    }

    /**
     * Test of select method, of class Row.
     */
    @Test
    public void testSelect() throws Exception {
        System.out.println("select");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low; 
        Connection connection = dbserver.getConnection();
        
        List<Row> list = Row.select(connection, "CN", 71820);
        if(list != null)
        {
            System.out.println("Se encontraron " + list.size());
        }
    }

    /**
     * Test of getItem method, of class Row.
     */
    @Test
    public void testGetItem() {
        System.out.println("getItem");
    }

    /**
     * Test of delete method, of class Row.
     */
    @Test
    public void testDelete() 
    {
        System.out.println("delete");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low; 
        Connection connection = dbserver.getConnection();
        List<Row> list = null;
        try 
        {
            list = Row.select(connection, "CN", 72161);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(RowTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        for(Row row : list)
        {
            try 
            {
                row.downItem(connection);
                if(row.delete(connection) == false)
                {
                    fail("Error inesperado.");
                }
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(RowTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
            }
        }
        
        if(FL_COMMIT)
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
        else if(FL_COMMIT == false)
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
        }
    }

    /**
     * Test of exist method, of class Row.
     */
    @Test
    public void testExist() throws Exception {
        System.out.println("exist");
    }

    /**
     * Test of downItem method, of class Row.
     */
    @Test
    public void testDownItem() throws Exception {
        System.out.println("downItem");
    }
    
}
