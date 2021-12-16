
package SIIL.services;

import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Azael Reyes
 */
public class MttoTest 
{
    private static final boolean FL_COMMIT = true;
    
    public MttoTest() 
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
     * Test of insert method, of class Mtto.
     */
    @Test
    public void testInsert() throws SQLException 
    {
        System.out.println("insert");
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
        
        
        Enterprise enterprise = new Enterprise(-1);
        Flow flow = new Flow(-1);
        Mtto mtto = new Mtto();
        Return result;
        try 
        {
            flow.selectRandom(dbserver.getConnection());
            enterprise.selectRandom(dbserver.getConnection());
            result = mtto.insert(dbserver, flow, enterprise,null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
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
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(result.isFail()&& FL_COMMIT == false)
        {
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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
            }//fail("Cant. Reg. incorrectos : " + insertedID);
        }*/
    }

    /**
     * Test of getId method, of class Mtto.
     */
    @Test
    public void testGetId() 
    {
        System.out.println("getId");
    }

    /**
     * Test of getEquipo method, of class Mtto.
     */
    @Test
    public void testGetEquipo() 
    {
        System.out.println("getEquipo");
    }

    /**
     * Test of getUbique method, of class Mtto.
     */
    @Test
    public void testGetUbique() 
    {
        System.out.println("getUbique");        
    }

    /**
     * Test of getPrevOrder method, of class Mtto.
     */
    @Test
    public void testGetPrevOrder() 
    {
        System.out.println("getPrevOrder");        
    }

    /**
     * Test of getPreComment method, of class Mtto.
     */
    @Test
    public void testGetPreComment() 
    {
        System.out.println("getPreComment");        
    }

    /**
     * Test of getLastOrder method, of class Mtto.
     */
    @Test
    public void testGetLastOrder() 
    {
        System.out.println("getLastOrder");        
    }

    /**
     * Test of getLastComment method, of class Mtto.
     */
    @Test
    public void testGetLastComment() 
    {
        System.out.println("getLastComment");        
    }

    /**
     * Test of getEnterprise method, of class Mtto.
     */
    @Test
    public void testGetEnterprise() 
    {
        System.out.println("getEnterprise");        
    }

    /**
     * Test of upEquipo method, of class Mtto.
     */
    @Test
    public void testUpEquipo() 
    {
        System.out.println("upEquipo");       
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
        
        Mtto mtto = new Mtto(); 
        Flow flow = new Flow(-1);
        Return ret = null;
        
        try 
        {         
            flow.selectRandom(dbserver.getConnection());
            mtto.selectRandom(dbserver);
            ret = mtto.upEquipo(dbserver, flow, null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLog ger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.Close();*/  
    }

    /**
     * Test of upEnterprise method, of class Mtto.
     */
    @Test
    public void testUpEnterprise()
    {
        System.out.println("upEnterprise");       
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
        
        Mtto mtto = new Mtto(); 
        Enterprise enterprise = new Enterprise(-1);
        Return ret = null;
        
        try 
        {         
            enterprise.selectRandom(dbserver.getConnection());
            mtto.selectRandom(dbserver);
            ret = mtto.upEnterprise(dbserver, enterprise, null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLog ger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.Close(); */
        
    }

    /**
     * Test of upPrevOrder method, of class Mtto.
     */
    @Test
    public void testUpPrevOrder() 
    {
        System.out.println("upPrevOrder");      
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
        
        Mtto mtto = new Mtto(); 
        Order order = new Order(-1);
        Return ret = null;
        
        try 
        {         
            order.selectRandom(dbserver);
            mtto.selectRandom(dbserver);
            ret = mtto.upPenultimateOrder(dbserver, order, null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLog ger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.Close(); */
    }

    /**
     * Test of upLastOrder method, of class Mtto.
     */
    @Test
    public void testUpLastOrder() 
    {
        System.out.println("upLastOrder");     
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
        
        Mtto mtto = new Mtto(); 
        Order order = new Order(-1);
        Return ret = null;
        
        try 
        {         
            order.selectRandom(dbserver);
            mtto.selectRandom(dbserver);
            ret = mtto.upLastOrder(dbserver, order, null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLog ger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.Close(); */
    }

    /**
     * Test of upPrevCommnet method, of class Mtto.
     */
    @Test
    public void testUpPrevCommnet() 
    {
        System.out.println("upPrevCommnet");             
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
        
        Mtto mtto = new Mtto(); 
        Return ret = null;
        
        try 
        {         
            mtto.selectRandom(dbserver);
            ret = mtto.upPenultimateCommnet(dbserver, "Comentario de Prueba", null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLog ger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.Close(); */
    }

    /**
     * Test of upLastCommnet method, of class Mtto.
     */
    @Test
    public void testUpLastCommnet() 
    {
        System.out.println("upLastCommnet");             
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
            return;
        }
        
        Mtto mtto = new Mtto(); 
        Return ret = null;
        
        try 
        {         
            mtto.selectRandom(dbserver);
            ret = mtto.upLastCommnet(dbserver, "Comentario de Prueba", null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLog ger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.Close(); */
    }

    /**
     * Test of selectRandom method, of class Mtto.
     */
    @Test
    public void testSelectRandom() 
    {
        System.out.println("selectRandom");
    }

    /**
     * Test of addOrder method, of class Mtto.
     */
    @Test
    public void testAddOrder()
    {
        System.out.println("addOrder");
    }

    /**
     * Test of downLastComment method, of class Mtto.
     */
    @Test
    public void testDownLastComment()
    {
        System.out.println("downLastComment");
    }

    /**
     * Test of downLastOrder method, of class Mtto.
     */
    @Test
    public void testDownLastOrder() 
    {
        System.out.println("downLastOrder");
    }

    /**
     * Test of downPenultimateComment method, of class Mtto.
     */
    @Test
    public void testDownPenultimateComment() 
    {
        System.out.println("downPenultimateComment");        
    }

    /**
     * Test of downPenultimateOrder method, of class Mtto.
     */
    @Test
    public void testDownPenultimateOrder() 
    {
        System.out.println("downPenultimateOrder");        
    }

    /**
     * Test of downClient method, of class Mtto.
     */
    @Test
    public void testDownClient() 
    {
        System.out.println("downClient");        
    }

    /**
     * Test of upPenultimateOrder method, of class Mtto.
     */
    @Test
    public void testUpPenultimateOrder() 
    {
        System.out.println("upPenultimateOrder");        
    }

    /**
     * Test of upPenultimateCommnet method, of class Mtto.
     */
    @Test
    public void testUpPenultimateCommnet() 
    {
        System.out.println("upPenultimateCommnet");        
    }

    /**
     * Test of getLocation method, of class Mtto.
     */
    @Test
    public void testGetLocation() 
    {
        System.out.println("getLocation");
    }

    /**
     * Test of getPenultimateOrder method, of class Mtto.
     */
    @Test
    public void testGetPenultimateOrder() 
    {
        System.out.println("getPenultimateOrder");        
    }

    /**
     * Test of getPenultimateComment method, of class Mtto.
     */
    @Test
    public void testGetPenultimateComment() 
    {
        System.out.println("getPenultimateComment");        
    }    
}
