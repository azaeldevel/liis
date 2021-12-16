
package SIIL.sockets;

import session.User;
import core.Instance;
import SIIL.sockets.messages.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azael
 */
public abstract class Client extends Socket implements Runnable
{
    /**
     * @return the id
     */
    public Instance getID() 
    {
        return id;
    }
    
    protected boolean  flagRun;
    private Instance id;
      
    public void close()
    {
        flagRun = false;
        try 
        {
            super.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Client(String host,int port,User user) throws IOException
    {
        super(host,port);
        flagRun = true;
        id = new Instance(user);
    }
    
    public Client(SocketImpl socketImpl) throws SocketException, UnknownHostException 
    {
        super(socketImpl);
        flagRun = true;
    }
    
    public Message question(Message message) 
    {
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(getOutputStream());
            oos.writeObject(message);
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        int stepTesting = 0;
        while(flagRun && stepTesting < 3)
        {
            Message ms = receive();
            stepTesting++;
            if(ms != null)
            {
                return ms;
            }
            else
            {
                try 
                {
                    synchronized(this)
                    {
                        wait(100);
                    }
                } 
                catch (InterruptedException ex) 
                {
                    //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
        }
        return null;
    }
    
    public boolean send(Message message) 
    {
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(getOutputStream());
            oos.writeObject(message);
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public Message receive()
    {
        try 
        {
            ObjectInputStream ois = new ObjectInputStream(getInputStream());
            //System.out.println(ois);
            return (Message) ois.readObject();
        } 
        catch (IOException | ClassNotFoundException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }  

}
