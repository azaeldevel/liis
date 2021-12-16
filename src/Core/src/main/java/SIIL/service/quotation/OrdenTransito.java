
package SIIL.service.quotation;

import SIIL.trace.Trace;
import com.galaxies.andromeda.util.Texting.Confirmation;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import process.Mail;

/**
 * 
 * @author Azael Reyes
 */
public class OrdenTransito extends ServiceQuotation implements Runnable
{
    public OrdenTransito()
    {
    } 
    
    public OrdenTransito(int ID)
    {
        super(ID);
    } 
        
    @Override
    public void run() 
    {
        try 
        {
            arrive();
        }  
        catch(com.sun.mail.smtp.SMTPSenderFailedException ex)
        {
            if(progress != null) progress.fail(new com.galaxies.andromeda.util.Texting.Error("Falló el envio de correo electrónico",ex));
            return;
        } 
        catch (SQLException ex) 
        {
            try 
            {
                if(progress != null) progress.fail(new com.galaxies.andromeda.util.Texting.Error("Un error inseperado ocurrio, la base de datos no puede aceptar la operación",ex));
                getServerDB().rollback();
            } 
            catch (SQLException ex1) 
            {
                if(progress != null) progress.fail(new com.galaxies.andromeda.util.Texting.Error("Se intento cancelar lo operación, sin embargo, un error inesperado ocurrio, el estado actual de los datos es ambiguo, consulte a su administrador.",ex1));
                Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex);
            return;            
        }
        catch (MessagingException | UnsupportedEncodingException ex) 
        {
            try 
            {
                if(progress != null) progress.fail(new com.galaxies.andromeda.util.Texting.Error("Falló el envio de correo electrónico",ex));
                getServerDB().rollback();
            } 
            catch (SQLException ex1) 
            {
                if(progress != null) progress.fail(new com.galaxies.andromeda.util.Texting.Error("Se intento cancelar lo operación, sin embargo, un error inesperado ocurrio, el estado actual de los datos es ambiguo, consulte a su administrador.",ex1));
                Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(java.lang.Throwable ex)
        {
            if(progress != null) progress.fail(new com.galaxies.andromeda.util.Texting.Error("Falló inesperado, consulte a su administrador",ex));
            Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void arrive() throws SQLException, MessagingException, UnsupportedEncodingException 
    {
        if(progress != null) progress.setProgress(20, "Iniciando Asistente de Trace...");
        downCompany(database);
        if(credential != null)
        {
            setTrace(new Trace(credential.getBD(), credential.getUser(), "Arribar - Cotización " + getFolio()));
            getTrace().insert(database);        
        }
        if(progress != null) progress.setProgress(30, "Enviando datos a Servidor...");
        int flArrive = updateArribo();
        if(progress != null) progress.setProgress(60, "Enviando correo a Servidor...");
        boolean flMail = sendMail();  
        if(flMail)
        {
            int mailCount = Mail.getCount(database);
            String comment;
            if(flArrive == 1)
            {
                getServerDB().commit();
                if(mailCount < 10)
                {
                    comment = "Operacion completada satisfactoriamente.";
                }
                else
                {
                    comment = "Operacion completada satisfactoriamente. Hay " + mailCount + " correos espera.";
                }
                if(progress != null) progress.finalized(new Confirmation(comment));
            }
            else
            {
                getServerDB().rollback();
                if(progress != null) progress.fail(new java.lang.Error("La inserrcion de datos resulto incosistente, no se guardanran los datos, registros afectados : " + flArrive));
            }
        }
        else
        {
            getServerDB().rollback();
            if(progress != null) progress.fail(new java.lang.Error("Fallo el envio de crroe electronico."));            
        }
        
    }

    private boolean sendMail() throws MessagingException, UnsupportedEncodingException, AddressException, SQLException 
    {
        process.Mail mail = new process.Mail(-1);
        return mail.insert(getServerDB(), Sender.generateTO(database, this), Sender.generateCC(database,credential,this), Sender.generateBCC(), Sender.confirmMensaje(database, credential, this), "orserv", Sender.confirmSubject(database, credential, this));
    }    
}
