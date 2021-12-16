
package SIIL.client.services;

import SIIL.service.quotation.OrdenUpdate;
import SIIL.Server.Database;
import SIIL.artifact.AmbiguosException;
import SIIL.artifact.DeployException;
import SIIL.trace.Trace;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Azael
 */
public class OrdenUpdateTest 
{    
    public OrdenUpdateTest() 
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

    @Test
    public void mainMethod() 
    {
        /*String userName = "sistemas";
        char[] password = {'d','e','v'};
        session.User user = new session.User();
        user.setBD("bc.tj");
        if (userName.length() > 0) {
            user.setAlias(userName.toLowerCase());
        } else {
            JOptionPane.showMessageDialog(null,
                    "Indique el usuario",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (password.length > 0) {
            user.setPassword(password);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Indique la contraseña",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        session.Credential cred  = new session.Credential();   
        MysqlDataSource ds = new MysqlDataSource();
        ds.setDatabaseName("DBSSIILa");
        ds.setServerName("192.168.1.200");
        ds.setDatabaseName("DBSSIILa");
        ds.setUser("application");  
        if(cred.check(user,"alpha",ds))
        {
            ;
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "usuario/Contraseña Incorrecto",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
        Database db = null;
        try 
        {
        ds = new MysqlDataSource();
        ds.setDatabaseName("DBSSIILa");
        ds.setServerName("192.168.1.200");
        ds.setDatabaseName("DBSSIILa");
        ds.setUser("application");  
            db = new Database("alpha",ds);
        } 
        catch (ClassNotFoundException | SQLException | DeployException | AmbiguosException ex) 
        {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,
                    "Conexion a Servidor Invalida",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
        }
        String BD = "bc.tj";
        int pID = 55;
        OrdenUpdate ord = new OrdenUpdate();
        ord.setBD(BD);
        ord.setFolio(4021);
        ord.setID(2);
        ord.fill(db, cred, pID);
        Throwable th = ord.fill(db, cred, pID);
        int fltrace = 0;
        ord.setTrace(new Trace(BD, user, "Actualizacón de Encargado - Cotización " + ord.getFolio()));
        try 
        {
            fltrace = ord.getTrace().insert(db);
        } 
        catch (SQLException ex) 
        {
            System.err.println("Fallo la creacion del registro de trace.");
            Logger.getLogger(OrdenUpdateTest.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Throwable up = ord.updateOwner(db);
        if(up == null || th != null)
        {
            try
            {
                db.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try
            {
                db.rollback();
                fail("No se confirmo la operación: " + up.getMessage());
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        db.close();*/
    }

    /**
     * Test of updateOwner method, of class OrdenUpdate.
     */
    @Test
    public void testUpdateOwner() {
        System.out.println("updateOwner");
    }

    /**
     * Test of updateCompany method, of class OrdenUpdate.
     */
    @Test
    public void testUpdateCompany() throws Exception 
    {
        /*String userName = "sistemas";
        char[] password = {'d','e','v'};
        session.User user = new session.User();
        user.setBD("bc.tj");
        if (userName.length() > 0) {
            user.setAlias(userName.toLowerCase());
        } else {
            JOptionPane.showMessageDialog(null,
                    "Indique el usuario",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (password.length > 0) {
            user.setPassword(password);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Indique la contraseña",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        session.Credential cred  = new session.Credential();   
        MysqlDataSource ds = new MysqlDataSource();
        ds.setDatabaseName("DBSSIILa");
        ds.setServerName("192.168.1.200");
        ds.setDatabaseName("DBSSIILa");
        ds.setUser("application");  
        if(cred.check(user,"alpha",ds))
        {
            ;
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "usuario/Contraseña Incorrecto",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
        Database db = null;
        try 
        {
        ds = new MysqlDataSource();
        ds.setDatabaseName("DBSSIILa");
        ds.setServerName("192.168.1.200");
        ds.setDatabaseName("DBSSIILa");
        ds.setUser("application");  
            db = new Database("alpha",ds);
        } 
        catch (ClassNotFoundException | SQLException | DeployException | AmbiguosException ex) 
        {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,
                    "Conexion a Servidor Invalida",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
        }
        String BD = "bc.tj";
        int pID = 55;
        OrdenUpdate ord = new OrdenUpdate();
        ord.setBD(BD);
        ord.setID(2); 
        SIIL.client.sales.Enterprise comp = new SIIL.client.sales.Enterprise();
        comp.setNumber("4613");
        ord.setCompany(comp);
        int fltrace = 0;
        ord.setTrace(new Trace(BD, user, "Actualizacón de Cliente - Cotización " + ord.getFolio()));
        try 
        {
            fltrace = ord.getTrace().insert(db);
        } 
        catch (SQLException ex) 
        {
            System.err.println("Fallo la creacion del registro de trace.");
            Logger.getLogger(OrdenUpdateTest.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Throwable up = ord.updateCompany(db);
        if(up == null)
        {
            try
            {
                db.commit();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try
            {
                db.rollback();
                fail("No se confirmo la operación: " + up.getMessage());
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }
}
