
package SIIL.service.quotation;

import SIIL.Server.Database;
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
 * @author Azael
 */
public class OrdenETA extends ServiceQuotation implements Runnable
{

    public OrdenETA(ServiceQuotation o) 
    {
        super(o);
    }

    public OrdenETA() 
    {
    }

    @Override
    public void run() 
    {
        try 
        {
            eta();
        }  
        catch (SQLException ex) 
        {
            try 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Un error inseperado ocurrio, la base de datos no puede aceptar la operación",ex));
                getServerDB().rollback();
            } 
            catch (SQLException ex1) 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Se intento cancelar lo operación, sin embargo, un error inesperado ocurrio, el estado actual de los datos es ambiguo, consulte a su administrador.",ex1));
                Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex);            
        } 
        catch (MessagingException | UnsupportedEncodingException ex) 
        {
            try 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Falló el envio de correo electrónico",ex));
                getServerDB().rollback();
            } 
            catch (SQLException ex1) 
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("Se intento cancelar lo operación, sin embargo, un error inesperado ocurrio, el estado actual de los datos es ambiguo, consulte a su administrador.",ex1));
                Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(java.lang.Throwable ex)
        {
            progress.fail(new com.galaxies.andromeda.util.Texting.Error("Falló inesperado, consulte a su administrador",ex));
            Logger.getLogger(OrdenCotizada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public Boolean eta(Database db,java.util.Date fecha,com.galaxies.andromeda.util.Progress progress,Trace trace) throws SQLException, MessagingException, UnsupportedEncodingException
    {
        setServerDB(db);
        setFhETA(fecha);
        setProgressObject(progress);
        setTrace(trace);
        
        downCompany(database);
        int flETA = updateETA(database);       
        boolean flMail = senadMail();
        if(flMail)
        {            
            if(flETA == 1)
            {              
                return true;
            }
            else
            {
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("La inserrcion de datos resulto incosistente, no se guardanran los datos."));
                return false;
            }
        }
        else
        {
            progress.fail(new com.galaxies.andromeda.util.Texting.Error("Fallo al enviar el correo electronico, la operacion se cancela.")); 
            return false;
        }
    }
    
    private Boolean eta() throws SQLException, MessagingException, UnsupportedEncodingException 
    {
        progress.setProgress(20, "Iniciando Asistente de Trace...");
        setTrace(new Trace(credential.getBD(), credential.getUser(), "ETA - Cotización " + getFolio()));
        getTrace().insert(database);
        progress.setProgress(30, "Enviando Datos a Servidor...");
        downCompany(database);
        int flETA = updateETA(database);
        
        progress.setProgress(30, "Enviando Correo a Servidor...");        
        boolean flMail = senadMail();
        progress.setProgress(80, "Correo enviado.");
        if(flMail)
        {
            int mailCount = Mail.getCount(database);
            String comment;
            if(flETA == 1)
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
                progress.finalized(new Confirmation(comment));
                return true;
            }
            else
            {
                getServerDB().rollback();
                progress.fail(new com.galaxies.andromeda.util.Texting.Error("La inserrcion de datos resulto incosistente, no se guardanran los datos."));
                return false;
            }
        }
        else
        {
            getServerDB().rollback();
            progress.fail(new com.galaxies.andromeda.util.Texting.Error("Fallo al enviar el correo electronico, la operacion se cancela.")); 
            return false;
        }
    }

    private boolean senadMail() throws MessagingException, UnsupportedEncodingException, AddressException, SQLException 
    {
        process.Mail mail = new process.Mail(-1);
        return mail.insert(getServerDB(), Sender.generateTO(database, this), Sender.generateCC(database,credential,this), Sender.generateBCC(), Sender.pedMensaje(database, credential, this), "orserv", Sender.pedSubject(database, credential, this));
    }
}
