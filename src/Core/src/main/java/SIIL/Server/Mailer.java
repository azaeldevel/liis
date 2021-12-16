
package SIIL.Server;

import SIIL.core.TypeMail;
import core.bobeda.FTP;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import process.Mail;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class Mailer implements Runnable
{
    private boolean flRun;
    private List<Sender> senders;
    private int sendersLast;
    private Throwable error;
    private final SIIL.core.config.Server configServer;
    private TypeMail type;
    
    
    public Mailer(SIIL.core.config.Server configServer, TypeMail type)
    {
        this.configServer = configServer;
        this.type = type;
        
        Database dbserver = null; 
        try 
        {
            dbserver = new Database(configServer);                
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            System.err.println("Creacion de conexion: " + ex.getMessage());
            error = ex;
            return;
        }
        try 
        {
            configOrservSenders(dbserver,configServer);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
            error = ex;
        }
    }
    
    private Sender nextSender()
    {
        if(senders.size() < 1) 
        {
            System.err.println("No se ha configurado la collecion de senders");
            return null;
        }
        sendersLast++;
        if(sendersLast > senders.size()-1) sendersLast = 0;
        while(!senders.get(sendersLast).getDisponible())
        {
            if(sendersLast < senders.size()) sendersLast = 0;
        }
        return senders.get(sendersLast);
    }
    
    public void close()
    {
        flRun = false;
    }
    
    public process.Mail nextMail(Connection connection) throws SQLException
    {
        process.Mail m = new process.Mail(-1);
        m.selectLast(connection,type);
        if(m.getID() < 1)
        {//no hay pendientes
            return null;
        }
        
        return m;
    }
    
    @Override
    public void run() 
    {
        int counter = 0;
        flRun = true;        
        Database dbserver = null;        
        while(flRun) 
        {
            counter++;
            try 
            {
                dbserver = new Database(configServer);                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                System.err.println("Creacion de conexion: " + ex.getMessage());
                error = ex;
                Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                //return;
            }            
            process.Mail mail = null;
            try 
            {
                mail = nextMail(dbserver.getConnection());
            }
            catch (SQLException ex) 
            {
                System.err.println(ex.getMessage());
                Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                error = ex;
            }
            
            if(mail == null)
            {
                try 
                {
                    synchronized(this)
                    {
                        this.wait(20000);
                    }
                    continue;
                }
                catch (InterruptedException ex) 
                {
                    Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                    error = ex;
                }
            }

            if(counter == 10)
            {
                try 
                {
                    configOrservSenders(dbserver,configServer);
                    counter = 0;
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                    error = ex;
                }
            }
            boolean flconsume = consume(dbserver,mail);
            if(flconsume == false)
            {
                try 
                {
                    configOrservSenders(dbserver,configServer);//actualiza la lista de senders segun la base de datos.
                }
                catch (SQLException ex) 
                {
                    //Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                    error = ex;
                }
            }
            try 
            {
                dbserver.getConnection().close();
                dbserver = null;
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                error = ex;
            }            
            
        }       
    }

    public Boolean consume(Database dbserver, Mail mail) 
    {
        if(mail == null) return true;
        Boolean flSend;
        try 
        {
            Sender sender = nextSender();
            flSend = send(dbserver,sender,mail);
        }
        catch (SAXException | ParserConfigurationException | IOException  | MessagingException | SQLException ex) 
        {
            //Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
            error = ex;
            return false;
        }
        
        Boolean flremove = null;
        if (flSend) 
        {
            try 
            {
                flremove = sended(dbserver,mail);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                error = ex;
                return false;
            }
        }
        else
        {
            try 
            {
                return mail.upFlag(dbserver, Mail.Flag.ERROR).isFlag();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        if(flremove == null) return false;
        if (flremove) 
        {
            try 
            {
                dbserver.commit();
                return true;
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                error = ex;
                return false;
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
                Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
                error = ex;
                return false;
            }
        }
        return false;
    }

    private void configOrservSenders(Database database,SIIL.core.config.Server configServer) throws SQLException 
    {
        Sender messeger;
        
        senders = new ArrayList<>();        
        if(database == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT username FROM ProcessSenders WHERE type =";
        if(type == TypeMail.ORSERV)
        {
            sql += "'orserv'";
        }
        else if(type == TypeMail.FACTURA)
        {
            sql += "'factura'";
        }
        
        Statement stmt = database.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        //System.out.println(sql);
        while(rs.next())
        {
            messeger = new Sender();
            messeger.configurate(type, rs.getString(1),configServer);
            senders.add(messeger);
        }        
    }

    private boolean send(Database dbserver, Sender sender, process.Mail mail) throws SQLException, MessagingException, IOException, ParserConfigurationException, SAXException 
    {
        return sender.send(dbserver, mail);
    }

    private Boolean sended(Database dbserver, Mail mail) throws SQLException 
    {
        return mail.upFlag(dbserver, Mail.Flag.SENDED).isFlag();
    }

    /**
     * @return the error
     */
    public Throwable getError() 
    {
        return error;
    }
}
