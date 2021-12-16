
package SIIL.desktop.auth;

import SIIL.sockets.messages.OpenApplication;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.swing.JOptionPane;

/**
 *
 * @author Azael
 */
public class ToolLoginModule implements LoginModule
{
    Subject subject;
    CallbackHandler callbackHandler;
    Map<String, ?> sharedState;
    Map<String, ?> options;
    String phase;
    String serverapp;
    int socketport;
    private MysqlDataSource ds;
    
    public String getPhase()
    {
        return this.phase;
    }
    
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) 
    {
        this.subject          = subject;
        this.callbackHandler  = callbackHandler;
        this.sharedState      = sharedState;
        this.options          = options;
        this.phase          = (String)options.get("phase");
        this.serverapp      = (String)options.get("serverapp");
        this.socketport     = Integer.valueOf((String)options.get("socketport"));
    }

    @Override
    public boolean login() 
    {   
        NameCallback nameCb = new NameCallback("user: ");
        PasswordCallback passCb = new PasswordCallback("password: ", true);
        
        Callback[] callbacks = new Callback[] {nameCb,passCb};
        try 
        {
            callbackHandler.handle(callbacks);
        }
        catch (IOException | UnsupportedCallbackException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        boolean success = validate(nameCb.getName(), passCb.getPassword());
        
        if(success)
        {
            System.setProperty("tools.username", nameCb.getName());
        }
        
        return  success;
    }

    @Override
    public boolean commit() throws LoginException {
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        return true;
    }

    private boolean validate(String userName, char[] password) 
    {
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
            return false;
        }
        if (password.length > 0) {
            user.setPassword(password);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Indique la contraseña",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        SIIL.servApp.cred  = new session.Credential();
        SIIL.servApp.login = this;
        ds = new MysqlDataSource();
        ds.setDatabaseName((String) options.get("mysqldb"));
        ds.setServerName((String) options.get("mysqlhost"));
        ds.setUser((String) options.get("mysqluser"));          
        //ds.setCharacterEncoding("ISO_8859_1");
        
        if(SIIL.servApp.cred.check(user,phase,ds))
        {            
            try 
            {
                SIIL.servApp.socket = new SIIL.sockets.ClientSideClient(serverapp, socketport,user,SIIL.servApp.getInstance());
            } 
            catch (IOException ex) 
            {
                JOptionPane.showMessageDialog(null,
                        "Falló al crear la conexion de Spech.",
                        "Error Externo",
                        JOptionPane.ERROR_MESSAGE
                );
                SIIL.servApp.getInstance().dispose();
                System.exit(0);
            } 
            SIIL.servApp.socketTh = new Thread(SIIL.servApp.socket);
            SIIL.servApp.socketTh.start();
            SIIL.servApp.socket.send(new OpenApplication(SIIL.servApp.socket.getID()));
            int countTime = 0,elapse = 100;
            while (SIIL.servApp.socket.isFlagRegisteredApplication() == false )
            {
                if(countTime > 5)
                {
                    JOptionPane.showMessageDialog(null,
                            "Se exedio el timpo limite de espera.",
                            "Error Externo",
                            JOptionPane.ERROR_MESSAGE
                    );
                    System.exit(0);
                }
                try 
                {
                    synchronized(this)
                    {
                        wait(1000);
                        countTime++;
                    }
                }
                catch (InterruptedException ex) 
                {
                    Logger.getLogger(ToolLoginModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            SIIL.servApp.getInstance().enableAcces(); 
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "usuario/Contraseña Incorrecto",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    /**
     * @return the ds
     */
    public MysqlDataSource getDS() {
        return ds;
    }
    
}