
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
public class ConfigurationTest 
{
    
    public ConfigurationTest() {
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
     * Test of update method, of class Configuration.
     */
    @Test
    public void testUpdate() throws Exception 
    {
        System.out.println("Iniciando prueba de Configuracion...");
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
        }*/
        
        
        
        
        ///>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        /*System.out.println("Configurando prueba de Confuration...");
        boolean flCreate = true;
        Database db = null;
        try 
        {
            ds = new MysqlDataSource();
            ds.setServerName("192.168.1.200");
            ds.setDatabaseName("DBSSIILa");
            ds.setUser("application");  
            db = new Database("alpha",ds);
        }
        catch (ClassNotFoundException | SQLException | DeployException | AmbiguosException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Estado estado = new Estado();
        estado.fill(db, cred, "docedit");        
        String strObject = "SIIL.Servicios.Orden.Screen.List";
        
        Configuration conf = new Configuration();
        conf.setBD(cred.getBD());
        conf.setOffice(cred.getSuc());
        conf.setUser(cred.getUser());
        conf.setObject(strObject);
        conf.setAttribute(estado.getCode());
        String v = flCreate ? "enable" : "disable";
        conf.setValue(v);
        
        int fl = 0;
        try 
        {
            fl = conf.update(db);
            assert(fl == 1);
            System.out.println("Validacion de registro insertado.");
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if(fl == 1)
        {
            try 
            {
                db.commit();
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
                db.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return;
            }            
        }
        db.close();*/
    }
    
}
