
package SIIL.sockets;

import SIIL.Server.Database;
import SIIL.core.Exceptions.DatabaseException;
import SIIL.core.Exceptions.ValidationStateInputException;
import SIIL.core.Exceptions.ValidationStateOutputException;
import SIIL.sockets.messages.ConfirmApplication;
import core.Instance;
import SIIL.sockets.messages.Message;
import SIIL.sockets.messages.OpenApplication;
import SIIL.sockets.messages.RegisteredApplication;
import SIIL.sockets.messages.RequireCloseApplication;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azael
 */
public class Server extends ServerSocket implements Runnable
{
    private boolean flagRun;
    private SIIL.sockets.messages.Message messsage;
    private Throwable fail;
    private Map<String,ClientSideServer> sockets;
    private MysqlDataSource datasource;
    private String phase;
    private SIIL.core.config.Server config;
    
    public void confirmConections(boolean dropIfFail)
    {
        Collection<ClientSideServer> sks = sockets.values();
        for(ClientSideServer sk : sks)
        {
            sk.confirmConnection(dropIfFail);
        }
    }
    
    
    @Override
    public ClientSideServer accept() throws IOException 
    {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!isBound())
            throw new SocketException("Socket is not bound yet");
        ClientSideServer s = new ClientSideServer((SocketImpl)null, this);
        implAccept(s);
        return s;
    }
    
    @Override
    public void close()
    {
        try 
        {
            flagRun = false;
            super.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }
    
    @Override
    public void run()
    {
        while(flagRun)//!Thread.interrupted()
        {
            ClientSideServer cliente = null;
            Object mensaje = null;
            
            //System.out.println("Server - run : Esperado Cliente...");
            try 
            {
                cliente = accept();
                cliente.setSoLinger(true, 10);
            } 
            catch (IOException ex) 
            {
                //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Fallo de creacion de conexion con cliente: " + ex.getMessage());
            }
            //System.out.println("Server - run : Cliente aceptado...");            
            //System.out.println("Server - run : Esperando mesaje...");
            if(cliente == null)
            {//quiza se cerro el socket
                System.err.println("Se acepto socket null.");
                continue;
            }
            mensaje = cliente.receive();
            //System.out.println("Server - run : Mensaje de recibido...");
            if(mensaje == null)
            {//quiza se cerro el socket
                System.err.println("Se recibio mensaje null.");
                continue;
            }          
            
            if (mensaje instanceof OpenApplication)
            {
                OpenApplication msg = (OpenApplication) mensaje;
                System.out.println("MSG OpenApplication :" + msg.getSocketID().getAddress() + "|" + msg.getSocketID().getUser() + "|" + msg.getSocketID().getHasCode());                
                try 
                {
                    add(msg.getSocketID(),cliente);
                    //indicar al cliente que se acepto su solicitud.
                    if(!cliente.send(new RegisteredApplication(msg.getSocketID())))
                    {
                        remove(msg.getSocketID());
                        System.err.println("Falló el envio RegisteredApplication " + msg.getSocketID().getAddress() + "|" + msg.getSocketID().getUser() + "|" + msg.getSocketID().getHasCode());                        
                    }
                    else
                    {
                        //System.out.println("Notificando registro a aplicacion: " + msg.getSocketID().getAddress() + "|" + msg.getSocketID().getUser() + "|" + msg.getSocketID().getHasCode());
                        //iniciando bucle de mensajes.
                        Thread th = new Thread(cliente);
                        th.start();
                    }
                } 
                catch (DatabaseException | ValidationStateOutputException | ValidationStateInputException ex) 
                {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                } catch (SQLException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (mensaje instanceof Message)
            {
                //System.out.println("Server - run : Mensaje no manejado ");
            }
            else
            {
                //System.out.println("Server - run : Mensaje desconocido.");
            }
        }
    }
    
    public Server(int port,SIIL.core.config.Server config) throws IOException 
    {
        super(port);
        init(config);
    }

    private void init(SIIL.core.config.Server config) 
    {
        flagRun = true;
        sockets = new HashMap<>();
        this.config = config;
    }
    
    /**
     * Agrega un cliente al registro de registro del servidor.
     * @param id Indica el cliente afectado
     * @param client El socket correspondiente en el lado del servidor.
     */
    private void add(Instance id, ClientSideServer client) throws DatabaseException, ValidationStateOutputException, ValidationStateInputException, SQLException 
    {
        //System.out.println("Server - add : Registrando instancia.");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(config);
        }
        catch (SQLException | ClassNotFoundException ex) 
        {
            System.err.println("Server - add : Falló la conexion a base de datos.");
            return;
        }
        //Hay una instancia del programa en ejecucion?
        ArrayList<Instance> clients = Instance.select(id.getUser(),dbserver);
        int retSave = 0;
        if(clients != null && clients.size() > 0)
        {
            if(clients.size() > 1)
            {
                System.err.println("Server - add : Se encontraron " + clients.size() + " registro(s) para " + id.getUser());
            }
            //System.out.println("Server - add : Usuario duplicado " + id.getUser());            
            remove(clients);            
            retSave = saveSocket(client,id,dbserver);
        }
        else
        {
            remove(clients);
            retSave = saveSocket(client,id,dbserver);
            //System.out.println("Server - add : abierto para " + id.getUser());
        }
        try 
        {
            if(retSave == 1)
            {
                dbserver.commit();
            }
            else
            {
                dbserver.rollback();
            }
            System.out.println("Se agrego la aplicacion a la base de datos : " + id.getAddress() + "|" + id.getUser() + "|" + id.getHasCode());
            return;
        } 
        catch (SQLException ex) 
        {
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex1) 
            {
                //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
                System.out.println("No se agrego la aplicacion ala base de datos : " + id.getAddress() + "|" + id.getUser() + "|" + id.getHasCode());
            }
        }
        dbserver.close();
    }

    /**
     * Elimina el cliente del registro.
     * @param id Indica el cliente afectado 
     * @throws SIIL.core.Exceptions.ValidationStateInputException 
     * @throws SIIL.core.Exceptions.DatabaseException 
     */
    public void remove(Instance id) throws ValidationStateInputException, DatabaseException 
    {
        Database dbserver = null;
        try 
        {
            dbserver = new Database(config);
        }
        catch (SQLException | ClassNotFoundException ex) 
        {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Falló la creacionde conexion a Sevidor de BD:" + ex.getMessage());
        }
        //buscando el socket correspondioente al cliete.
        Client client = findSocket(id,dbserver);
        
        if(client == null)
        {// no hay nada que removoer
            System.err.println("Server - remove : Se solicito elimiar el socket inexistente para " + id.getAddress() + "|" + id.getHasCode() + "|" + id.getUser().getAlias());
            return;
        }              
        //para eliminar un socket hay cerrar la aplicacion correspondiente
        //System.out.println("Server - remove : RequireCloseApplication sending...");
        if(client.send(new RequireCloseApplication(id)))
        {//si se envia el mesaje dejar que el sistema complete el proceso.
            //System.out.println("Server - remove : RequireCloseApplication send...");
        }
        
        try 
        {
            dbserver.commit();
            //System.out.println("Server - remove : Datos confirmados.");
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Falló el comix de la operacion remove : " + ex.getMessage());
        }
        dbserver.close();
    }

    /**
     * Registra el socket en la base de datos y almacena la instancia localmente.
     * @param client
     * @param id 
     */
    private int saveSocket(ClientSideServer client, Instance id, Database dbserver) throws DatabaseException, SQLException 
    {
        String hasCode = id.getHasCode();
        int retval = 0;
        try 
        {
            retval = Instance.insert(id,hasCode,dbserver);
        } 
        catch (ValidationStateInputException ex) 
        {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Falló la insert en saveSoket:" + " --> " + ex.getMessage());
            return -1;
        } 
        catch (DatabaseException ex) 
        {
            throw new DatabaseException("Server - saveSocket : Fallo la operacion en Base de datos para almacenar los datos de la Instancia." + ex.getMessage(),ex);
        }
        
        ClientSideServer inserted = sockets.put(hasCode, client);
        /*if(inserted == null)
        {
            System.err.println("Server - saveSocket : Falló la insertcion del hascode " + hasCode + " cant registro(s) " + sockets.size());
        }
        else
        {
            System.out.println("Server - saveSocket : Se agrego correctamente la instancia con hascode " + hasCode + " cant registro(s) " + sockets.size());
        }*/
        return retval;
    }
    
    /**
     * Returna la instancia local correspondiente al registro identificado. 
     * @param id Identifica el socket que se busca.
     * @return no null si se hay correspondencia, null de otra forma.
     */
    private ClientSideServer findSocket(Instance id,Database dbserver) throws ValidationStateInputException, DatabaseException 
    {
        Instance instance = Instance.select(id,dbserver);
        if(instance != null)
        {
            //System.out.println("Server - findSocket : Se encontro la instancia en BD " + instance.getHasCode() + "|" + instance.getAddress()+ "|" + instance.getUser().getAlias());
            ClientSideServer onlist = sockets.get(instance.getHasCode());
            if(onlist == null) System.err.println("Server - findSocket : No existe el socke con has " + instance.getHasCode() + "|" + instance.getAddress()+ "|" + instance.getUser().getAlias());
            return onlist; 
        }
        else
        {
            //System.err.println("Server - findSocket : No la instancia en BD " + instance.getHasCode() + "|" + instance.getAddress()+ "|" + instance.getUser().getAlias());
            return null;
        }
    }
    
    /**
     * Elimina la instancia local y registra el cambio de estado en base de datos.
     * @param id 
     */
    private ClientSideServer eraseSocket(Instance id, Database dbserver) throws ValidationStateInputException, DatabaseException 
    {
        int flag = Instance.delete(id, dbserver);
        ClientSideServer cliente = sockets.get(id.getHasCode());
        if(cliente  != null )
        {
            //System.out.println("Server - eraseSocket : Se removio socket de la lista.");
            sockets.remove(id.getHasCode());
            return cliente;
        }
        else
        {
            //System.out.println("Server - eraseSocket : No se removio socket de la lista.");
            return null;
        }
    }

    private void remove(ArrayList<Instance> clients) throws ValidationStateInputException, DatabaseException 
    {
        for(Instance inst : clients)
        {
            remove(inst);
        }
    }

    ClientSideServer eraseSocket(Instance id)
    {
        Database dbserver = null;
        try 
        {
            dbserver = new Database(config);
        }
        catch (SQLException | ClassNotFoundException ex) 
        {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Falló la conexion a servidor de BD : " + ex.getMessage());
        }
        ClientSideServer client = null;
        try 
        {
            client = eraseSocket(id, dbserver);
        } 
        catch (ValidationStateInputException | DatabaseException ex) 
        {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);            
            System.err.println("Falló la operacion eraseSocket : " + ex.getMessage());
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex1) 
            {            
                System.err.println("Falló la operacion eraseSocket : " + ex.getMessage());
                //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
            }
            dbserver.close();
            return null;
        }
        if(client != null)
        {
            try 
            {
                dbserver.commit();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);            
                System.err.println("Falló la operacion commit : " + ex.getMessage());
                try 
                {
                    dbserver.rollback();
                } 
                catch (SQLException ex1) 
                {
                    //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);            
                    System.err.println("Falló la operacion de rollback : " + ex.getMessage());
                }
            }
        }
        dbserver.close();
        return client;
    }
}
