
package SIIL.Server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author areyes
 */
@Deprecated
public class SenderLocal 
{
    private String Username;
    private String PassWord;
    protected String To;
    protected String Subject;
    protected String Mensage;
    Properties props;
    Session session;
    protected Message message;
    private static Integer smtpVector = 0;
    private static Integer smtpVectorMax = 10;
    private static ArrayList<String> smtpServers = null;
    Throwable fail;
    
    public SenderLocal()
    {
        if(smtpVector == 0 || smtpServers == null)
        {
            smtpVector = 1;
            smtpServers = new ArrayList<>();
        }
    }
    
    public String getSubject()
    {
        return Subject;
    }
    public String getMessage()
    {
        return Mensage;
    }
    
    public void init()
    {
        //seleccionar correo electronico
        Username = "";
        if( smtpVector < smtpVectorMax)
        {
            smtpVector++;
        }
        else
        {
            smtpVector = 3;
        }
        Username = "app." + smtpVector + "@siil.mx";
        
        PassWord = "s1115ad3CV";        
        props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtpout.secureserver.net");
        props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "80");
        props.put("mail.smtp.ssl.enable", "false");
        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Username, PassWord);
                    }
                });
        message = new MimeMessage(session);
        
    }
    
    public boolean send()
    {       
        try 
        {
            message.setFrom(new InternetAddress(Username,"SIIL Application"));
            //message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(To));
            message.setSubject(Subject);
            message.setContent(Mensage, "text/html; charset=utf-8");//message.setText(Mensage);

            Transport.send(message);
            //System.out.println("Correo enviado.");
        } 
        catch (UnsupportedEncodingException ex) 
        {
            Logger.getLogger(SenderLocal.class.getName()).log(Level.SEVERE, null, ex);
            fail = ex;
            return false;
        } 
        catch (MessagingException ex) 
        {
            Logger.getLogger(SenderLocal.class.getName()).log(Level.SEVERE, null, ex);
            fail = ex;
            return false;
        }
        return true;
    }
}
