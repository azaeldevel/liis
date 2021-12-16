
package SIIL.Server;

import SIIL.core.TypeMail;
import SIIL.core.config.Server;
import com.sun.mail.smtp.SMTPSendFailedException;
import core.bobeda.FTP;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import process.Attached;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class Sender 
{
    private Message message;
    private Boolean disponible;
    private TypeMail type;
    private Integer number;
    private String username;
    private String password;
    private Properties properties;
    private Session session;
    private Server configServer;
    
    
    public Sender()
    {
        disponible = true;
    }
    
    /**
     * 
     * @param database
     * @param mail
     * @return 
     * @throws java.sql.SQLException 
     */
    public boolean send(Database database, process.Mail mail) throws SQLException, MessagingException, IOException, SAXException, ParserConfigurationException
    {
        disponible = false;
        message = new MimeMessage(session);
        mail.download(database.getConnection());
        try 
        {
            message.setFrom(new InternetAddress(username,"SIIL Application"));
        }
        catch (MessagingException | UnsupportedEncodingException ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        try 
        {
            InternetAddress[]  list;
            if(mail.getTO() != null)
            {
                message.addRecipients(Message.RecipientType.TO, mail.getTO());
            }
            if(mail.getCC() != null)
            {
                message.addRecipients(Message.RecipientType.CC, mail.getCC());
            }
            if(mail.getBCC() != null)
            {
                message.addRecipients(Message.RecipientType.BCC, mail.getBCC());
            }
        }
        catch (MessagingException ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        try 
        {
            message.setSubject(mail.getSubject());
        }
        catch (MessagingException ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
                
        if(type == TypeMail.ORSERV)
        {
            try 
            {
                message.setContent(mail.getText(), "text/html; charset=utf-8");//message.setText(Mensage);
            } 
            catch (MessagingException ex) 
            {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }            
        }
        else if(type == TypeMail.FACTURA)
        {
            SIIL.core.config.Server serverConfig = new SIIL.core.config.Server(); 
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());     
            Database dbserver = null;
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(configServer);
            } 
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Creacion de conexion: " + ex.getMessage());
            }
            List<Attached> atts = Attached.search(database,mail);            
            Multipart multipart = new MimeMultipart();            
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(mail.getText(), "text/html");
            multipart.addBodyPart(textBodyPart);
            for(Attached att: atts)
            {
                try 
                {
                    String dir = System.getProperty("java.io.tmpdir");
                    att.download(database, ftpServer, dir);
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(att.getVault().getDownloadFileName());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(att.getVault().getNombre());
                    multipart.addBodyPart(messageBodyPart);
                    message.setContent(multipart);
                }
                catch (IOException ex) 
                {
                    Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }  
            }  
            ftpServer.close();
        }
        
        try 
        {
            Transport.send(message);
        }
        catch(SMTPSendFailedException ex)
        {
            System.err.println("For id = " + mail.getID() + " : " + ex.getMessage());
            /*OutputStream out = new FileOutputStream(configServer.getWorkspace().get("failmail")+ message.getMessageNumber());
            message.writeTo(out);
            out.flush();
            out.close();*/
            return false;
        }
        catch (MessagingException ex) 
        {
            //Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            return false;
        }
        disponible = true;
        return true;
    }
    
    /**
     * @return the message
     */
    public Message getMessage() 
    {
        return message;
    }

    /**
     * @return the disponible
     */
    public Boolean getDisponible() 
    {
        return disponible;
    }

    /**
     * @return the type
     */
    public TypeMail getType() 
    {
        return type;
    }

    /**
     * @return the number
     */
    public Integer getNumber() 
    {
        return number;
    }
    
    public Boolean configurate(TypeMail type, final String username,Server configServer)
    {
        this.configServer = configServer;
        this.type = type;
        this.username  = username;
        password = "s1115ad3CV";
        properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "smtpout.secureserver.net");
        properties.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "80");
        properties.put("mail.smtp.ssl.enable", "false");
        session = Session.getInstance(properties,
        new javax.mail.Authenticator() 
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() 
            {
                return new PasswordAuthentication(username, password);
            }
        });
        
        return false;        
    }
}
