
package SIIL.service.quotation;

import SIIL.trace.Trace;
import com.galaxies.andromeda.util.Texting.Confirmation;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import process.ImportsCN;
import process.Mail;

/**
 *
 * @author Azael
 */
public class OrdenAutorizar extends ServiceQuotation implements Runnable
{

    public OrdenAutorizar(ServiceQuotation o) 
    {
        super(o);
    }

    public OrdenAutorizar() 
    {
    }
    
    @Override
    public void run() 
    {
        try 
        {
            authorize();
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

    private void authorize() throws SQLException, MessagingException, UnsupportedEncodingException, IOException 
    {      
        //Actualizcion de Registros de cotizacion
        ImportsCN imp = new ImportsCN();
        downFolio(database.getConnection());
        imp.importCNQuotation(this, database);
        
        
        //
        progress.setProgress(40, "Iniciando asistente de Trace...");
        setTrace(new Trace(credential.getBD(), credential.getUser(), "Autorizacón - Cotización " + getFolio()));
        getTrace().insert(database);
        progress.setProgress(60, "Enviando datos hacia Servidor...");
        int flAutho = updateAutho(database,credential.getUser());        
        progress.setProgress(80, "Enviando correo hacia Servidor...");
        boolean flMail = sendMail();        
        progress.setProgress(90, "Enviando datos hacia Servidor...");
        progress.setProgress(80, "Correo enviado.");
        progress.setProgress(80, "Confirmando transacción...");
        if(flMail)
        {
            int mailCount = Mail.getCount(database);
            String comment;
            if(flAutho == 1)
            {
                getServerDB().commit();
                if(mailCount < 10)
                {
                    comment = "Operacion completada satisfactoriamente.";
                }
                else
                {
                    comment = "Operacion completada satisfactoriamente. Hay " + mailCount + " correos en espera.";
                }
                progress.finalized(new Confirmation(comment));
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
    }

    private boolean sendMail() throws MessagingException, UnsupportedEncodingException, AddressException, SQLException 
    {
        process.Mail mail = new process.Mail(-1);
        return mail.insert(getServerDB(), Sender.generateTO(database, this), Sender.generateCC(database,credential,this), Sender.generateBCC(), Sender.authoMensaje(database, credential, this), "orserv", Sender.authoSubject(database, credential, this));
    }
}
