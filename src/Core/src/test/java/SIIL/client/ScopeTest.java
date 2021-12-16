
package SIIL.client;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Azael
 */
public class ScopeTest 
{
    
    public ScopeTest() {
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
     * Test of requestEmailSetByDepartment method, of class Scope.
     */
    @Test
    public void testRequestEmailSetByDepartment() 
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
        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("192.168.1.200");
        ds.setDatabaseName("DBSSIILa");
        ds.setUser("application");  
        session.Credential cred  = new session.Credential();        
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
        ds.setServerName("192.168.1.200");
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
        }
        assert(true);*/
    }
    
}
