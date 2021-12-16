
package SIIL.desktop.auth;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

/**
 *
 * @author Azael Reyes
 */
public class ToolCallbackHandler implements CallbackHandler
{
    String userName;
    char[] password;

    public ToolCallbackHandler(String username, char[] password) 
    {
        this.userName = username;
        this.password = password;
    }
    
    @Override
    public void handle(Callback[] callbacks) 
    {
        for (int i = 0; i < callbacks.length; i++) 
        {
            if (callbacks[i] instanceof NameCallback) 
            {
                NameCallback nameCb = (NameCallback)callbacks[i]; 
                nameCb.setName(userName);
            }
            else if(callbacks[i] instanceof PasswordCallback) 
            {
                PasswordCallback passCb = (PasswordCallback)callbacks[i];
                passCb.setPassword(password);
            }
            else
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE,"No se reconoce el Handle " + callbacks[i].toString());
            }
        }
    }    
}
