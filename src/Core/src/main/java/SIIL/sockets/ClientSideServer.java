
package SIIL.sockets;

import core.Instance;
import SIIL.sockets.messages.BatMessage;
import SIIL.sockets.messages.ClosedApplication;
import SIIL.sockets.messages.ConfirmApplication;
import SIIL.sockets.messages.ConfirmedConnection;
import SIIL.sockets.messages.Message;
import SIIL.sockets.messages.SynchronizeApplication;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.UnknownHostException;

/**
 *
 * @author Azael Reyes
 */
public class ClientSideServer extends Client
{
    private Server server;
    private Instance lastInstance;
    
    
    public ClientSideServer(SocketImpl socketImpl,Server server) throws SocketException, UnknownHostException 
    {
        super(socketImpl);
        this.server = server;
    }
    
    
    @Override
    public void run() 
    {
        //System.out.println("ClientSideServer - Run...");
        Message mensaje;
        while(flagRun)//!Thread.interrupted()
        {
            //System.out.println("ClientSideServer - Esperando mensaje...");
            mensaje = receive();
            if(mensaje == null)
            {
                //System.out.println("ClientSideServer - Mensaje nulo se asume socket cerrado.");
                flagRun = false;
                if(lastInstance != null)server.eraseSocket(lastInstance);
                return;
            }
            else if(mensaje instanceof Message)
            {
                lastInstance = ((Message)mensaje).getSocketID();
            }
            
            //System.out.println("ClientSideServer - Mensaje recibido...");
            if (mensaje instanceof ClosedApplication)
            {
                //terminar el bucle de mensajes.
                //System.out.println("ClientSideServer - Mensaje ClosedApplication...");
                flagRun = false;
                //System.out.println("ClientSideServer - Mensaje ClosedApplication...");
                ClosedApplication msj = (ClosedApplication) mensaje;
                server.eraseSocket(msj.getSocketID());
                return;
            }
            else if(mensaje instanceof BatMessage)
            {
                BatMessage m = (BatMessage) mensaje;
                System.err.println("Server - remove : Previamente se envio la orden de cerra a la aplicacion, la cual respondio:");
                System.err.print(m.getMessage());
                System.err.println("Server - remove : La operacion será ignorada.");
            }            
            else if (mensaje instanceof SynchronizeApplication)
            {
                System.out.println("Syncronizacion solicitada.");
                server.confirmConections(true);
            }
            else if (mensaje instanceof ConfirmedConnection)
            {
                ;
            }
            else if (mensaje instanceof Message)
            {
                ;
            }
            else
            {                
                //System.out.println("ClientSideServer - Mensaje desconocido...");
            }
        }        
    }

    boolean confirmConnection(boolean dropIfFail) 
    {
        Message r = question(new ConfirmedConnection(lastInstance));
        if(r instanceof ConfirmedConnection)
        {
            return true;
        }
        return false;
    }
}
