
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
public class OrdenFinalizar extends ServiceQuotation implements Runnable
{

    public OrdenFinalizar()
    {        
    }
    
    public OrdenFinalizar(ServiceQuotation o) 
    {
        super(o);
    }

    @Override
    public void run() 
    {
        try 
        {
            closeService();
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

    public boolean closeService(Database db,com.galaxies.andromeda.util.Progress progress,session.Credential cred) throws SQLException, MessagingException, UnsupportedEncodingException 
    {
        setServerDB(db);
        setProgressObject(progress);
        Throwable ret = fill(db, cred, getID());
        if(ret != null) return false;
        return closeService();
    }
    
    private boolean closeService() throws SQLException, MessagingException, UnsupportedEncodingException 
    {
        progress.setProgress(10, "Iniciando Asistente de Trace...");
        setTrace(new Trace(credential.getBD(), credential.getUser(), "Final - Cotización " + getFolio()));
        getTrace().insert(database);
        downCompany(database);
        progress.setProgress(10, "Enviando Datos a Servidor...");
        int flUpdate = updateFin();
        progress.setProgress(10, "Enviando correos a Servidor...");        
        boolean flMail = sendMail();
        if(flMail)
        {
            int mailCount = Mail.getCount(database);
            String comment;
            if(flUpdate == 1)
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
                progress.fail(new java.lang.Error("La inserrcion de datos resulto incosistente, no se guardanran los datos."));
            }
        }
        else
        {
            getServerDB().rollback();
            progress.fail(new java.lang.Error("Fallo al enviar el correo electronico, la operacion se cancela."));            
        }  
        return false;
    }

    private boolean sendMail() throws SQLException, AddressException
    {
        process.Mail mail = new process.Mail(-1);
        return mail.insert(getServerDB(), Sender.generateTO(database, this), Sender.generateCC(database,credential,this), Sender.generateBCC(), Sender.termMensaje(database, credential, this), "orserv", Sender.termSubject(database, credential, this));
    }        
}
