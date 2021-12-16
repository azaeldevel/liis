
package SIIL.service.quotation;

import SIIL.Server.Database;
import SIIL.trace.Trace;
import com.galaxies.andromeda.util.Texting.Confirmation;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import process.Mail;

/**
 *
 * @author Azael Reyes
 */
public class OrdenAlmacenada extends ServiceQuotation implements Runnable
{

    @Override
    public void run() {
        try {
            surtir();
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
    
    public Boolean surtir(Database db,com.galaxies.andromeda.util.Progress progress,String sa) throws SQLException, MessagingException, UnsupportedEncodingException
    {
        setServerDB(db);
        setSA(sa);
        setProgressObject(progress);        
        return surtir();
    }

    private Boolean surtir() throws SQLException, MessagingException, UnsupportedEncodingException 
    {
        progress.setProgress(20, "Iniciando Asistente de Trace...");
        setTrace(new Trace(credential.getBD(),credential.getUser(), "Asignación de S.A. - Cotización " + getFolio()));
        getTrace().insert(database);
        downCompany(database);
        progress.setProgress(45, "Enviando Datos a Servidor...");
        int flUpdate = updateSurtir();
        progress.setProgress(45, "Enviando correos a Servidor...");
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
                return false;
            }
        }
        else
        {
            getServerDB().rollback();
            progress.fail(new java.lang.Error("Fallo al enviar el correo electronico, la operacion se cancela."));  
            return false;
        }        
    }

    private boolean sendMail() throws MessagingException, UnsupportedEncodingException, AddressException, SQLException 
    {
        process.Mail mail = new process.Mail(-1);
        return mail.insert(getServerDB(), Sender.generateTO(database, this), Sender.generateCC(database,credential,this), Sender.generateBCC(), Sender.surtirMensaje(database, credential, this), "orserv", Sender.surtirSubject(database, credential, this));
    }
}
