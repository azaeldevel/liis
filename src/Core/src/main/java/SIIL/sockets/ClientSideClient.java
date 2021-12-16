
package SIIL.sockets;

import session.User;
import SIIL.sockets.messages.ClosedApplication;
import SIIL.sockets.messages.Message;
import SIIL.sockets.messages.RegisteredApplication;
import SIIL.sockets.messages.RequireCloseApplication;
import java.awt.Frame;
import java.io.IOException;

/**
 *
 * @author Azael Reyes
 */
public class ClientSideClient extends Client
{    
    private boolean flagRegisteredApplication;
    private Frame frame;
    
    /**
     * @return the flagRegisteredApplication
     */
    public boolean isFlagRegisteredApplication() 
    {
        return flagRegisteredApplication;
    }
    
    public ClientSideClient(String host, int port, User user, Frame frame) throws IOException 
    {
        super(host, port, user);
        this.frame = frame;
    }
    
    @Override
    public void run() 
    {
        //System.out.println("Socket Cliente - Run...");
        Message mensaje = null;
        while(flagRun)//!Thread.interrupted()
        {
            //System.out.println("Socket Cliente - Esperando mensaje...");
            mensaje = receive();
            if(mensaje == null)
            {
                //System.out.println("Socket Cliente - Mensaje nulo se asume socket cerrado.");
                flagRun = false;
                return;
            }
            //System.out.println("Socket Cliente - Mensaje recibido...");
            if (mensaje instanceof RegisteredApplication)
            {//Se acepto la peticion de abrir la aplicacion
                flagRegisteredApplication = true;
                System.out.println("Notificacion de registro : " + getID().getAddress() + "|" + getID().getUser() + "|" + getID().getHasCode()); 
            }
            else if (mensaje instanceof RequireCloseApplication)
            {
                //System.out.println("Socket Cliente - Notificacion de cerrar aplicacion.");
                closeApplication();
                return;
            }
            else if (mensaje instanceof Message)
            {
                System.out.println("Socket Cliente - Mensaje no manejado...");
            }
            else
            {
                System.out.println("Socket Cliente - Mensaje desconocido...");
            }        
        }        
    }

    private void closeApplication() 
    {
        flagRun = false;
        send(new ClosedApplication(getID()));
        frame.dispose();
    }
}
